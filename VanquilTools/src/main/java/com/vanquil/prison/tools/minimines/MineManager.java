package com.vanquil.prison.tools.minimines;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vanquil.prison.tools.VanquilTools;
import com.vanquil.prison.tools.config.Config;
import com.vanquil.prison.tools.util.C;
import com.vanquil.prison.tools.util.Scheduling;
import com.vanquil.prison.tools.util.dimension.BlockPos;
import com.vanquil.prison.tools.util.listeners.Listeners;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Map;

public class MineManager {
    public static class MinesConfig {
        private static final String CONFIG_NAME = "mines.json";

        private List<Mine.Config> mines = Lists.newArrayList();
        private String mineResettingMessage = "Mine \"%s\" has been reset.";

        public List<Mine.Config> mines() {
            return mines;
        }
    }

    private final List<Mine> toReset = Lists.newArrayList();
    private final Map<String, Mine> mineMap = Maps.newHashMap();
    private final Config<MinesConfig> config;

    public MineManager() {
        this.config = new Config<>(
                VanquilTools.basePath.resolve(MinesConfig.CONFIG_NAME),
                MinesConfig.class, MinesConfig::new);
        for (Mine.Config mine : config.instance().mines) {
            Mine mine1 = new Mine(mine);
            mineMap.put(mine.name(), mine1);
            toReset.add(mine1);
        }

        Scheduling.repeat(() -> {
            for (Mine mine : toReset) {
                MinesConfig conf = config.instance();
                String message = conf.mineResettingMessage;
                C.broadcast(message, mine.config());
                mine.reset();
            }
        }, 5L, 5L);

        Scheduling.repeat(() -> {
            updateHolograms();
            queryResetting();
        }, 1L, 1L);

        protectionListeners();
    }

    private void protectionListeners() {
        Listeners.registerListener(0, BlockBreakEvent.class, event -> {
            if (getMineAtLocation(event.getPlayer().getLocation()) == null) {
                event.setCancelled(true);
            }
        });
    }

    private Mine getMineAtLocation(Location location) {
        BlockPos pos = BlockPos.of(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        for (Mine mine : mineMap.values()) {
            if (mine.config().cuboid().containsLocation(pos))
                return mine;
        }
        return null;
    }

    public void updateHolograms() {
        // TODO
    }

    public void queryResetting() {
        for (Map.Entry<String, Mine> entry : mineMap.entrySet()) {
            Mine mine = entry.getValue();
            if (mine.requiresReset()) {
                toReset.add(mine);
            }
        }
    }
}
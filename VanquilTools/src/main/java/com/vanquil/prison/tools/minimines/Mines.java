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
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Mines {
    public static class MinesConfig {
        private static final String CONFIG_NAME = "mines.json";

        private List<Mine.Config> mines = Lists.newArrayList();
        private String mineResettingMessage = "Mine \"{0}\" has been reset.";

        public List<Mine.Config> mines() {
            return mines;
        }
    }

    public static class MineListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        @SuppressWarnings("unused")
        public void onEvent(BlockBreakEvent event) {
            Block block = event.getBlock();
            Mine mine = Mines.getMineAtLocation(block.getLocation());
            if (mine != null) {
                mine.decCurrentVolume();
            }
        }
    }

    private static final List<Mine> TO_RESET = Lists.newArrayList();
    private static final Map<String, Mine> MINE_MAP = Maps.newHashMap();
    private static final Config<MinesConfig> CONFIG;

    static {
        CONFIG = new Config<>(
                VanquilTools.basePath.resolve(MinesConfig.CONFIG_NAME),
                MinesConfig.class, MinesConfig::new);
        for (Mine.Config mine : CONFIG.instance().mines) {
            Mine mine1 = new Mine(mine);
            MINE_MAP.put(mine.name(), mine1);
            TO_RESET.add(mine1);
        }

        Listeners.registerListener(new MineListener());

        Scheduling.repeat(() -> {
            Iterator<Mine> iterator = TO_RESET.iterator();
            while (iterator.hasNext()) {
                Mine next = iterator.next();
                MinesConfig conf = CONFIG.instance();
                String message = conf.mineResettingMessage;
                C.broadcast(message, next.config().displayName());
                next.reset();
                iterator.remove();
            }
        }, 5, 5);

        Scheduling.repeat(() -> {
            updateHolograms();
            queryResetting();
        }, 1, 1);
    }

    public static Mine getMineAtLocation(BlockPos pos) {
        for (Mine mine : MINE_MAP.values()) {
            if (mine.config().cuboid().containsLocation(pos)) {
                return mine;
            }
        }
        return null;
    }

    public static Mine getMineAtLocation(Location location) {
        BlockPos pos = BlockPos.of(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        for (Mine mine : MINE_MAP.values()) {
            if (mine.config().cuboid().containsLocation(pos))
                return mine;
        }
        return null;
    }

    public static void updateHolograms() {
        // TODO
    }

    public static void queryResetting() {
        for (Map.Entry<String, Mine> entry : MINE_MAP.entrySet()) {
            Mine mine = entry.getValue();
            if (mine.requiresReset()) {
                TO_RESET.add(mine);
            }
        }
    }

    public static void load() {
    }
}
package com.vanquil.prison.tools.minimines;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vanquil.prison.tools.VanquilTools;
import com.vanquil.prison.tools.config.Config;
import com.vanquil.prison.tools.util.C;
import com.vanquil.prison.tools.util.Scheduling;

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
        Scheduling.repeat(() -> {
            for (Mine mine : toReset) {
                MinesConfig conf = config.instance();
                String message = conf.mineResettingMessage;
                C.broadcast(message, mine.config());
                mine.reset();
            }
        }, 5L, 5L);
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

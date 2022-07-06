package com.vanquil.prison.tools.minimines.mine;

import com.google.common.collect.Maps;
import com.vanquil.prison.tools.util.dimension.Cuboid;
import org.bukkit.Material;

import java.util.Map;

public class Mine {
    private String world;
    private Cuboid cuboid;
    private String displayName;
    private long lastResetEpoch;
    private int currentVolume;

    public static class Config {
        private String worldName;
        private Cuboid cuboid;
        private String displayName;
        private double resetVolumeThreshold = 0.3; // Reset if <= 30% volume.
        private Map<Material, Double> blockDistribution;

        public double resetVolumeThreshold() {
            return resetVolumeThreshold;
        }

        public String displayName() {
            return displayName;
        }

        public String worldName() {
            return worldName;
        }

        public Cuboid cuboid() {
            return cuboid;
        }
    }
}

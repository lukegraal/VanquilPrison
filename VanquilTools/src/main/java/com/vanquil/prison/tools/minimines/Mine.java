package com.vanquil.prison.tools.minimines;

import com.google.common.collect.Maps;
import com.vanquil.prison.tools.util.dimension.Cuboid;
import com.vanquil.prison.tools.util.material.Material2;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Mine {
    public static class Config {
        private String worldName;
        private Cuboid cuboid;
        private String displayName;
        private double resetVolumeThreshold = 0.3; // Reset if <= 30% volume.
        private List<Pair<Material2, Double>> blockDistribution;

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

    private final Config config;
    private final EnumeratedDistribution<Material2> distribution;
    private long currentVolume;

    public Mine(Config config) {
        this.distribution = new EnumeratedDistribution<>(config.blockDistribution);
        this.currentVolume = config.cuboid.volumePos().size();
        this.config = config;
    }

    public double volumePercentage() {
        return ((double) currentVolume / config.cuboid.volume());
    }

    public boolean requiresReset() {
        return (volumePercentage() <= config.resetVolumeThreshold);
    }

    public void forceReset() {
        Cuboid cuboid = config.cuboid;
        cuboid.setTo(config.worldName, distribution);
        currentVolume = config.cuboid.volume();
    }
}

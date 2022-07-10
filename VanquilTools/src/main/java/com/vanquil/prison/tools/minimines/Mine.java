package com.vanquil.prison.tools.minimines;

import com.vanquil.prison.tools.util.dimension.BlockPos;
import com.vanquil.prison.tools.util.dimension.Cuboid;
import com.vanquil.prison.tools.util.dimension.EntityPos;
import com.vanquil.prison.tools.util.material.Material2;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Mine {
    public static class Config {
        private String worldName;
        private Cuboid cuboid;
        private EntityPos spawnPosition, hologramPosition = null;
        private String name, displayName, permission = null;
        private double resetVolumeThreshold = 0.3; // Reset if <= 30% volume.
        private List<MaterialChance> blockDistribution;

        public static class MaterialChance {
            private Material2 material;
            private double chance;

            public Pair<Material2, Double> convert() {
                return new Pair<>(material, chance);
            }
        }

        public List<Pair<Material2, Double>> getBlockDistribution() {
            return blockDistribution.stream().map(MaterialChance::convert).collect(Collectors.toList());
        }

        public String name() {
            return name;
        }

        public EntityPos spawnPosition() {
            return spawnPosition;
        }

        public double resetVolumeThreshold() {
            return resetVolumeThreshold;
        }

        public String displayName() {
            return displayName;
        }

        public String worldName() {
            return worldName;
        }

        public String permission() {
            return permission;
        }

        public Cuboid cuboid() {
            return cuboid;
        }
    }

    private final Config config;
    private final World world;
    private final EnumeratedDistribution<Material2> distribution;
    private long currentVolume;

    public Mine(Config config) {
        this.distribution = new EnumeratedDistribution<>(config.getBlockDistribution());
        this.currentVolume = config.cuboid.volumePos().size();
        this.world = Bukkit.getWorld(config.worldName);
        if (world == null)
            throw new RuntimeException("Nonexistent world \"" + config.worldName
                    + "\" specified for mine \"" + config.name + "\".");
        this.config = config;
    }

    public Config config() {
        return config;
    }

    public double volumePercentage() {
        return ((double) currentVolume) / config.cuboid.volume();
    }

    public boolean requiresReset() {
        return (volumePercentage() <= config.resetVolumeThreshold);
    }

    public void decCurrentVolume() {
        --currentVolume;
    }

    // for JH
    public Set<BlockPos> getLayerPositions(int y) {
        return config.cuboid
                .volumePos().stream()
                .filter(b -> b.y() == y)
                .collect(Collectors.toSet());
    }

    public void reset() {
        // Teleport out players
        for (Player player : world.getPlayers()) {
            Location loc = player.getLocation();
            EntityPos pos = new EntityPos(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            if (config.cuboid.containsLocation(pos)) {
                CraftPlayer player1 = (CraftPlayer) player;
                player1.getHandle().mount(null);
                player1.getHandle().setLocation(pos.x(), pos.y(), pos.z(), pos.yaw(), pos.pitch());
            }        }
        Cuboid cuboid = config.cuboid;
        cuboid.setTo(config.worldName, distribution);
        currentVolume = config.cuboid.volume();
    }
}

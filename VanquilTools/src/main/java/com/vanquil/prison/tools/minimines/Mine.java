package com.vanquil.prison.tools.minimines;

import com.vanquil.prison.tools.util.MinMax;
import com.vanquil.prison.tools.util.dimension.BlockPos;
import com.vanquil.prison.tools.util.dimension.Cuboid;
import com.vanquil.prison.tools.util.dimension.EntityPos;
import com.vanquil.prison.tools.util.listeners.Listeners;
import com.vanquil.prison.tools.util.material.Material2;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Mine {
    public static class Config {
        private String worldName;
        private Cuboid cuboid;
        private EntityPos spawnPosition;
        private String name;
        private String displayName;
        private double resetVolumeThreshold = 0.3; // Reset if <= 30% volume.
        private List<Pair<Material2, Double>> blockDistribution;

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

        public Cuboid cuboid() {
            return cuboid;
        }
    }

    private final Config config;
    private final World world;
    private final EnumeratedDistribution<Material2> distribution;
    private long currentVolume;

    public Mine(Config config) {
        this.distribution = new EnumeratedDistribution<>(config.blockDistribution);
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
        return ((double) currentVolume / config.cuboid.volume());
    }

    public boolean requiresReset() {
        return (volumePercentage() <= config.resetVolumeThreshold);
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
            }
        }
        Cuboid cuboid = config.cuboid;
        cuboid.setTo(config.worldName, distribution);
        currentVolume = config.cuboid.volume();
    }
}

package com.vanquil.prison.tools.util.dimension;

import com.google.common.collect.Sets;
import com.vanquil.prison.tools.util.MinMax;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.Set;

public class Cuboid {
    private final BlockPos a, b;
    private transient long volume = -1;

    public Cuboid(BlockPos a, BlockPos b) {
        this.a = a;
        this.b = b;
    }

    public BlockPos a() {
        return a;
    }

    public BlockPos b() {
        return b;
    }

    public long volume() {
        if (volume == -1) {
            MinMax mx = MinMax.of(a.x(), b.x()),
                    my = MinMax.of(a.y(), b.y()),
                    mz = MinMax.of(a.z(), b.z());
            int l = mx.max() - mx.min(),
                    h = my.max() - my.min(),
                    w = mz.max() - mz.min();
            volume = ((long) l) * h * w;
        }
        return volume;
    }

    public Set<BlockPos> volumePos() {
        Set<BlockPos> set = Sets.newHashSet();
        MinMax  mx = MinMax.of(a.x(), b.x()),
                my = MinMax.of(a.y(), b.y()),
                mz = MinMax.of(a.z(), b.z());
        for (int y = my.min(); y < my.max(); y++)
            for (int x = mx.min(); x < mx.max(); x++)
                for (int z = mz.min(); z < mz.max(); z++)
                    set.add(BlockPos.of(x, y, z));
        return set;
    }

    public void setTo(String world, EnumeratedDistribution<Material> materials) {
        World w = Bukkit.getWorld(world);
        for (BlockPos pos : volumePos()) {
            WorldServer handle = ((CraftWorld) w).getHandle();

        }
    }
}

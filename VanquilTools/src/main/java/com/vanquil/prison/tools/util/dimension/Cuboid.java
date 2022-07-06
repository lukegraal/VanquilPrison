package com.vanquil.prison.tools.util.dimension;

import com.google.common.collect.Sets;
import com.vanquil.prison.tools.util.MinMax;
import com.vanquil.prison.tools.util.material.Material2;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

import java.util.Set;

public class Cuboid {
    private final BlockPos a, b;
    private transient long volume = -1;
    private transient Set<BlockPos> volumePositions = null;

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
        if (volumePositions == null) {
            Set<BlockPos> set = Sets.newHashSet();
            MinMax mx = MinMax.of(a.x(), b.x()),
                    my = MinMax.of(a.y(), b.y()),
                    mz = MinMax.of(a.z(), b.z());
            for (int y = my.min(); y < my.max(); y++)
                for (int x = mx.min(); x < mx.max(); x++)
                    for (int z = mz.min(); z < mz.max(); z++)
                        set.add(BlockPos.of(x, y, z));
            volumePositions = set;
        }
        this.volume = volumePositions.size();
        return volumePositions;
    }

    public void setTo(String world, EnumeratedDistribution<Material2> materials) {
        World w = Bukkit.getWorld(world);
        for (BlockPos pos : volumePos()) {
            WorldServer handle = ((CraftWorld) w).getHandle();
            BlockPosition blockPos = new BlockPosition(pos.x(), pos.y(), pos.z());
            Material2 sample = materials.sample();
            IBlockData blockData = Block.getById(sample.id()).fromLegacyData(sample.data());
            boolean success = handle.setTypeAndData(blockPos, blockData, 2);
            if (success) handle.notify(blockPos);
        }
    }
}

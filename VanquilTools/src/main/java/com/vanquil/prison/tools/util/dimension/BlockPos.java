package com.vanquil.prison.tools.util.dimension;

public class BlockPos {
    private final int x, y, z;

    private BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static BlockPos of(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    @Override
    public String toString() {
        return "BlockPos{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

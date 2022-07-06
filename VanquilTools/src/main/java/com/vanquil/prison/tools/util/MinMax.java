package com.vanquil.prison.tools.util;

public class MinMax {
    private final int min, max;

    private MinMax(int a, int b) {
        if (a < b) {
            min = a;
            max = b;
        } else {
            min = b;
            max = a;
        }
    }

    public static MinMax of(int a, int b) {
        return new MinMax(a, b);
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }
}

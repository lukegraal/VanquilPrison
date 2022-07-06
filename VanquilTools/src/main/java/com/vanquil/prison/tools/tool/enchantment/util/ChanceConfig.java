package com.vanquil.prison.tools.tool.enchantment.util;

import com.google.common.collect.Maps;

import java.util.Map;

public class ChanceConfig {
    private final double initialChance = 0.05;
    private final double chanceMultiplier = 1.01;
    private final Map<Integer, Double> chanceOverrides = Maps.newHashMap();

    public double initialChance() {
        return initialChance;
    }

    public double chanceMultiplier() {
        return chanceMultiplier;
    }

    public Map<Integer, Double> chanceOverride() {
        return chanceOverrides;
    }

    public double getChance(int level) {
        Double chance = chanceOverrides.get(level);
        if (chance == null)
            chance = initialChance * chanceMultiplier;
        return chance;
    }
}

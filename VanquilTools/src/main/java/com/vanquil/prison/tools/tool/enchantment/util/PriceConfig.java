package com.vanquil.prison.tools.tool.enchantment.util;

import com.google.common.collect.Maps;

import java.util.Map;

public class PriceConfig {
    private final long initialPrice = 1_000L;
    private final double priceMultiplier = 1.10;
    private final Map<Integer, Long> priceOverrides = Maps.newHashMap();

    public long initialPrice() {
        return initialPrice;
    }

    public double priceMultiplier() {
        return priceMultiplier;
    }

    public Map<Integer, Long> priceOverrides() {
        return priceOverrides;
    }

    public long getPrice(int level) {
        Long price = priceOverrides.get(level);
        if (price == null)
            price = (long) (initialPrice * priceMultiplier);
        return price;
    }
}

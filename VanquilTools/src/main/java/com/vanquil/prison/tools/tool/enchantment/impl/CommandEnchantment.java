package com.vanquil.prison.tools.tool.enchantment.impl;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConditionalEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.util.ChanceConfig;
import com.vanquil.prison.tools.tool.enchantment.util.PriceConfig;
import com.vanquil.prison.tools.util.C;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;

public class CommandEnchantment
        implements UpgradeableEnchantment, ConditionalEnchantment {
    public static class Config {
        String uniqueName;
        int maxLevel;
        PriceConfig pricing = new PriceConfig();
        ChanceConfig chance = null;
        String displayName;
        String description;
        ToolType toolType;

        // {0} -> player name
        // {1} -> level
        Map<Integer, List<String>> executions;

        public String getUniqueName() {
            return uniqueName;
        }
    }

    private final Config config;

    public CommandEnchantment(Config config) {
        this.config = config;
    }

    @Override
    public ToolType type() {
        return config.toolType;
    }

    @Override
    public String uniqueName() {
        return config.uniqueName;
    }

    @Override
    public String displayName() {
        return config.displayName;
    }

    @Override
    public String description() {
        return config.description;
    }

    @Override
    public boolean testCondition(EnchantmentUseContext context) {
        if (config.chance == null) {
            return true;
        } else {
            return RandomUtils.nextDouble() <= config.chance.getChance(context.toolMetadata().getEnchantmentLevel(this));
        }
    }

    @Override
    public void apply(EnchantmentUseContext context) {
        int level = context.toolMetadata().getEnchantmentLevel(this);
        List<String> executions = null;
        int lastHeader = -1;
        for (Map.Entry<Integer, List<String>> entry : config.executions.entrySet()) {
            if (entry.getKey() > lastHeader
                    && level >= entry.getKey()) {
                lastHeader = entry.getKey();
                executions = entry.getValue();
            }
        }
        if (executions != null) {
            for (String execution : executions) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        C.format(execution, context.player().getName(), level));
            }
        }
    }

    @Override
    public long priceToUpgrade(int level) {
        return config.pricing.getPrice(level);
    }

    @Override
    public int maxLevel() {
        return config.maxLevel;
    }
}

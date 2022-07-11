package com.vanquil.prison.tools.tool.enchantment.config;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.tool.enchantment.impl.CommandEnchantment;

import java.util.List;

public class CustomEnchantmentConfig {
    private final List<CommandEnchantment.Config> customEnchantments = Lists.newArrayList();

    public List<CommandEnchantment.Config> customEnchantments() {
        return customEnchantments;
    }
}

package com.vanquil.prison.tools.tool.enchantment;

import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import org.bukkit.enchantments.Enchantment;

public interface VanillaEnchantment
        extends UpgradeableEnchantment {
    Enchantment vanillaEnchantment();

    @Override
    default void apply(EnchantmentUseContext context) {
    }
}

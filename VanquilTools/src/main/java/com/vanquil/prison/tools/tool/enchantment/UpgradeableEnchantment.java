package com.vanquil.prison.tools.tool.enchantment;

public interface UpgradeableEnchantment
        extends ToolEnchantment {
    long priceToUpgrade(int level);
    int maxLevel();
}

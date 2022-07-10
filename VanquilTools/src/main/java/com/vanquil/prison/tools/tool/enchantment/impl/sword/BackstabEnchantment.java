package com.vanquil.prison.tools.tool.enchantment.impl.sword;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConditionalEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.util.ChanceConfig;
import org.apache.commons.lang.math.RandomUtils;

public class BackstabEnchantment implements ConditionalEnchantment, UpgradeableEnchantment,
        ConfigurableEnchantment<BackstabEnchantment.Config> {
    private static final String NAME = "sword_backstab";

    public static class Config {
        final int maxLevel = 5;
        final String displayName = "&eBackstab";
        final String description = "Increases damage when you strike a player with your sword from behind.";
        final int damagePerHit = 6; // 3 hearts
        final ChanceConfig chance = new ChanceConfig();
    }

    private Config config = new Config();

    @Override
    public boolean testCondition(EnchantmentUseContext context) {
        return RandomUtils.nextDouble() <= config.chance.getChance(context.toolMetadata().getEnchantmentLevel(this));
    }

    @Override
    public Config config() {
        return config;
    }

    @Override
    public void updateConfig(Config config) {
        this.config = config;
    }

    @Override
    public ToolType type() {
        return null;
    }

    @Override
    public String uniqueName() {
        return NAME;
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
    public void apply(EnchantmentUseContext context) {

    }

    @Override
    public long priceToUpgrade(int level) {
        return 0;
    }

    @Override
    public int maxLevel() {
        return 0;
    }
}

package com.vanquil.prison.tools.tool.enchantment.impl.sword;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConditionalEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.context.WeaponUseContext;
import com.vanquil.prison.tools.tool.enchantment.util.ChanceConfig;
import org.apache.commons.lang.math.RandomUtils;

public class BackstabEnchantment implements ConditionalEnchantment, UpgradeableEnchantment,
        ConfigurableEnchantment<BackstabEnchantment.Config> {
    private static final String NAME = "sword_backstab";

    public static class Config {
        final int maxLevel = 5;
        final String displayName = "&cBackstab";
        final String description = "&7Increases damage when you strike a player with your sword from behind.";
        final double damageMultiplier = 1.3; // 30% more damage
        final ChanceConfig chance = new ChanceConfig();
    }

    private Config config = new Config();

    @Override
    public boolean testCondition(EnchantmentUseContext context) {
        int level = context.toolMetadata().getEnchantmentLevel(this);
        if (level == 0) return false;
        if (RandomUtils.nextDouble() <= config.chance.getChance(level)) {
            if (context instanceof WeaponUseContext) {
                WeaponUseContext ctx = (WeaponUseContext) context;
                float targetYaw = ctx.target().getLocation().getYaw();
                float playerYaw = ctx.player().getLocation().getYaw();

            }
        }
        return false;
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
        return ToolType.SWORD;
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

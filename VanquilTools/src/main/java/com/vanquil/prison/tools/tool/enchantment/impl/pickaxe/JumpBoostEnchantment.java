package com.vanquil.prison.tools.tool.enchantment.impl.pickaxe;

import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.PotionEffectEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.util.PriceConfig;
import com.vanquil.prison.tools.util.C;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpBoostEnchantment implements
        PotionEffectEnchantment,
        ConfigurableEnchantment<JumpBoostEnchantment.Config> {
    private static final String NAME = "pickaxe_jump_boost";

    public static class Config {
        final String displayName = "&cJump Boost";
        final String description = "Gives you the Jump Boost effect while you're holding the tool.";
        final int maxLevel = 3;
        final PriceConfig pricing = new PriceConfig();
    }

    private Config config = new Config();

    @Override
    public Config config() {
        return config;
    }

    @Override
    public void updateConfig(Config config) {
        this.config = config;
    }

    @Override
    public String displayName() {
        return C.format(config.displayName);
    }

    @Override
    public PotionEffect effect(EnchantmentUseContext context) {
        ToolMetadata metadata = context.toolMetadata();
        int level = metadata.getEnchantmentLevel(this);
        if (level > 0)
            return new PotionEffect(PotionEffectType.SPEED, 120, level-1, false, false);
        return null;
    }

    @Override
    public ToolType type() {
        return ToolType.PICKAXE;
    }

    @Override
    public String uniqueName() {
        return NAME;
    }

    @Override
    public String description() {
        return config.description;
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

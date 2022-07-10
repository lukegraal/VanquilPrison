package com.vanquil.prison.tools.tool.enchantment.impl.pickaxe;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.PotionEffectEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.util.C;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class NightVisionEnchantment
        implements PotionEffectEnchantment, ConfigurableEnchantment<NightVisionEnchantment.Config> {
    public static final String NAME = "pickaxe_night_vision";

    public static class Config {
        final String displayName = "&cNight Vision";
        final String description = "Gives you the Night Vision effect while you're holding the tool.";
        final long price = 1_000;
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
    public PotionEffect effect(EnchantmentUseContext context) {
        int level = context.toolMetadata().getEnchantmentLevel(this);
        if (level > 0) {
            return new PotionEffect(PotionEffectType.NIGHT_VISION, 320, 0, false, false);
        } else {
            return null;
        }
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
    public String displayName() {
        return C.format(config.displayName);
    }

    @Override
    public String description() {
        return config.description;
    }

    @Override
    public long priceToUpgrade(int level) {
        return config.price;
    }

    @Override
    public int maxLevel() {
        return 1;
    }
}

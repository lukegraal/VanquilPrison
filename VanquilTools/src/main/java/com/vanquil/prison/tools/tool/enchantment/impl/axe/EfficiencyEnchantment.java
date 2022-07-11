package com.vanquil.prison.tools.tool.enchantment.impl.axe;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.VanillaEnchantment;
import com.vanquil.prison.tools.tool.enchantment.util.PriceConfig;
import org.bukkit.enchantments.Enchantment;

public class EfficiencyEnchantment
        implements VanillaEnchantment, ConfigurableEnchantment<EfficiencyEnchantment.Config> {
    private static final String NAME = "axe_efficiency";

    public static class Config {
        final int maxLevel = 50;
        final String displayName = "&cEfficiency";
        final String description = "&7Makes you mine things faster!";
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
    public ToolType type() {
        return ToolType.AXE;
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
    public long priceToUpgrade(int level) {
        return config.pricing.getPrice(level);
    }

    @Override
    public int maxLevel() {
        return config.maxLevel;
    }

    @Override
    public Enchantment vanillaEnchantment() {
        return Enchantment.DIG_SPEED;
    }
}

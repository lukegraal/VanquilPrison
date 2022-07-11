package com.vanquil.prison.tools.tool.enchantment.impl.axe;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConditionalEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.BlockToolUseContext;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.util.ChanceConfig;
import com.vanquil.prison.tools.tool.enchantment.util.PriceConfig;
import com.vanquil.prison.tools.util.material.Material2;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class RadiusEnchantment
        implements UpgradeableEnchantment, ConditionalEnchantment, ConfigurableEnchantment<RadiusEnchantment.Config> {
    private static final String NAME = "axe_radius";

    public static class Config {
        final String displayName = "&cRadius";
        final String description = "&7Break a series of crops instantly within a specific radius";
        final List<Material2> materials = Lists.newArrayList(
                Material2.PUMPKIN,
                Material2.BLOCK_OF_MELON
        );
        final int maxLevel = 1_000;
        final int radius = 5;
        final ChanceConfig chance = new ChanceConfig();
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
    public boolean testCondition(EnchantmentUseContext context) {
        double chance = config.chance.getChance(context.toolMetadata().getEnchantmentLevel(this));
        return RandomUtils.nextDouble() <= chance;
    }

    @Override
    public void apply(EnchantmentUseContext context) {
        BlockToolUseContext ctx = (BlockToolUseContext) context;
        BlockBreakEvent event = ctx.event();
        Block block = event.getBlock();
        Material type = block.getType();
        //noinspection deprecation
        if (!config.materials.contains(Material2.fromId(type.getId())))
            return;
        int rad = config.radius;
        for (int x = block.getX()-rad; x < block.getX()+rad; x++) {
            for (int z = block.getZ()-rad; z < block.getZ()+rad; z++) {
                Block blockAt = block.getWorld().getBlockAt(x, block.getY(), z);
                if (type == blockAt.getType()) {
                    block.setType(Material.AIR, true);
                }
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

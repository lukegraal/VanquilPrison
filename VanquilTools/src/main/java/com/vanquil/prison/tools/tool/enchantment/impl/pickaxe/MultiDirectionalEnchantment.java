package com.vanquil.prison.tools.tool.enchantment.impl.pickaxe;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConditionalEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.BlockToolUseContext;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.util.ChanceConfig;
import com.vanquil.prison.tools.tool.enchantment.util.PriceConfig;
import com.vanquil.prison.tools.util.C;
import com.vanquil.prison.tools.util.dimension.BlockPos;
import com.vanquil.prison.tools.util.dimension.Cuboid;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MultiDirectionalEnchantment implements
        UpgradeableEnchantment, ConditionalEnchantment,
        ConfigurableEnchantment<MultiDirectionalEnchantment.Config> {
    public static final String NAME = "pickaxe_multi_directional";

    public static class Config {
        final String displayName = "&cMultiDirectional";
        final String description = "Increases your change to combust the X and Z axes of the block.";
        final int maxLevel = 1_000;
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
        return ToolType.PICKAXE;
    }

    @Override
    public String description() {
        return config.description;
    }

    @Override
    public String displayName() {
        return C.format(config.displayName);
    }

    @Override
    public String uniqueName() {
        return NAME;
    }

    @Override
    public boolean testCondition(EnchantmentUseContext context) {
        double chance = config.chance.getChance(context.toolMetadata().getEnchantmentLevel(this));
        return (RandomUtils.nextDouble() <= chance);
    }

    @Override
    public void apply(EnchantmentUseContext context) {
        BlockToolUseContext ctx = (BlockToolUseContext) context;
        Block b = ctx.event().getBlock();
        int x = b.getX(), z = b.getZ();
        Cuboid cuboid = ctx.mine().config().cuboid();
        World world = ctx.event().getBlock().getWorld();
        for (BlockPos pos : cuboid.volumePos()) {
            if (pos.x() == x || pos.z() == z) {
                Block block = world.getBlockAt(pos.x(), pos.y(), pos.z());
                block.setType(Material.AIR, false);
            }
        }
    }

    @Override
    public int maxLevel() {
        return config.maxLevel;
    }

    @Override
    public long priceToUpgrade(int level) {
        return config.pricing.getPrice(level);
    }
}

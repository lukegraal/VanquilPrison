package com.vanquil.prison.tools.tool.enchantment.impl.pickaxe;

import com.google.common.cache.Cache;
import com.vanquil.prison.tools.minimines.Mine;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConditionalEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.BlockToolUseContext;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.util.ChanceConfig;
import com.vanquil.prison.tools.tool.enchantment.util.PriceConfig;
import com.vanquil.prison.tools.util.dimension.BlockPos;
import com.vanquil.prison.tools.util.dimension.Cuboid;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CombustiveEnchantment implements
        UpgradeableEnchantment, ConditionalEnchantment,
        ConfigurableEnchantment<CombustiveEnchantment.Config> {
    public static final String NAME = "pickaxe_combustive";

    public static class Config {
        final int maxLevel = 1_000;
        final int explosionRadius = 10;
        final PriceConfig pricing = new PriceConfig();
        final ChanceConfig chance = new ChanceConfig();
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
    public String uniqueName() {
        return NAME;
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
    public boolean testCondition(EnchantmentUseContext context) {
        int l = context.toolMetadata().getEnchantmentLevel(this);
        double chance = config.chance.getChance(l);
        return RandomUtils.nextDouble() <= chance;
    }

    @Override
    public void apply(EnchantmentUseContext context) {
        BlockToolUseContext ctx = (BlockToolUseContext) context;
        Block centreBlock = ctx.event().getBlock();
        World world = centreBlock.getWorld();
        Mine mine = ctx.mine();
        Cuboid cuboid = mine.config().cuboid();
        int cx = centreBlock.getX(), cy = centreBlock.getY(), cz = centreBlock.getZ();
        int r = config.explosionRadius;
        int maxrx = centreBlock.getX() + r, maxry = centreBlock.getY() + r, maxrz = centreBlock.getZ() + r;
        int minrx = centreBlock.getX() - r, minry = centreBlock.getY() - r, minrz = centreBlock.getZ() - r;
        double radSqr = Math.pow(r, 2);
        for (int x = minrx; x < maxrx; x++) {
            for (int y = minry; y < maxry; y++) {
                for (int z = minrz; z < maxrz; z++) {
                    double distSqr = Math.pow((cx-x), 2) + Math.pow((cy-y), 2) + Math.pow((cz-z), 2);
                    if (distSqr < radSqr && cuboid.containsLocation(BlockPos.of(x, y, z))) {
                        Block blockAt = world.getBlockAt(x, y, z);
                        if (blockAt.getType() != Material.AIR)
                            blockAt.setType(Material.OBSIDIAN);

                    }
                }
            }
        }
    }


}

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

public class JackHammerEnchantment implements
        UpgradeableEnchantment, ConditionalEnchantment,
        ConfigurableEnchantment<JackHammerEnchantment.Config> {
    public static final String NAME = "pickaxe_jack_hammer";

    public static class Config {
        final String displayName = "&cJack Hammer";
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
    public boolean testCondition(EnchantmentUseContext context) {
        double chance = config.chance.getChance(context.toolMetadata().getEnchantmentLevel(this));
        boolean b = RandomUtils.nextDouble() <= chance;
        System.out.println("testing condition: " + b);
        return b;
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
    public void apply(EnchantmentUseContext context) {
        System.out.println("applying");
        BlockToolUseContext ctx = (BlockToolUseContext) context;
        Cuboid cuboid = ctx.mine().config().cuboid();
        int y = ctx.event().getBlock().getY();
        World world = ctx.event().getBlock().getWorld();
        for (BlockPos pos : cuboid.volumePos()) {
            if (pos.y() == y) {
                Block block = world.getBlockAt(pos.x(), y, pos.z());
                block.setType(Material.AIR, false);
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

package com.vanquil.prison.tools.tool.enchantment.impl.pickaxe;

import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.BlockToolUseContext;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.util.PriceConfig;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneEnchantment
        implements UpgradeableEnchantment, ConfigurableEnchantment<FortuneEnchantment.Config> {
    public static final String NAME = "pickaxe_fortune";

    public static class Config {
        final int maxLevel = 100;
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
    public void apply(EnchantmentUseContext context) {
        BlockToolUseContext ctx = (BlockToolUseContext) context;
        BlockBreakEvent event = ctx.event();
        Material type = event.getBlock().getType();

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack itemInHand = event.getPlayer().getItemInHand();
        if (itemInHand != null
                && itemInHand.getType() != Material.AIR) {
            if (ToolType.isOfType(itemInHand, type().materials())) {
                ToolMetadata metadata = Tools.metadata(itemInHand);
                BlockToolUseContext ctx = new BlockToolUseContext(itemInHand, event, event.getPlayer(), metadata);
                apply(ctx);
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

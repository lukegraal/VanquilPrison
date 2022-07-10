package com.vanquil.prison.tools.tool.enchantment.listener;

import com.vanquil.prison.tools.minimines.Mine;
import com.vanquil.prison.tools.minimines.Mines;
import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.PotionEffectEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.BlockToolUseContext;
import com.vanquil.prison.tools.util.dimension.BlockPos;
import com.vanquil.prison.tools.util.listeners.Listeners;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantmentListener
        implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Mine mine = Mines.getMineAtLocation(BlockPos.of(block.getX(), block.getY(), block.getZ()));
        if (mine == null) {
            event.setCancelled(true);
            return;
        }

        ItemStack itemInHand = event.getPlayer().getItemInHand();
        if (itemInHand != null && itemInHand.getType() != Material.AIR) {
            ToolMetadata metadata = Tools.metadata(itemInHand);
            if (metadata != null) {
                for (ToolEnchantment enchantment : EnchantmentRegistry.Enchantments) {
                    if (enchantment.type() == ToolType.PICKAXE
                            && !(enchantment instanceof PotionEffectEnchantment)
                            && metadata.hasEnchantment(enchantment)) {
                        Player player = event.getPlayer();
                        BlockToolUseContext ctx = new BlockToolUseContext(itemInHand, event, player, metadata, mine);
                        enchantment.apply(ctx);
                    }
                }
            }
        }
    }

    public static void register() {
        Listeners.registerListener(new EnchantmentListener());
    }
}

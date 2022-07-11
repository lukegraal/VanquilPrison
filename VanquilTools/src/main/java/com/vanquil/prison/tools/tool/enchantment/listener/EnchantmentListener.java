package com.vanquil.prison.tools.tool.enchantment.listener;

import com.vanquil.prison.tools.minimines.Mine;
import com.vanquil.prison.tools.minimines.Mines;
import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.PotionEffectEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.container.EnchantmentContainer;
import com.vanquil.prison.tools.tool.enchantment.context.BlockToolUseContext;
import com.vanquil.prison.tools.tool.enchantment.context.MineToolUseContext;
import com.vanquil.prison.tools.util.listeners.Listeners;
import com.vanquil.prison.tools.util.message.Titles;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EnchantmentListener
        implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        ItemStack itemInHand = event.getPlayer().getItemInHand();
        if (itemInHand != null && itemInHand.getType() != Material.AIR) {
            ToolMetadata metadata = Tools.metadata(itemInHand);
            if (metadata != null) {
                for (ToolEnchantment enchantment : EnchantmentRegistry.Enchantments) {
                    if (!(enchantment instanceof PotionEffectEnchantment)
                            && metadata.hasEnchantment(enchantment)) {
                        Player player = event.getPlayer();
                        Mine mine = Mines.getMineAtLocation(block.getLocation());
                        BlockToolUseContext ctx;
                        if (mine != null) {
                            ctx = new MineToolUseContext(itemInHand, event, player, metadata, mine);
                        } else {
                            ctx = new BlockToolUseContext(itemInHand, event, player, metadata);
                        }
                        enchantment.apply(ctx);

                        int drops = ctx.dropAmount();
                        PlayerInventory inventory = event.getPlayer().getInventory();
                        int lastAmount = drops % 64;
                        int stacks = (drops-lastAmount) / 64;
                        Material material = block.getType();
                        ItemStack[] items = new ItemStack[stacks+1];
                        for (int i = 0; i < stacks; i++)
                            items[i] = new ItemStack(material, 64);
                        items[stacks] = new ItemStack(material, lastAmount);
                        inventory.addItem(items);
                        if (inventory.firstEmpty() == -1) {
                            Titles.sendTitle(player, "&c&lInventory Full",
                                    null, 5, 40, 5);
                        }
                    }
                }

                event.getBlock().setType(Material.AIR, true);
            }
        }
    }

    @EventHandler
    public void onToolInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR
                || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() != Material.AIR) {
                ItemStack item = event.getItem();
                ToolMetadata metadata = Tools.metadata(item);
                if (metadata != null) {
                    Inventory inventory = new EnchantmentContainer(metadata, item).getInventory();
                    event.getPlayer().openInventory(inventory);
                }
            }
        }
    }

    public static void register() {
        Listeners.registerListener(new EnchantmentListener());
    }
}

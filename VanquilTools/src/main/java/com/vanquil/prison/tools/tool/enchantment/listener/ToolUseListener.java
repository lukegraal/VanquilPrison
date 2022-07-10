package com.vanquil.prison.tools.tool.enchantment.listener;

import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.Tools;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ToolUseListener
        implements Listener {
    @EventHandler
    public void onToolInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR
                || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() != Material.AIR) {
                ItemStack item = event.getItem();
                ToolMetadata metadata = Tools.metadata(item);
                if (metadata != null) {

                    event.setCancelled(true);
                }
            }
        }
    }
}

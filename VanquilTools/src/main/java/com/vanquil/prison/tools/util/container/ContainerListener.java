package com.vanquil.prison.tools.util.container;

import com.vanquil.prison.tools.util.listeners.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ContainerListener
        implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onContainerEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null && clickedInventory.getHolder() instanceof Container) {
            Container holder = (Container) clickedInventory.getHolder();
            ContainerClickable clickable = holder.getClickableMap().get(event.getSlot());
            Player player = (Player) event.getWhoClicked();
            if (clickable != null) {
                clickable.action().click(player, event.getClick());
                if (clickable.locked()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static void register() {
        Listeners.registerListener(new ContainerListener());
    }
}

package com.vanquil.prison.tools.util.container;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface ClickableAction {
    ClickableAction UNCLICKABLE = (player, type) -> {};

    void click(Player player, ClickType type);
}

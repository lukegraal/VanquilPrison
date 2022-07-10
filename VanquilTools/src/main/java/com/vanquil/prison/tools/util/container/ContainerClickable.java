package com.vanquil.prison.tools.util.container;

import org.bukkit.inventory.ItemStack;

public class ContainerClickable {
    private final ItemStack item;
    private final ClickableAction action;
    private final boolean locked;

    public ContainerClickable(ItemStack item, ClickableAction action) {
        this(item, action, true);
    }

    public ContainerClickable(ItemStack item, ClickableAction action, boolean locked) {
        this.item = item;
        this.action = action;
        this.locked = locked;
    }

    public ItemStack item() {
        return item;
    }

    public ClickableAction action() {
        return action;
    }

    public boolean locked() {
        return locked;
    }
}

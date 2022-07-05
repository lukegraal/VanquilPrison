package com.vanquil.prison.tools.tool.enchantment.ctx;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantmentUseContext {
    private final Player player;
    private final ItemStack item;

    public EnchantmentUseContext(ItemStack item, Player player) {
        this.player = player;
        this.item = item;
    }

    public ItemStack item() {
        return item;
    }

    public Player player() {
        return player;
    }
}

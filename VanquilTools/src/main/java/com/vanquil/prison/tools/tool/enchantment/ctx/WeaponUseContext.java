package com.vanquil.prison.tools.tool.enchantment.ctx;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponUseContext extends EnchantmentUseContext {
    private final Player target;

    public WeaponUseContext(ItemStack item, Player player, Player target) {
        super(item, player);
        this.target = target;
    }

    public Player target() {
        return target;
    }
}

package com.vanquil.prison.tools.tool.enchantment.context;

import com.vanquil.prison.tools.tool.ToolMetadata;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponUseContext extends EnchantmentUseContext {
    private final Player target;

    public WeaponUseContext(ItemStack item, Player player, Player target, ToolMetadata metadata) {
        super(item, player, metadata);
        this.target = target;
    }

    public Player target() {
        return target;
    }
}

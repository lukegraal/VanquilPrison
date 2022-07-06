package com.vanquil.prison.tools.tool.enchantment.context;

import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.Tools;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantmentUseContext {
    private final Player player;
    private final ItemStack item;
    private final ToolMetadata metadata;

    public EnchantmentUseContext(ItemStack item, Player player, ToolMetadata metadata) {
        this.player = player;
        this.item = item;
        this.metadata = metadata;
    }

    public ToolMetadata toolMetadata() {
        return metadata;
    }

    public ItemStack item() {
        return item;
    }

    public Player player() {
        return player;
    }
}

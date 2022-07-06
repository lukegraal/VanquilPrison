package com.vanquil.prison.tools.tool.enchantment.context;

import com.vanquil.prison.tools.tool.ToolMetadata;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockToolUseContext extends EnchantmentUseContext {
    private final BlockBreakEvent event;

    public BlockToolUseContext(ItemStack item, BlockBreakEvent event, Player player, ToolMetadata metadata) {
        super(item, player, metadata);
        this.event = event;
    }

    public BlockBreakEvent event() {
        return event;
    }
}

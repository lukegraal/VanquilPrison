package com.vanquil.prison.tools.tool.enchantment.context;

import com.vanquil.prison.tools.minimines.Mine;
import com.vanquil.prison.tools.tool.ToolMetadata;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockToolUseContext extends EnchantmentUseContext {
    private final Mine mine;
    private final BlockBreakEvent event;

    public BlockToolUseContext(ItemStack item, BlockBreakEvent event, Player player, ToolMetadata metadata, Mine mine) {
        super(item, player, metadata);
        this.mine = mine;
        this.event = event;
    }

    public BlockBreakEvent event() {
        return event;
    }

    public Mine mine() {
        return mine;
    }
}

package com.vanquil.prison.tools.tool.enchantment.context;

import com.vanquil.prison.tools.minimines.Mine;
import com.vanquil.prison.tools.tool.ToolMetadata;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public final class MineToolUseContext
        extends BlockToolUseContext {
    private final Mine mine;

    public MineToolUseContext(ItemStack item, BlockBreakEvent event, Player player, ToolMetadata metadata, Mine mine) {
        super(item, event, player, metadata);
        this.mine = mine;
    }

    public Mine mine() {
        return mine;
    }
}

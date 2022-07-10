package com.vanquil.prison.tools.tool.enchantment;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import org.bukkit.event.Listener;

public interface ToolEnchantment extends Listener {
    ToolType type();

    String uniqueName();

    String displayName();

    void apply(EnchantmentUseContext context);
}

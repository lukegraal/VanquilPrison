package com.vanquil.prison.tools.tool.enchantment.impl.axe;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ctx.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import org.bukkit.entity.Player;

public class FortuneEnchantment implements ToolEnchantment {
    public static final String NAME = "axe_fortune";

    @Override
    public ToolType type() {
        return ToolType.AXE;
    }

    @Override
    public String uniqueName() {
        return NAME;
    }

    @Override
    public void apply(EnchantmentUseContext context) {
        Player player = context.player();

    }
}

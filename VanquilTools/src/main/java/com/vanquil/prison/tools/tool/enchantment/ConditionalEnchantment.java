package com.vanquil.prison.tools.tool.enchantment;

import com.vanquil.prison.tools.tool.enchantment.ctx.EnchantmentUseContext;

public interface ConditionalEnchantment {
    boolean proc(EnchantmentUseContext context);
}

package com.vanquil.prison.tools.tool.enchantment;

import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;

public interface ConditionalEnchantment {
    boolean testCondition(EnchantmentUseContext context);
}

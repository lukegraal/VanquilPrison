package com.vanquil.prison.tools.tool.enchantment;

public interface ConfigurableEnchantment<T> extends ToolEnchantment {
    T config();
    void updateConfig(T t);
}

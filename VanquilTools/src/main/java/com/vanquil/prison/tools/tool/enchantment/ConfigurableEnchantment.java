package com.vanquil.prison.tools.tool.enchantment;

public interface ConfigurableEnchantment<T> {
    T config();
    void updateConfig(T t);
}

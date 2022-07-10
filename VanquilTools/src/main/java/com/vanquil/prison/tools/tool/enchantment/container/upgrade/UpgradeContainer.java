package com.vanquil.prison.tools.tool.enchantment.container.upgrade;

import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.util.container.Container;

public class UpgradeContainer
        extends Container {

    public UpgradeContainer(ToolEnchantment enchantment, ToolMetadata metadata) {
        if (enchantment instanceof UpgradeableEnchantment) {
            UpgradeableEnchantment ench = (UpgradeableEnchantment) enchantment;

        }
    }
}

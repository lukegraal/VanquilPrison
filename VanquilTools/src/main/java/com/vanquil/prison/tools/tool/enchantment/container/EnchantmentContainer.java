package com.vanquil.prison.tools.tool.enchantment.container;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.container.upgrade.UpgradeContainer;
import com.vanquil.prison.tools.tool.enchantment.container.upgrade.UpgradeContainerConfig;
import com.vanquil.prison.tools.util.C;
import com.vanquil.prison.tools.util.RomanNumeral;
import com.vanquil.prison.tools.util.container.Container;
import com.vanquil.prison.tools.util.container.config.ContainerItemConfig;
import com.vanquil.prison.tools.util.material.ItemBuilder;
import com.vanquil.prison.tools.util.material.Material2;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EnchantmentContainer
        extends Container {
    public static class Config {
        final UpgradeContainerConfig.EnchantmentList enchantmentList = new UpgradeContainerConfig.EnchantmentList();
        final UpgradeContainerConfig.ModifyLevel modifyLevel = new UpgradeContainerConfig.ModifyLevel();

        public UpgradeContainerConfig.ModifyLevel modifyLevel() {
            return modifyLevel;
        }
    }

    public EnchantmentContainer(ToolMetadata metadata, ItemStack itemStack) {
        closeBehaviour(CloseBehaviour.CLOSE_COMPLETELY);
        Config config = EnchantmentRegistry.ContainerConfig.instance();
        setTitle(config.enchantmentList.title());
        List<Integer> enchSlots = Lists.newArrayList();
        for (int i = 0; i < config.enchantmentList.format().size(); i++) {
            String line = config.enchantmentList.format().get(i);
            for (int j = 0; j < line.toCharArray().length; j++) {
                char c = line.charAt(j);
                if (c == config.enchantmentList.enchantmentSlotChar()) {
                    enchSlots.add((i*9+j));
                } else if (c == config.enchantmentList.closeCharacter()) {
                    ContainerItemConfig conf = config.enchantmentList.itemMap().get(Character.toString(c));
                    if (conf != null) {
                        ItemStack stack = ItemBuilder.of(conf.material(), conf.amount())
                                .name(conf.displayName())
                                .lore(conf.lore())
                                .build();
                        setItem(i, j, stack, (player, type) -> player.closeInventory());
                    }
                }
            }
        }

        int i = 0;
        for (ToolEnchantment enchantment : EnchantmentRegistry.enchantmentsFor(metadata.toolType())) {
            int slot = enchSlots.get(i);
            int level;
            ItemBuilder builder = ItemBuilder.of(Material2.ENCHANTED_BOOK, 1)
                    .name(enchantment.displayName())
                    .wrappedLore(enchantment.description(), 40)
                    .addLore("&r");
            if (metadata.hasEnchantment(enchantment)) {
                if (enchantment instanceof UpgradeableEnchantment)
                    level = metadata.getEnchantmentLevel(((UpgradeableEnchantment) enchantment));
                else level = 1;
                ItemStack stack = builder
                        .addLore(C.format(config.enchantmentList.currentLevelFormat(), RomanNumeral.toRomanNumeral(level)))
                        .addLore(config.enchantmentList.enchantmentUpgrade())
                        .build();
                setItem(slot, stack, (player, type) -> {
                    Inventory inventory = new UpgradeContainer(0, enchantment, metadata, itemStack).getInventory();
                    player.openInventory(inventory);
                });
            } else {
                ItemStack stack = builder
                        .addLore(C.format(config.enchantmentList.currentLevelFormat(), "None"))
                        .addLore(config.enchantmentList.enchantmentUpgrade())
                        .build();
                setItem(slot, stack, (player, type) -> {
                    Inventory inventory = new UpgradeContainer(0, enchantment, metadata, itemStack).getInventory();
                    player.openInventory(inventory);
                });
            }
            i++;
        }
    }
}

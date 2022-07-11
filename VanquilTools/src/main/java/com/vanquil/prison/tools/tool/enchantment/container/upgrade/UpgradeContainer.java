package com.vanquil.prison.tools.tool.enchantment.container.upgrade;

import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.container.EnchantmentContainer;
import com.vanquil.prison.tools.util.C;
import com.vanquil.prison.tools.util.container.Container;
import com.vanquil.prison.tools.util.material.ItemBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class UpgradeContainer
        extends Container {
    public UpgradeContainer(int modifier, ToolEnchantment enchantment, ToolMetadata metadata, ItemStack itemStack) {
        EnchantmentContainer.Config config = EnchantmentRegistry.ContainerConfig.instance();
        setTitle(config.modifyLevel().title());
        if (enchantment instanceof UpgradeableEnchantment) {
            UpgradeableEnchantment ench = (UpgradeableEnchantment) enchantment;
            int maxLevel = ench.maxLevel();
            int currentLevel = metadata.getEnchantmentLevel(ench);
            int newLevel = currentLevel + modifier;

            List<String> format = config.modifyLevel().format();
            int rows = config.modifyLevel().format().size();
            if (rows < 3) setSize(3*9);
            else if (rows > 6) setSize(6*9);
            else setSize(rows*9);

            for (int i = 0; i < format.size(); i++) {
                String line = format.get(i);
                for (int j = 0; j < line.toCharArray().length; j++) {
                    char c = line.charAt(j);
                    Map<String, UpgradeContainerConfig.UpgradeButtonConfig> itemMap = config.modifyLevel().itemMap2();
                    UpgradeContainerConfig.UpgradeButtonConfig item = itemMap.get(Character.toString(c));
                    ItemBuilder builder = ItemBuilder.of(item.material(), item.amount())
                            .name(item.displayName())
                            .lore(item.lore());
                    if (c == config.modifyLevel().confirmCharacter()) {
                        UpgradeContainerConfig.UpgradeButtonConfig c1 = itemMap.get(String.valueOf(config.modifyLevel().confirmCharacter()));
                        builder.lore();
                        for (String s : c1.lore()) {
                            builder.addLore(C.format(s, ench.displayName(), newLevel,
                                    ench.priceToUpgrade(currentLevel+modifier)));
                        }
                    }
                    ItemStack stack = builder.build();
                    setItem((i*9+j), stack, (player, type) -> {
                        if (item.modifier != 0) {
                            if ((currentLevel + item.modifier) > maxLevel) {
                                C.send(player, "&cYou can't go above the maximum level of this enchantment!");
                                return;
                            }
                            if ((currentLevel + item.modifier) < currentLevel) {
                                C.send(player, "&cYou can't go under the current level of this enchantment!");
                                return;
                            }
                            System.out.println("opened new container");
                            new UpgradeContainer((modifier + item.modifier), ench, metadata, itemStack);
                        } else {
                            if (c == config.modifyLevel().closeCharacter()) {
                                player.closeInventory();
                            } else if (c == config.modifyLevel().confirmCharacter()) {
                                // todo: currency integration
                                ItemStack itemStack1 = Tools.updateMetadata(itemStack, m -> {
                                    m.setEnchantmentLevel(ench, currentLevel + modifier);
                                    return m;
                                });
                                player.setItemInHand(itemStack1);
                                Inventory inv = new EnchantmentContainer(Tools.metadata(itemStack1), itemStack).getInventory();
                                player.openInventory(inv);
                                C.send(player, "&aUpgraded!");
                            }
                        }
                    });
                }
            }
        }
    }
}

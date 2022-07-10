package com.vanquil.prison.tools.tool.enchantment.container;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.util.C;
import com.vanquil.prison.tools.util.RomanNumeral;
import com.vanquil.prison.tools.util.container.Container;
import com.vanquil.prison.tools.util.container.config.ContainerConfig;
import com.vanquil.prison.tools.util.container.config.ContainerItemConfig;
import com.vanquil.prison.tools.util.material.ItemBuilder;
import com.vanquil.prison.tools.util.material.Material2;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class EnchantmentContainer
        extends Container {
    public static class Config
            extends ContainerConfig {
        final String currentLevelFormat = "&7Current level: &b{0}";
        final String enchantmentUpgrade = "Click to upgrade enchantment";
        final char enchantmentSlots = '@';

        {
            List<String> f = Lists.newArrayList();
            f.add("#########");
            f.add("#@@@@@@@#");
            f.add("#@@@@@@@#");
            f.add("#@@@@@@@#");
            f.add("#########");
            f.add("%%%%X%%%%");
            format = f;

            itemMap.put("#", new ContainerItemConfig() {{
                displayName = "";
                lore = Collections.emptyList();
                amount = 1;
                material = Material2.CYAN_STAINED_GLASS;
            }});
            itemMap.put("X", new ContainerItemConfig() {{
                displayName = "&cGo back or exit";
                material = Material2.BARRIER;
                amount = 1;
            }});
        }
    }

    public EnchantmentContainer(ToolMetadata metadata) {
        closeBehaviour(CloseBehaviour.CLOSE_COMPLETELY);
        
        Config config = EnchantmentRegistry.ContainerConfig.instance();
        List<Integer> enchSlots = Lists.newArrayList();
        for (int i = 0; i < config.format().size(); i++) {
            int row = i+1;
            String line = config.format().get(i);
            for (int j = 0; j < line.toCharArray().length; j++) {
                int col = j+1;
                char c = line.charAt(j);
                if (c == config.enchantmentSlots) {
                    enchSlots.add((row*col)-1);
                } else if (c == config.closeCharacter()) {
                    ContainerItemConfig conf = config.itemMap().get(Character.toString(c));
                    if (conf != null) {
                        ItemStack stack = ItemBuilder.of(conf.material(), conf.amount())
                                .name(conf.displayName())
                                .lore(conf.lore())
                                .build();
                        setItem(row, col, stack, (player, type) -> player.closeInventory());
                    }
                }
            }
        }

        int i = 0;
        for (ToolEnchantment enchantment : EnchantmentRegistry.enchantmentsFor(metadata.toolType())) {
            ++i;
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
                        .addLore(C.format(config.currentLevelFormat, RomanNumeral.toRomanNumeral(level)))
                        .addLore(config.enchantmentUpgrade)
                        .build();
                setItem(slot, stack, (player, type) -> /*todo*/ {});
            } else {
                ItemStack stack = builder
                        .addLore(C.format(config.currentLevelFormat, "None"))
                        .addLore(config.enchantmentUpgrade)
                        .build();
                setItem(slot, stack, (player, type) -> /*todo*/ {});
            }
        }
    }
}

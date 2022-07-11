package com.vanquil.prison.tools.tool.enchantment.container.upgrade;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vanquil.prison.tools.util.container.config.ContainerConfig;
import com.vanquil.prison.tools.util.container.config.ContainerItemConfig;
import com.vanquil.prison.tools.util.material.Material2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UpgradeContainerConfig {
    public static class EnchantmentList
            extends ContainerConfig {
        final String currentLevelFormat = "&7Current level: &b{0}";
        final String enchantmentUpgrade = "Click to upgrade enchantment";
        final char enchantmentSlotsChar = '@';

        public EnchantmentList() {
            List<String> f = Lists.newArrayList();
            f.add("#########");
            f.add("#@@@@@@@#");
            f.add("#@@@@@@@#");
            f.add("#@@@@@@@#");
            f.add("#########");
            f.add("%%%%X%%%%");
            format = f;

            title = "Upgrade Tool";
            itemMap = Maps.newHashMap();
            itemMap.put("#", new ContainerItemConfig(1, Material2.CYAN_STAINED_GLASS_PANE, "&r", Collections.emptyList()));
            itemMap.put("X", new ContainerItemConfig(1, Material2.BARRIER, "&cGo back or exit", Collections.emptyList()));
        }

        public String currentLevelFormat() {
            return currentLevelFormat;
        }

        public String enchantmentUpgrade() {
            return enchantmentUpgrade;
        }

        public char enchantmentSlotChar() {
            return enchantmentSlotsChar;
        }
    }

    public static class UpgradeButtonConfig
            extends ContainerItemConfig {
        int modifier;

        public UpgradeButtonConfig(int amount, Material2 material, String displayName, List<String> lore, int modifier) {
            super(amount, material, displayName, lore);
            this.modifier = modifier;
        }

        public UpgradeButtonConfig() {
        }
    }

    public static class ModifyLevel
            extends ContainerConfig {
        final char confirmCharacter = '@';
        final Map<String, UpgradeButtonConfig> itemMap2;

        public ModifyLevel() {
            title = "Modify Transaction";
            closeCharacter = 'X';
            List<String> f = Lists.newArrayList();
            f.add("#########");
            f.add("#ABC@EFG#");
            f.add("####X####");
            format = f;

            itemMap2 = Maps.newHashMap();
            itemMap2.put(String.valueOf(closeCharacter), new UpgradeButtonConfig(1, Material2.BARRIER, "&cCancel transaction", Collections.emptyList(), 0));
            itemMap2.put("#", new UpgradeButtonConfig(1, Material2.CYAN_STAINED_GLASS_PANE, "&r", Collections.emptyList(), 0));
            itemMap2.put("A", new UpgradeButtonConfig(1, Material2.RED_STAINED_GLASS_PANE, "&c-100 levels", Collections.emptyList(), -100));
            itemMap2.put("B", new UpgradeButtonConfig(1, Material2.RED_STAINED_GLASS_PANE, "&c-10 levels", Collections.emptyList(), -10));
            itemMap2.put("C", new UpgradeButtonConfig(1, Material2.RED_STAINED_GLASS_PANE, "&c-1 level", Collections.emptyList(), -1));
            itemMap2.put("E", new UpgradeButtonConfig(1, Material2.LIME_STAINED_GLASS_PANE, "&a+1 levels", Collections.emptyList(), 1));
            itemMap2.put("F", new UpgradeButtonConfig(1, Material2.LIME_STAINED_GLASS_PANE, "&a+10 levels", Collections.emptyList(), 10));
            itemMap2.put("G", new UpgradeButtonConfig(1, Material2.LIME_STAINED_GLASS_PANE, "&a+100 levels", Collections.emptyList(), 100));
            itemMap2.put("@", new UpgradeButtonConfig(1, Material2.NAME_TAG, "&d&lConfirm Transaction",
                    Lists.newArrayList("&7Enchantment: {0}", "&7New level: &a{1}", "&7Price: {2}"), 0));
        }

        public char confirmCharacter() {
            return confirmCharacter;
        }

        public Map<String, UpgradeButtonConfig> itemMap2() {
            return itemMap2;
        }
    }
}

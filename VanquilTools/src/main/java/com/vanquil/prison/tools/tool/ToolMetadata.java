package com.vanquil.prison.tools.tool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import com.vanquil.prison.tools.VanquilTools;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class ToolMetadata {
    public static final String NBT_KEY = "tool_metadata";
    @SerializedName("type")
    private final ToolType toolType;
    @SerializedName("upgradeable_enchantments")
    private final Map<String, Integer> upgradeableEnchantments = Maps.newHashMap();
    @SerializedName("custom_enchantments")
    private final List<String> customEnchantments = Lists.newArrayList();

    public ToolMetadata(ToolType toolType) {
        this.toolType = toolType;
    }

    public ToolType toolType() {
        return toolType;
    }

    public static ToolMetadata get(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        String string = stack.getTag().getString(NBT_KEY);
        return VanquilTools.GSON.fromJson(string, ToolMetadata.class);
    }

    public void addEnchantment(ToolEnchantment enchantment) {
        if (enchantment instanceof UpgradeableEnchantment) {
            String key = enchantment.uniqueName();
            int level = upgradeableEnchantments.getOrDefault(key, 0);
            upgradeableEnchantments.put(key, level+1);
        } else {
            customEnchantments.add(enchantment.uniqueName());
        }
    }

    public void setEnchantmentLevel(UpgradeableEnchantment enchantment, int level) {
        upgradeableEnchantments.put(enchantment.uniqueName(), level);
    }

    public int getEnchantmentLevel(UpgradeableEnchantment enchantment) {
        String s = enchantment.uniqueName();
        Integer i = upgradeableEnchantments.get(s);
        if (i == null) i = 0;
        return i;
    }

    public boolean hasEnchantment(ToolEnchantment enchantment) {
        if (enchantment instanceof UpgradeableEnchantment) {
            return upgradeableEnchantments.get(enchantment.uniqueName()) != null;
        } else {
            return customEnchantments.contains(enchantment.uniqueName());
        }
    }

    public Map<String, Integer> getUpgradeableEnchantments() {
        return upgradeableEnchantments;
    }

    public List<String> getCustomEnchantments() {
        return customEnchantments;
    }
}

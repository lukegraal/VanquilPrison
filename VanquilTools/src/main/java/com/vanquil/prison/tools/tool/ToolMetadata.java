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
    @SerializedName("upgradeable_enchantments")
    private final Map<String, Integer> upgradeableEnchantments = Maps.newHashMap();
    @SerializedName("vanilla_enchantments")
    private final Map<String, Integer> vanillaEnchantments = Maps.newHashMap();
    @SerializedName("custom_enchantments")
    private final List<String> customEnchantments = Lists.newArrayList();

    public ToolMetadata() {
    }

    public static ToolMetadata get(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        String string = stack.getTag().getString(NBT_KEY);
        return VanquilTools.GSON.fromJson(string, ToolMetadata.class);
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

    public boolean hasVanillaEnchantment(Enchantment enchantment) {
        return vanillaEnchantments.containsKey(enchantment.getName());
    }

    public Map<String, Integer> getVanillaEnchantments() {
        return vanillaEnchantments;
    }

    public Map<String, Integer> getUpgradeableEnchantments() {
        return upgradeableEnchantments;
    }

    public List<String> getCustomEnchantments() {
        return customEnchantments;
    }
}

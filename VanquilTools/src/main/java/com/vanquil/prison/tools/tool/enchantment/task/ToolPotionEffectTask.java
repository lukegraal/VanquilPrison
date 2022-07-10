package com.vanquil.prison.tools.tool.enchantment.task;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.PotionEffectEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class ToolPotionEffectTask implements Runnable {
    private static final Set<PotionEffectEnchantment> ENCHANTMENTS = EnchantmentRegistry.PotionEffectEnchantments;
    private static final Map<ToolType, Set<PotionEffectEnchantment>> SET_MAP = Maps.newHashMap();

    static {
        for (PotionEffectEnchantment enchantment : ENCHANTMENTS) {
            Set<PotionEffectEnchantment> set = SET_MAP.get(enchantment.type());
            if (set != null) set.add(enchantment);
            else SET_MAP.put(enchantment.type(), Sets.newHashSet(enchantment));
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand != null
                    && itemInHand.getType() != Material.AIR) {
                ToolMetadata metadata = Tools.metadata(itemInHand);
                if (metadata != null) {
                    for (Map.Entry<ToolType, Set<PotionEffectEnchantment>> entry : SET_MAP.entrySet()) {
                        ToolType type = entry.getKey();
                        if (ToolType.isOfType(itemInHand, type.materials())) {
                            for (PotionEffectEnchantment e : entry.getValue()) {
                                Integer level = metadata.getUpgradeableEnchantments().get(e.uniqueName());
                                if (level != null && level > 0) {
                                    EnchantmentUseContext c = new EnchantmentUseContext(itemInHand, player, metadata);
                                    e.apply(c);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

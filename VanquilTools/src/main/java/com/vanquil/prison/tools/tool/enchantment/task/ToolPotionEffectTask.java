package com.vanquil.prison.tools.tool.enchantment.task;

import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.impl.axe.SpeedEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ToolPotionEffectTask implements Runnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand == null
                    || itemInHand.getType() == Material.AIR) {
                ToolMetadata metadata = Tools.metadata(itemInHand);
                if (metadata != null) {
                    SpeedEnchantment enchantment = EnchantmentRegistry.AxeSpeedEnchantment;
                    if (ToolType.isOfType(itemInHand, enchantment.type().materials())) {
                        int level = metadata.getEnchantmentLevel(enchantment);
                        if (level > 0) {
                            EnchantmentUseContext ctx = new EnchantmentUseContext(itemInHand, player, metadata);
                            enchantment.effect(ctx);
                        }
                    }
                }
            }
        }
    }
}

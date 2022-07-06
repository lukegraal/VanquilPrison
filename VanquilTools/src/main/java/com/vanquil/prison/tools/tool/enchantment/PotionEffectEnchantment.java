package com.vanquil.prison.tools.tool.enchantment;

import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public interface PotionEffectEnchantment extends ToolEnchantment {
    PotionEffect effect(EnchantmentUseContext context);

    @Override
    default void apply(EnchantmentUseContext context) {
        Player player = context.player();
        PotionEffect effect = effect(context);
        player.addPotionEffect(effect);
    }
}

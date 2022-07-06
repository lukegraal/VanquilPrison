package com.vanquil.prison.tools.tool.enchantment;

import com.google.common.collect.Sets;
import com.vanquil.prison.tools.tool.enchantment.impl.axe.JumpBoostEnchantment;
import com.vanquil.prison.tools.tool.enchantment.impl.axe.SpeedEnchantment;

import java.util.Set;

public class EnchantmentRegistry {
    public static final SpeedEnchantment AxeSpeedEnchantment = new SpeedEnchantment();
    public static final JumpBoostEnchantment AxeJumpBoostEnchantment = new JumpBoostEnchantment();
    public static final Set<ToolEnchantment> Enchantments = Sets.newHashSet();

    static {
        register(AxeSpeedEnchantment);
        register(AxeJumpBoostEnchantment);
    }

    public EnchantmentRegistry() {

    }

    public static void register(ToolEnchantment enchantment) {
        Enchantments.add(enchantment);
    }

    public static Set<ToolEnchantment> potionEffectEnchantments() {
        Set<ToolEnchantment> set = Sets.newHashSet();
        for (ToolEnchantment enchantment : Enchantments) {
            if (enchantment instanceof PotionEffectEnchantment) {
                set.add(enchantment);
            }
        }
        return set;
    }
}

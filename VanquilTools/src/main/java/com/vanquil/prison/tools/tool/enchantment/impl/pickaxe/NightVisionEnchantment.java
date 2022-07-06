package com.vanquil.prison.tools.tool.enchantment.impl.pickaxe;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.PotionEffectEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class NightVisionEnchantment implements PotionEffectEnchantment {
    public static final String NAME = "pickaxe_night_vision";

    @Override
    public PotionEffect effect(EnchantmentUseContext context) {
        int level = context.toolMetadata().getEnchantmentLevel(this);
        if (level > 0) {
            return new PotionEffect(PotionEffectType.NIGHT_VISION, 10, 1, false, false);
        } else {
            return null;
        }
    }

    @Override
    public ToolType type() {
        return ToolType.PICKAXE;
    }

    @Override
    public String uniqueName() {
        return null;
    }

    @Override
    public long priceToUpgrade(int level) {
        return 0;
    }

    @Override
    public int maxLevel() {
        return 0;
    }
}

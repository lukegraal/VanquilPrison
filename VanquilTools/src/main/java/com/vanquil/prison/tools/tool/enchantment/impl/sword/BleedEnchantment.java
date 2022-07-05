package com.vanquil.prison.tools.tool.enchantment.impl.sword;

import com.google.common.collect.Maps;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.ConditionalEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ctx.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.ctx.WeaponUseContext;
import com.vanquil.prison.tools.tool.enchantment.storable.ToolMetadata;
import com.vanquil.prison.tools.util.C;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BleedEnchantment
        implements ConditionalEnchantment, UpgradeableEnchantment, ConfigurableEnchantment<BleedEnchantment.Config> {

    private static final String NAME = "bleed";

    public static class Config {
        final String displayName = "Bleed",
                     description = "Gives you a chance to inflict wither effects on your opponent",
                     attackerMessage = "You gave %s a wither effect for 5 seconds!",
                     targetMessage = "%s's sword gave you the wither effect for 5 seconds!";
        final int maxLevel = 3,
                  effectDurationSeconds = 5,
                  effectAmplifier = 1;
        final Map<Integer, Long> tokenPriceMap = Maps.newHashMap();
        final Map<Integer, Float> chanceMap = Maps.newHashMap();

        public Config() {
            chanceMap.put(1, 0.01f);
            chanceMap.put(2, 0.02f);
            chanceMap.put(3, 0.03f);
            tokenPriceMap.put(1, 1_000_000L);
            tokenPriceMap.put(2, 2_000_000L);
            tokenPriceMap.put(3, 3_000_000L);
        }

        public String displayName() {
            return displayName;
        }

        public String description() {
            return description;
        }
    }

    private Config config = new Config();

    @Override
    public Config config() {
        return config;
    }

    @Override
    public void updateConfig(Config config) {
        this.config = config;
    }

    @Override
    public boolean proc(EnchantmentUseContext context) {
        ToolMetadata metadata = ToolMetadata.get(context.item());
        Integer i = metadata.getUpgradeableEnchantments().get(uniqueName());
        if (i != null) {
            float chance = config.chanceMap.get(i);
            float val = ThreadLocalRandom.current().nextFloat(1.0f);
            return val <= chance;
        }
        return false;
    }

    @Override
    public ToolType type() {
        return ToolType.SWORD;
    }

    @Override
    public String uniqueName() {
        return NAME;
    }

    @Override
    public void apply(EnchantmentUseContext context) {
        WeaponUseContext ctx = (WeaponUseContext) context;
        Player target = ctx.target();
        applyEffect(ctx.player(), target);
    }

    @Override
    public long priceToUpgrade(int level) {
        return config.tokenPriceMap.get(level);
    }

    @Override
    public int maxLevel() {
        return config.maxLevel;
    }

    private void applyEffect(Player attacker, Player target) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, config.effectDurationSeconds, config.effectAmplifier));
        C.send(target, config.targetMessage, attacker.getName());
        C.send(target, config.attackerMessage, target.getName());
    }
}

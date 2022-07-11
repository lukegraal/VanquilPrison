package com.vanquil.prison.tools.tool.enchantment;

import com.google.common.collect.Sets;
import com.vanquil.prison.tools.VanquilTools;
import com.vanquil.prison.tools.config.Config;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.config.CustomEnchantmentConfig;
import com.vanquil.prison.tools.tool.enchantment.container.EnchantmentContainer;
import com.vanquil.prison.tools.tool.enchantment.impl.CommandEnchantment;
import com.vanquil.prison.tools.tool.enchantment.impl.axe.EfficiencyEnchantment;
import com.vanquil.prison.tools.tool.enchantment.impl.axe.FortuneEnchantment;
import com.vanquil.prison.tools.tool.enchantment.impl.axe.RadiusEnchantment;
import com.vanquil.prison.tools.tool.enchantment.listener.EnchantmentListener;

import javax.annotation.Nullable;
import java.util.Set;

public class EnchantmentRegistry {
    public static final ToolEnchantment EfficiencyEnchantment = new EfficiencyEnchantment();
    public static final ToolEnchantment FortuneEnchantment = new FortuneEnchantment();
    public static final ToolEnchantment RadiusEnchantment = new RadiusEnchantment();

    public static final Set<ToolEnchantment> Enchantments = Sets.newHashSet();
    public static final Set<PotionEffectEnchantment> PotionEffectEnchantments = Sets.newHashSet();

    public static final Config<EnchantmentContainer.Config> ContainerConfig = new Config<>(
            VanquilTools.basePath.resolve("enchantment_container.json"),
            EnchantmentContainer.Config.class, EnchantmentContainer.Config::new
    );
    public static final Config<CustomEnchantmentConfig> CustomEnchantmentsConfig = new Config<>(
            VanquilTools.basePath.resolve("custom_enchantments.json"),
            CustomEnchantmentConfig.class, CustomEnchantmentConfig::new
    );

    static {
        register(EfficiencyEnchantment);
        register(FortuneEnchantment);
        register(RadiusEnchantment);
        registerCustomEnchantments();
    }

    private static void registerCustomEnchantments() {
        CustomEnchantmentConfig instance = CustomEnchantmentsConfig.instance();
        for (CommandEnchantment.Config enchantment : instance.customEnchantments()) {
            if (findEnchantment(enchantment.getUniqueName()) != null) {
                System.out.println("Duplicate enchantment (same unique name): " + enchantment.getUniqueName());
            } else {
                CommandEnchantment ench = new CommandEnchantment(enchantment);
                Enchantments.add(ench);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void register(ToolEnchantment enchantment) {
        Enchantments.add(enchantment);
        if (enchantment instanceof ConfigurableEnchantment<?>) {
            ConfigurableEnchantment<Object> ench = (ConfigurableEnchantment<Object>) enchantment;
            Object config = ench.config();
            Config<Object> conf = new Config<>(
                    VanquilTools.basePath
                            .resolve("enchantments")
                            .resolve(ench.uniqueName() + ".json"),
                    (Class<Object>) config.getClass(), () -> config);
            ench.updateConfig(conf.instance());
        }
        if (enchantment instanceof PotionEffectEnchantment) {
            PotionEffectEnchantment ench = (PotionEffectEnchantment) enchantment;
            PotionEffectEnchantments.add(ench);
        }
    }

    public static @Nullable ToolEnchantment findEnchantment(String name) {
        for (ToolEnchantment enchantment : Enchantments) {
            if (enchantment.uniqueName().equalsIgnoreCase(name)) {
                return enchantment;
            }
        }
        return null;
    }

    public static void load() {
        EnchantmentListener.register();
    }

    public static Set<ToolEnchantment> enchantmentsFor(ToolType toolType) {
        Set<ToolEnchantment> set = Sets.newHashSet();
        for (ToolEnchantment enchantment : Enchantments) {
            if (enchantment.type() == toolType) {
                set.add(enchantment);
            }
        }
        return set;
    }
}

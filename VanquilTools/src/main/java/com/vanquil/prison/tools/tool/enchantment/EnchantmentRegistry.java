package com.vanquil.prison.tools.tool.enchantment;

import com.google.common.collect.Sets;
import com.vanquil.prison.tools.VanquilTools;
import com.vanquil.prison.tools.config.Config;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.container.EnchantmentContainer;
import com.vanquil.prison.tools.tool.enchantment.impl.pickaxe.*;
import com.vanquil.prison.tools.tool.enchantment.listener.EnchantmentListener;

import javax.annotation.Nullable;
import java.util.Set;

public class EnchantmentRegistry {
    public static final SpeedEnchantment PickaxeSpeedEnchantment = new SpeedEnchantment();
    public static final JumpBoostEnchantment PickaxeJumpBoostEnchantment = new JumpBoostEnchantment();
    public static final NightVisionEnchantment PickaxeNightVisionEnchantment = new NightVisionEnchantment();
    public static final FortuneEnchantment PickaxeFortuneEnchantment = new FortuneEnchantment();
    public static final JackHammerEnchantment PickaxeJackHammerEnchantment = new JackHammerEnchantment();
    public static final MultiDirectionalEnchantment PickaxeMultiDirectionalEnchantment = new MultiDirectionalEnchantment();
    public static final CombustiveEnchantment PickaxeCombustiveEnchantment = new CombustiveEnchantment();

    public static final Set<ToolEnchantment> Enchantments = Sets.newHashSet();
    public static final Set<PotionEffectEnchantment> PotionEffectEnchantments = Sets.newHashSet();

    public static final Config<EnchantmentContainer.Config> ContainerConfig = new Config<>(
            VanquilTools.basePath.resolve("enchantment_container.json"),
            EnchantmentContainer.Config.class, EnchantmentContainer.Config::new
    );

    static {
        register(PickaxeSpeedEnchantment);
        register(PickaxeJumpBoostEnchantment);
        register(PickaxeNightVisionEnchantment);
        register(PickaxeFortuneEnchantment);
        register(PickaxeJackHammerEnchantment);
        register(PickaxeMultiDirectionalEnchantment);
        register(PickaxeCombustiveEnchantment);
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

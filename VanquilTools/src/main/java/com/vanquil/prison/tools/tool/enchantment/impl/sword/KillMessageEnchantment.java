package com.vanquil.prison.tools.tool.enchantment.impl.sword;

import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.enchantment.ConfigurableEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.context.WeaponUseContext;
import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.util.C;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class KillMessageEnchantment implements ToolEnchantment,
        ConfigurableEnchantment<KillMessageEnchantment.Config> {
    public static final String NAME = "kill_message";

    public static class Config {
        transient final EnumeratedDistribution<String> distro;
        final Map<String, Double> deathMessages = new HashMap<String, Double>() {{
            put("{0} took a massive L while fighting {1}.", 0.2);
            put("{0} got scared while fighting {1}.", 0.2);
            put("{0} dropped his sword while running away from {1}.", 0.2);
            put("{0} forgot how to Minecraft upon seeing {1}.", 0.2);
            put("{0} gave up mid-fight with {1}.", 0.2);
        }};

        {
            distro = new EnumeratedDistribution<>(
                    deathMessages.entrySet().stream()
                            .map(e -> new Pair<>(e.getKey(), e.getValue()))
                            .collect(Collectors.toList())
            );
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
    public ToolType type() {
        return ToolType.SWORD;
    }

    @Override
    public String uniqueName() {
        return NAME;
    }

    @Override
    public String displayName() {
        return null;
    }

    @Override
    public void apply(EnchantmentUseContext context) {
        WeaponUseContext ctx = (WeaponUseContext) context;
        Player target = ctx.target(), killer = ctx.player();
        String sample = config.distro.sample();
        Bukkit.broadcastMessage(C.format(sample, target.getName(), killer.getName()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        ItemStack itemInHand = killer.getItemInHand();
        if (itemInHand != null && itemInHand.getType() != Material.AIR) {
            ToolMetadata toolMetadata = Tools.metadata(itemInHand);
            if (toolMetadata != null && toolMetadata.hasEnchantment(this)) {
                apply(new WeaponUseContext(itemInHand, killer, event.getEntity(), toolMetadata));
            }
        }
    }
}

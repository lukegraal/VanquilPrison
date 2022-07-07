package com.vanquil.prison.tools.tool.enchantment.impl.sword;

import com.google.common.collect.Maps;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.enchantment.context.EnchantmentUseContext;
import com.vanquil.prison.tools.tool.enchantment.ConditionalEnchantment;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.ToolMetadata;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

import static com.vanquil.prison.tools.VanquilTools.GSON;

public class ComboEnchantment implements ConditionalEnchantment, ToolEnchantment {
    private static final Map<UUID, Byte> COMBO_MAP = Maps.newHashMap();
    private static final Integer COMBO_PROC_AMOUNT = 10;
    public static final String NAME = "weapon_combo";

    @Override
    public boolean testCondition(EnchantmentUseContext context) {
        return (COMBO_MAP.get(context.player().getUniqueId()) >= COMBO_PROC_AMOUNT);
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

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHitPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player
                && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager(),
                    damaged = (Player) event.getEntity();
            ItemStack itemInHand = damager.getItemInHand();
            if (itemInHand != null && itemInHand.getType() != Material.AIR) {
                if (ToolType.isOfType(itemInHand, type().materials())) {
                    if (hasEnchantment(itemInHand, this)) {
                        UUID damagerUuid = damager.getUniqueId();
                        Byte b = COMBO_MAP.get(damagerUuid);
                        if (b != null) {
                            if (b+1 == 10) {
                                COMBO_MAP.remove(damagerUuid);
                                provideStrength(damager);
                            }
                        } else {
                            COMBO_MAP.put(damagerUuid, (byte) 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMissPlayer(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR
                || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (ToolType.isOfType(event.getItem(), type().materials())) {
                if (hasEnchantment(event.getItem(), this)) {
                    COMBO_MAP.remove(event.getPlayer().getUniqueId());
                }
            }
        }
    }

    private static boolean hasEnchantment(ItemStack item, ToolEnchantment enchantment) {
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (stack.hasTag()) {
            NBTTagCompound tag = stack.getTag();
            String json = tag.getString("metadata");
            ToolMetadata metadata = GSON.fromJson(json, ToolMetadata.class);
            return metadata.hasEnchantment(enchantment);
        }
        return false;
    }

    private static void provideStrength(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, 2, true, false));
    }
}

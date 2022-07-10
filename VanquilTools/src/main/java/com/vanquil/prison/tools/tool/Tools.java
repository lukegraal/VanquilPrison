package com.vanquil.prison.tools.tool;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.vanquil.prison.tools.VanquilTools;
import com.vanquil.prison.tools.config.Config;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.util.C;
import com.vanquil.prison.tools.util.RomanNumeral;
import com.vanquil.prison.tools.util.material.ItemBuilder;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class Tools {
    private static final SecureRandom RANDOM_GENERATOR = new SecureRandom();
    private static final Cache<Long, ToolMetadata> METADATA_CACHE
            = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build();
    private static final String TOOL_ID_TAG = "vanquil_tool_id";
    private static final Config<ToolsConfig> CONFIG = new Config<>(
            VanquilTools.basePath.resolve("tools.json"),
            ToolsConfig.class, ToolsConfig::new
    );

    public static void load() {
    }

    public static Long toolId(ItemStack itemStack) {
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        if (stack.hasTag()) {
            NBTTagCompound tag = stack.getTag();
            long l = tag.getLong(TOOL_ID_TAG);
            if (l > 0)
                return l;
            return null;
        }
        return null;
    }

    public static boolean isTool(ItemStack itemStack) {
        return toolId(itemStack) != null;
    }

    public static ItemStack createNew(ToolType type) {
        ToolsConfig.ToolConfig toolConf = config().instance().getToolConf(type);
        if (toolConf != null) {
            ToolMetadata metadata = new ToolMetadata(type);
            ItemBuilder builder = ItemBuilder.of(toolConf.material)
                    .name(toolConf.displayName)
                    .unbreakable(true, true);
            applyNameAndDescription(type, builder);
            ItemStack stack = builder.build();
            net.minecraft.server.v1_8_R3.ItemStack s1 = CraftItemStack.asNMSCopy(stack);
            NBTTagCompound tag = s1.getTag() != null ? s1.getTag() : new NBTTagCompound();
            tag.setString(ToolMetadata.NBT_KEY, VanquilTools.GSON.toJson(metadata));
            long id = generateId();
            tag.setLong(TOOL_ID_TAG, id);
            s1.setTag(tag);
            METADATA_CACHE.put(id, metadata);
            return CraftItemStack.asBukkitCopy(s1);
        }
        return null;
    }

    public static ItemBuilder applyNameAndDescription(ToolType type, ItemBuilder builder) {
        ToolsConfig.ToolConfig conf = config().instance().getToolConf(type);
        return conf == null ? builder : builder.name(conf.displayName).wrappedLore(conf.description, 40);
    }

    public static List<String> getEnchantmentDisplay(ToolMetadata metadata) {
        List<String> enchantments = Lists.newArrayList();
        if (metadata != null) {
            for (String ench : metadata.getCustomEnchantments()) {
                ToolEnchantment enchantment = EnchantmentRegistry.findEnchantment(ench);
                if (enchantment != null)
                    enchantments.add(C.format(enchantment.displayName()));
            }
            for (Map.Entry<String, Integer> entry : metadata.getUpgradeableEnchantments().entrySet()) {
                String key = entry.getKey();
                int level = entry.getValue();
                ToolEnchantment enchantment = EnchantmentRegistry.findEnchantment(key);
                if (enchantment != null)
                    enchantments.add(C.format(enchantment.displayName() + "_"
                            + RomanNumeral.toRomanNumeral(level)));
            }
        }
        return enchantments;
    }

    public static ItemStack updateMetadata(ItemStack itemStack, Function<ToolMetadata, ToolMetadata> function) {
        ToolMetadata metadata = metadata(itemStack);
        if (metadata != null) {
            ToolMetadata newMetadata = function.apply(metadata);
            net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound tag = stack.getTag();
            long id = tag.getLong(TOOL_ID_TAG);
            tag.setString(ToolMetadata.NBT_KEY, VanquilTools.GSON.toJson(newMetadata));
            stack.setTag(tag);
            itemStack = CraftItemStack.asBukkitCopy(stack);
            METADATA_CACHE.put(id, newMetadata);
            updateDisplay(itemStack);
        }
        return itemStack;
    }

    public static ItemStack updateDisplay(ItemStack itemStack) {
        ToolMetadata metadata = metadata(itemStack);
        if (metadata != null) {
            List<String> enchantments = getEnchantmentDisplay(metadata);
            ItemBuilder builder = ItemBuilder.of(itemStack);
            applyNameAndDescription(metadata.toolType(), builder);
            if (enchantments.size() > 0) {
                for (String s : ItemBuilder.wrap("\n" + String.join("&7, &r", enchantments), 40)) {
                    String x = s.replace("\n", "")
                            .replace("\r", "")
                            .replace("_", " ");
                    builder.addLore(x);
                }
            }
            itemStack = builder.build();
        }
        return itemStack;
    }

    public static ToolMetadata metadata(ItemStack itemStack) {
        Long aLong = toolId(itemStack);
        if (aLong != null) {
            try {
                return METADATA_CACHE.get(aLong, () -> {
                    net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
                    String json = stack.getTag().getString(ToolMetadata.NBT_KEY);
                    return VanquilTools.GSON.fromJson(json, ToolMetadata.class);
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static long generateId() {
        return RANDOM_GENERATOR.nextLong() & Long.MAX_VALUE;
    }

    public static Config<ToolsConfig> config() {
        return CONFIG;
    }
}

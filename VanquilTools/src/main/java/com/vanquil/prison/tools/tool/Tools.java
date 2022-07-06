package com.vanquil.prison.tools.tool;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.vanquil.prison.tools.VanquilTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
public class Tools {
    private static final SecureRandom RANDOM_GENERATOR = new SecureRandom();
    private static final Cache<Long, ToolMetadata> METADATA_CACHE
            = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build();
    private static final String TOOL_ID_TAG = "vanquil_tool_id";

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
}

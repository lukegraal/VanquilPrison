package com.vanquil.prison.tools.util.material;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.util.C;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private final ItemStack base;
    private final ItemMeta meta;

    private ItemBuilder(ItemStack base) {
        this.base = base;
        this.meta = base.getItemMeta();
    }

    public static ItemBuilder of(ItemStack parent) {
        return new ItemBuilder(parent);
    }

    public static ItemBuilder of(Material2 material) {
        return of(material, 1);
    }

    public static ItemBuilder of(Material2 material, int amount) {
        return new ItemBuilder(material.toItemStack(amount));
    }

    public ItemBuilder name(String name) {
        String format = C.format(name);
        meta.setDisplayName(format);
        return this;
    }

    public ItemBuilder lore(String... lines) {
        List<String> list = Lists.newArrayList();
        for (String line : lines) {
            String l = C.format(line);
            list.add(l);
        }
        meta.setLore(list);
        return this;
    }

    public ItemBuilder lore(List<String> lines) {
        List<String> list = Lists.newArrayList();
        for (String line : lines) {
            String l = C.format(line);
            list.add(l);
        }
        meta.setLore(list);
        return this;
    }

    public ItemBuilder wrappedLore(String line, int length) {
        List<String> wrap = wrap(line, length);
        meta.setLore(wrap);
        return this;
    }

    public ItemBuilder addWrappedLore(String line, int length) {
        List<String> wrap = wrap(line, length);
        List<String> lore = meta.hasLore() ? Lists.newArrayList(meta.getLore()) : Lists.newArrayList();
        lore.addAll(wrap);
        return lore(lore);
    }

    public ItemBuilder addLore(List<String> lines) {
        List<String> lore = Lists.newArrayList(meta.getLore());
        lore.addAll(lines);
        return lore(lore);
    }

    public ItemBuilder addLore(String... lines) {
        return addLore(Lists.newArrayList(lines));
    }

    public static List<String> wrap(String line, int length) {
        String[] split = WordUtils.wrap(line, length).split("\n");
        List<String> list = Lists.newArrayList();
        String lastColor = "";
        for (String s : split) {
            String l = C.format(lastColor + s).replace("\r", "");
            String lc = ChatColor.getLastColors(C.format(s));
            if (!lc.equals("")) lastColor = lc;
            list.add(l);
        }
        return list;
    }

    public ItemBuilder unbreakable(boolean unbreakable, boolean hidden) {
        meta.spigot().setUnbreakable(unbreakable);
        if (hidden)
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder unsafeEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder safeEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, false);
        return this;
    }

    public ItemStack build() {
        base.setItemMeta(meta);
        return base;
    }
}

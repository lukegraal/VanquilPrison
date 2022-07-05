package com.vanquil.prison.tools.tool;

import org.bukkit.Material;

public enum ToolType {
    SWORD(
            Material.DIAMOND_SWORD,
            Material.GOLD_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.WOOD_SWORD
    ),
    AXE(
            Material.DIAMOND_AXE,
            Material.GOLD_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.WOOD_AXE
    ),
    SHOVEL(
            Material.DIAMOND_SPADE,
            Material.GOLD_SPADE,
            Material.IRON_SPADE,
            Material.STONE_SPADE,
            Material.WOOD_SPADE
    ),
    PICKAXE(
            Material.DIAMOND_PICKAXE,
            Material.GOLD_PICKAXE,
            Material.IRON_PICKAXE,
            Material.STONE_PICKAXE,
            Material.WOOD_PICKAXE
    );

    private final Material[] material;

    ToolType(Material... materials) {
        this.material = materials;
    }

    public Material[] materials() {
        return material;
    }
}
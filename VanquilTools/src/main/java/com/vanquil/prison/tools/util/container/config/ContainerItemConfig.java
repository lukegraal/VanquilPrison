package com.vanquil.prison.tools.util.container.config;

import com.vanquil.prison.tools.util.material.Material2;

import java.util.Collections;
import java.util.List;

public class ContainerItemConfig {
    protected int amount = 1;
    protected Material2 material;
    protected String displayName;
    protected List<String> lore = Collections.emptyList();

    public ContainerItemConfig(int amount, Material2 material, String displayName, List<String> lore) {
        this.amount = amount;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }

    public ContainerItemConfig() {
    }

    public int amount() {
        return amount;
    }

    public Material2 material() {
        return material;
    }

    public String displayName() {
        return displayName;
    }

    public List<String> lore() {
        return lore;
    }
}

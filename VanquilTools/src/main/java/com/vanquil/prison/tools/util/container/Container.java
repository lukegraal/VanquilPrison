package com.vanquil.prison.tools.util.container;

import com.google.common.collect.Maps;
import com.vanquil.prison.tools.util.container.config.ContainerConfig;
import com.vanquil.prison.tools.util.container.config.ContainerItemConfig;
import com.vanquil.prison.tools.util.material.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class Container implements InventoryHolder  {
    public enum CloseBehaviour {
        RETURN_TO_PARENT, CLOSE_COMPLETELY
    }

    private int size;
    private String title = "Container";
    private final Map<Integer, ContainerClickable> clickableMap;
    private Container parent = null;
    private CloseBehaviour closeBehaviour = CloseBehaviour.RETURN_TO_PARENT;

    public Container(int size) {
        this.size = size;
        this.clickableMap = Maps.newHashMap();
    }

    public Container(ContainerConfig config) {
        if (!config.verifyFormat())
            throw new IllegalArgumentException("Bad config :(");
        this.size = config.format().size() * 9;
        this.clickableMap = Maps.newHashMap();
        for (int i = 0; i < config.format().size(); i++) {
            String line = config.format().get(i);
            for (int j = 0; j < line.toCharArray().length; j++) {
                char c = line.charAt(j);
                ContainerItemConfig conf = config.itemMap().get(Character.toString(c));
                if (conf != null) {
                    ItemStack stack = ItemBuilder.of(conf.material(), conf.amount())
                            .name(conf.displayName())
                            .lore(conf.lore())
                            .build();
                    setItem(i, j, stack, (player, type) -> {
                        if (c == 'X') {
                            switch (closeBehaviour) {
                                case CLOSE_COMPLETELY: {
                                    player.closeInventory();
                                    break;
                                }
                                case RETURN_TO_PARENT: {
                                    if (parent != null) player.openInventory(parent.getInventory());
                                    break;
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Container() {
        this(9*6);
    }

    protected void closeBehaviour(CloseBehaviour closeBehaviour) {
        this.closeBehaviour = closeBehaviour;
    }

    Map<Integer, ContainerClickable> getClickableMap() {
        return clickableMap;
    }

    protected void setItem(int slot, ItemStack item) {
        clickableMap.put(slot, new ContainerClickable(item, ClickableAction.UNCLICKABLE));
    }

    protected void setItem(int row, int col, ItemStack item) {
        clickableMap.put((row*9+col), new ContainerClickable(item, ClickableAction.UNCLICKABLE));
    }

    protected void setItem(int slot, ItemStack item, ClickableAction action) {
        clickableMap.put(slot, new ContainerClickable(item, action));
    }

    protected void setItem(int row, int col, ItemStack item, ClickableAction action) {
        clickableMap.put((row*9+col), new ContainerClickable(item, action));
    }

    protected void setItem(int slot, ItemStack item, ClickableAction action, boolean locked) {
        clickableMap.put(slot, new ContainerClickable(item, action, locked));
    }

    protected void setItem(int row, int col, ItemStack item, ClickableAction action, boolean locked) {
        clickableMap.put((row*9+col), new ContainerClickable(item, action, locked));
    }

    public void parent(Container parent) {
        this.parent = parent;
    }

    public Container parent() {
        return parent;
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, size);
        for (Map.Entry<Integer, ContainerClickable> entry : clickableMap.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().item());
        }
        return inventory;
    }
}

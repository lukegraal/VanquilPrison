package com.vanquil.prison.tools.tool;

import com.google.common.collect.Maps;
import com.vanquil.prison.tools.util.material.Material2;

import java.util.HashMap;
import java.util.Map;

public class ToolsConfig {
    public static class ToolConfig {
        String displayName;
        String description;
        Material2 material;
        boolean allowDrop;

        public String displayName() {
            return displayName;
        }

        public String description() {
            return description;
        }

        public Material2 material() {
            return material;
        }

        public boolean allowDrop() {
            return allowDrop;
        }
    }

    private Map<ToolType, ToolConfig> tools = new HashMap<ToolType, ToolConfig>() {{
        put(ToolType.PICKAXE, PickaxeConf);
        put(ToolType.AXE, AxeConf);
    }};

    public ToolConfig getToolConf(ToolType type) {
        return tools.get(type);
    }

    private static final ToolConfig PickaxeConf = new ToolConfig() {{
        displayName = "&d&lPickaxe";
        description = "&7A special pickaxe that some random dude decided it was a good idea to do like 3 days of work " +
                "in order to configure and make it work with cool enchantments, I guess. Wait, why is this special again?";
        material = Material2.GOLDEN_PICKAXE;
        allowDrop = false;
    }};

    private static final ToolConfig AxeConf = new ToolConfig() {{
        displayName = "&d&lAxe";
        description = "&7A special axe that some random dude decided it was a good idea to do like 3 days of work " +
                "in order to configure and make it work with cool enchantments, I guess. Wait, why is this special again?";
        material = Material2.GOLDEN_AXE;
        allowDrop = false;
    }};
}

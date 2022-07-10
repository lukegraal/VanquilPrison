package com.vanquil.prison.tools.tool.enchantment.container.upgrade;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.util.container.config.ContainerConfig;
import com.vanquil.prison.tools.util.container.config.ContainerItemConfig;
import com.vanquil.prison.tools.util.material.Material2;

import java.util.List;

public class UpgradeContainerConfig {
    final String addAmount = "&7Add {0} &7levels: &a+{1}";
    final String price = "&7Price: &a{0}";

    public static class UpgradeButtonConfig
            extends ContainerItemConfig {
        int modifier;
    }

    public static class ModifyLevel
            extends ContainerConfig {
        {
            List<String> f = Lists.newArrayList();
            f.add("#########");
            f.add("#ABCDEFG#");
            f.add("####X####");
            format = f;

            itemMap.put("X", new ContainerItemConfig() {{
                displayName = "&cCancel transaction";
            }});
            itemMap.put("#", new ContainerItemConfig() {{
                displayName = "";
                amount = 1;
                material = Material2.CYAN_STAINED_GLASS;
            }});
            itemMap.put("A", new UpgradeButtonConfig() {{
                displayName = "&c-100 levels";
                material = Material2.RED_STAINED_GLASS_PANE;
                modifier = -100;
            }});
            itemMap.put("B", new UpgradeButtonConfig() {{
                displayName = "&c-10 levels";
                material = Material2.RED_STAINED_GLASS_PANE;
                modifier = -10;
            }});
            itemMap.put("C", new UpgradeButtonConfig() {{
                displayName = "&c-1 levels";
                material = Material2.RED_STAINED_GLASS_PANE;
                modifier = -1;
            }});
            itemMap.put("D", new UpgradeButtonConfig() {{
                displayName = "&aCheck purchase!";
                material = Material2.RED_STAINED_GLASS_PANE;
                modifier = 0;
            }});
            itemMap.put("E", new UpgradeButtonConfig() {{
                displayName = "&a-1 levels";
                material = Material2.RED_STAINED_GLASS_PANE;
                modifier = 1;
            }});
            itemMap.put("F", new UpgradeButtonConfig() {{
                displayName = "&a+10 levels";
                material = Material2.RED_STAINED_GLASS_PANE;
                modifier = 10;
            }});
            itemMap.put("G", new UpgradeButtonConfig() {{
                displayName = "&a+100 levels";
                material = Material2.RED_STAINED_GLASS_PANE;
                modifier = 100;
            }});
        }
    }
}

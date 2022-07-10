package com.vanquil.prison.tools.tool.enchantment.container.upgrade;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.util.container.config.ContainerConfig;
import com.vanquil.prison.tools.util.container.config.ContainerItemConfig;
import com.vanquil.prison.tools.util.material.Material2;

import java.util.Collections;
import java.util.List;

public class UpgradeContainerConfig {
    final String addAmount = "&7Add {0} &7levels: &a+{1}";
    final String price = "&7Price: &a{0} tokens";

    public static class UpgradeButtonConfig
            extends ContainerItemConfig {
        int modifier;
    }

    public static class ModifyLevel
            extends ContainerConfig {
        {
            closeCharacter = 'X';
            List<String> f = Lists.newArrayList();
            f.add("#########");
            f.add("#ABC@EFG#");
            f.add("####X####");
            format = f;

            itemMap.put(String.valueOf(closeCharacter), new UpgradeButtonConfig() {{
                displayName = "&cCancel transaction";
                modifier = 0;
            }});
            itemMap.put("#", new UpgradeButtonConfig() {{
                displayName = "";
                amount = 1;
                material = Material2.CYAN_STAINED_GLASS;
                modifier = 0;
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
            itemMap.put("@", new UpgradeButtonConfig() {{
                displayName = "&d&lConfirm purchase";
                material = Material2.RED_STAINED_GLASS_PANE;
                lore = Collections.singletonList("&7Click to process transaction.");
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

    public static class ConfirmTransaction
            extends ContainerConfig {
        {
            closeCharacter = 'B';
            List<String> f = Lists.newArrayList();
            f.add("#########");
            f.add("##A###B##");
            f.add("#########");
            format = f;

            itemMap.put("#", new UpgradeButtonConfig() {{
                displayName = "";
                amount = 1;
                material = Material2.CYAN_STAINED_GLASS;
                modifier = 0;
            }});
            itemMap.put(String.valueOf(closeCharacter), new UpgradeButtonConfig() {{
                displayName = "&c&lCancel transaction";
                modifier = 0;
            }});
            itemMap.put("A", new UpgradeButtonConfig() {{
                displayName = "&a&lConfirm transaction";
                modifier = 0;
            }});
        }
    }
}

package com.vanquil.prison.tools.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class C {
    public static void send(Player player, String message, Object... format) {
        String s = format(message, format);
        player.sendMessage(s);
    }

    public static String format(String s, Object... format) {
        for (int i = 0; i < format.length; i++) {
            s = s.replace("{" + i + "}", format[i].toString());
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}

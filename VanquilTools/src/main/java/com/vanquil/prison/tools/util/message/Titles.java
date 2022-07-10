package com.vanquil.prison.tools.util.message;

import com.vanquil.prison.tools.util.C;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class Titles {
    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent[] components = CraftChatMessage.fromString(C.format(message), false);
        if (components.length > 0) {
            PacketPlayOutChat chatPacket = new PacketPlayOutChat(components[0], (byte) 2);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chatPacket);
        }
    }

    public static void sendTitle(Player player, String title, String subtitle,
                                 int fadeIn, int stay, int fadeOut) {
        if (title != null) title = C.format(title);
        if (subtitle != null) subtitle = C.format(subtitle);
        PacketPlayOutTitle titlePacket = null, subtitlePacket = null;
        CraftPlayer player1 = (CraftPlayer) player;
        PlayerConnection connection = player1.getHandle().playerConnection;
        if (title != null) {
            titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                    CraftChatMessage.fromString(title)[0]);
            connection.sendPacket(titlePacket);
        }

        if (subtitle != null) {
            subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                    CraftChatMessage.fromString(subtitle)[0]);
            connection.sendPacket(subtitlePacket);
        }

        PacketPlayOutTitle timePacket = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
        if (titlePacket != null)
            connection.sendPacket(titlePacket);
        if (subtitlePacket != null)
            connection.sendPacket(subtitlePacket);
        connection.sendPacket(timePacket);
    }

    public static void resetTitle(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null);
        connection.sendPacket(title);
    }

    public static void clearTitle(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, null);
        connection.sendPacket(title);
    }
}

package com.vanquil.prison.tools.util.listeners;

import com.vanquil.prison.tools.VanquilTools;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class Listeners {
    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, VanquilTools.getPlugin(VanquilTools.class));
    }
}
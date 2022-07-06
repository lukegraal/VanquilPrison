package com.vanquil.prison.tools.util.listeners;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vanquil.prison.tools.VanquilTools;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Listeners {
    public static final int DEFAULT_PRIORITY = 0xFF;
    public static final Map<Class<? extends Event>, TreeMap<Integer, List<Listener2<? extends Event>>>> LISTENER_MAP
            = Maps.newHashMap();

    public static <T extends Event> void registerListener(int priority, Class<T> eventClass, Listener2<T> listener) {
        TreeMap<Integer, List<Listener2<? extends Event>>> treeMap
                = LISTENER_MAP.getOrDefault(eventClass, new TreeMap<>());
        List<Listener2<? extends Event>> listener2s = treeMap.get(priority);
        if (listener2s == null)
            listener2s = treeMap.put(priority, Lists.newArrayList());
        Objects.requireNonNull(listener2s).add(listener);
    }

    public static <T extends Event> void registerListener(Class<T> eventClass, Listener2<T> listener) {
        registerListener(DEFAULT_PRIORITY, eventClass, listener);
    }

    public static void registerBukkitListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, VanquilTools.getPlugin(VanquilTools.class));
    }

    public static void registerDispatcher() {
        registerBukkitListener(new Dispatcher());
    }

    static class Dispatcher implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
        @SuppressWarnings({"unchecked", "unused"})
        public void onEvent(Event event) {
            TreeMap<Integer, List<Listener2<? extends Event>>> treeMap = LISTENER_MAP.get(event.getClass());
            if (treeMap != null) {
                for (Map.Entry<Integer, List<Listener2<? extends Event>>> entry : treeMap.entrySet()) {
                    for (Listener2<? extends Event> listener2 : entry.getValue()) {
                        Listener2<Event> listener21 = (Listener2<Event>) listener2;
                        listener21.call(event);
                    }
                }
            }
        }
    }
}
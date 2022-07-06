package com.vanquil.prison.tools.util.listeners;

import org.bukkit.event.Event;

public interface Listener2<T extends Event> {
    void call(T event);
}

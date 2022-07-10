package com.vanquil.prison.tools.command;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void call(PlayerCommandPreprocessEvent event) {
        List<String> arguments = Arrays.stream(event.getMessage().split("\\s+")).collect(Collectors.toList());
        String nameOrAlias = arguments.remove(0).substring(1);
        Command command = null;
        for (Command command1 : CommandRegistry.COMMANDS) {
            CommandOptions options = command1.options();
            if (options.name().equalsIgnoreCase(nameOrAlias)) {
                command = command1;
            } else {
                for (String alias : options.aliases()) {
                    if (nameOrAlias.equalsIgnoreCase(alias)) {
                        command = command1;
                        break;
                    }
                }
            }
        }

        if (command != null) {
            command.runSelfOrChild(event.getPlayer(), arguments);
            event.setCancelled(true);
        }
    }
}

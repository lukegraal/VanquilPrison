package com.vanquil.prison.tools.command;

import com.vanquil.prison.tools.command.context.CommandContext;
import com.vanquil.prison.tools.util.listeners.Listener2;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandListener implements Listener2<PlayerCommandPreprocessEvent> {
    @Override
    public void call(PlayerCommandPreprocessEvent event) {
        event.setCancelled(true);

        List<String> arguments = Arrays.stream(event.getMessage().split("\\s+")).collect(Collectors.toList());
        String nameOrAlias = arguments.remove(0);
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
            command.runSelfOrChild(event.getPlayer(), event.getMessage(), arguments);
        }
    }
}

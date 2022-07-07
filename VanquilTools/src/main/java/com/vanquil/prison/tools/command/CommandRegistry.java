package com.vanquil.prison.tools.command;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.VanquilTools;
import com.vanquil.prison.tools.config.Config;

import java.util.List;

public final class CommandRegistry {
    static final List<Command> COMMANDS = Lists.newArrayList();
    static final Config<CommandConfig> CONFIG = new Config<>(
            VanquilTools.basePath.resolve("commands.json"),
            CommandConfig.class, CommandConfig::new);

    public static class CommandConfig {
        final String noPermissionMessage = "You do not have permission to run that command, sorry!";
    }

    public static void registerCommand(Command command) {
        COMMANDS.add(command);
    }
}

package com.vanquil.prison.tools.command;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.VanquilTools;
import com.vanquil.prison.tools.config.Config;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.util.List;

public final class CommandRegistry {
    static final List<Command> COMMANDS = Lists.newArrayList();
    static final Config<CommandConfig> CONFIG = new Config<>(
            VanquilTools.basePath.resolve("commands.json"),
            CommandConfig.class, CommandConfig::new);

    public static class CommandConfig {
        final String noPermissionMessage = "You do not have permission to run that command, sorry!";
        final String failedToParseMessage = "Couldn't figure out what you meant by \"{0}\" for an argument of type \"{1}\".";
    }

    public static void registerCommand(Command command) {
        VanquilTools plugin = VanquilTools.getPlugin(VanquilTools.class);
        CraftServer server = (CraftServer) plugin.getServer();
        server.getCommandMap().register(command.options().name(), "vanquil", command.impl);
        for (String alias : command.options().aliases())
            server.getCommandMap().register(alias, "vanquil", command.impl);
        COMMANDS.add(command);
    }
}

package com.vanquil.prison.tools.command.context;

import com.avaje.ebeaninternal.server.lib.util.MapFromString;
import com.google.common.collect.Maps;
import com.vanquil.prison.tools.command.Command;
import com.vanquil.prison.tools.command.argument.ArgumentConversionError;
import com.vanquil.prison.tools.command.argument.CommandArgument;
import org.bukkit.entity.Player;
import org.checkerframework.checker.signature.qual.SignatureUnknown;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CommandContext {
    private final Player executor;
    private final String commandString;
    private final List<String> arguments;
    private final Command command;
    private final Map<CommandArgument<?>, Object> valueMap = Maps.newHashMap();

    public CommandContext(Player executor, Command command, String commandString, List<String> arguments) {
        this.executor = executor;
        this.command = command;
        this.commandString = commandString;
        this.arguments = arguments;

        for (CommandArgument<?> argument : command.options().arguments()) {
            Object argument1 = parseArgument(argument);
            valueMap.put(argument, argument1);
        }
    }

    private <T> T parseArgument(CommandArgument<T> argument) throws ArgumentConversionError {
        List<CommandArgument<?>> commandArguments = command.arguments();

        int index = -1;
        for (int i = 0; i < commandArguments.size(); i++) {
            CommandArgument<?> arg = commandArguments.get(i);
            if (argument.equals(arg)) {
                index = i;
                break;
            }
        }

        if (index == -1)
            throw new ArgumentConversionError();
        if (arguments.size() < index+1)
            throw new ArgumentConversionError();

        String value;
        if (!argument.vararg()) {
            value = arguments.get(index);
        } else {
            int l = arguments.size();
            value = String.join(" ", arguments.subList(index, l));
        }

        try {
            T convert = argument.convert(value);
            if (convert != null)
                return convert;
        } catch (Exception e) {
            throw new ArgumentConversionError();
        }
        throw new ArgumentConversionError();
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgument(CommandArgument<T> argument) {
        Object o = valueMap.get(argument);
        return (T) o;
    }

    public Player executor() {
        return executor;
    }
}
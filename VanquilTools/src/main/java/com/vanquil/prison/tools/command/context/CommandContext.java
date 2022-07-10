package com.vanquil.prison.tools.command.context;

import com.google.common.collect.Maps;
import com.vanquil.prison.tools.command.Command;
import com.vanquil.prison.tools.command.argument.ArgumentTranslationException;
import com.vanquil.prison.tools.command.argument.CommandArgument;
import com.vanquil.prison.tools.util.C;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class CommandContext {
    private final Player executor;
    private final List<String> arguments;
    private final Command command;
    private final Map<CommandArgument<?>, Object> valueMap = Maps.newHashMap();

    public CommandContext(Player executor, Command command,
                          List<String> arguments)
            throws CommandException {
        this.executor = executor;
        this.command = command;
        this.arguments = arguments;

        for (CommandArgument<?> argument : command.options().arguments()) {
            try {
                Object argument1 = parseArgument(argument);
                valueMap.put(argument, argument1);
            } catch (ArgumentTranslationException e) {
                C.send(executor, "Couldn't parse argument \"{0}\": {1}", argument.options().name(), e.getMessage());
                throw e;
            }
        }
    }

    private <T> T parseArgument(CommandArgument<T> argument)
            throws CommandException {
        List<CommandArgument<?>> commandArguments = Arrays.asList(command.options().arguments());

        int index = -1;
        for (int i = 0; i < commandArguments.size(); i++) {
            CommandArgument<?> arg = commandArguments.get(i);
            if (argument.equals(arg)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            C.send(executor, "Something internal went wrong: argument \"{0}\" index {1} out of bounds (argument "
                    + "registered does not match argument provided at parse time).", argument.options().name(), index);
            throw new CommandException();
        }
        if (arguments.size() < index+1) {
            C.send(executor, "Argument \"{0}\" ({1}) is missing!", argument.options().name(), index);
            throw new CommandException();
        }

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
        } catch (ArgumentTranslationException e) {
            C.send(executor, "Couldn't parse argument \"{0}\" ({1}): {2}",
                    argument.options().name(), index, e.getMessage());
            throw new CommandException();
        }
        throw new CommandException();
    }

    public void respond(String message, Object... format) {
        C.send(executor, message, format);
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgument(CommandArgument<T> argument) throws CommandException {
        Object o = valueMap.get(argument);
        if (o == null) {
            C.send(executor, "Something internal went wrong: argument \"{0}\" specified " +
                    "does not match argument registered.", argument.options().name());
            throw new CommandException();
        }
        return (T) o;
    }

    public Player executor() {
        return executor;
    }
}
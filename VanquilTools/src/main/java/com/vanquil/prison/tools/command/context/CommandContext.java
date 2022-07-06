package com.vanquil.prison.tools.command.context;

import com.vanquil.prison.tools.command.Command;
import com.vanquil.prison.tools.command.argument.ArgumentConversionError;
import com.vanquil.prison.tools.command.argument.CommandArgument;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandContext {
    private final Player executor;
    private final String commandString;
    private final List<String> arguments;
    private final Command command;

    public CommandContext(Player executor, Command command, String commandString) {
        this.executor = executor;
        this.command = command;
        this.commandString = commandString;
        this.arguments = parseArguments();
    }

    private List<String> parseArguments() {
        List<String> list = Arrays.stream(commandString.split("\\s+"))
                .collect(Collectors.toList());
        list.remove(0);
        return list;
    }

    public <T> T getArgument(CommandArgument<T> argument) throws ArgumentConversionError {
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
            throw new ArgumentConversionError(ArgumentConversionError.Reason.INTERNAL_ERROR);
        if (arguments.size() < index+1)
            throw new ArgumentConversionError(ArgumentConversionError.Reason.MISSING_VALUE);

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
            throw new ArgumentConversionError(ArgumentConversionError.Reason.FAILED_TO_PARSE);
        }
        throw new ArgumentConversionError(ArgumentConversionError.Reason.INTERNAL_ERROR);
    }

    public Player executor() {
        return executor;
    }
}
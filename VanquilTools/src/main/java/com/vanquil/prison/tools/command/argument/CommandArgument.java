package com.vanquil.prison.tools.command.argument;

import com.vanquil.prison.tools.command.CommandOptions;

import java.util.function.Function;

public class CommandArgument<T> {
    private final Class<T> type;
    private final Function<String, T> converter;
    private final CommandOptions options;
    private final boolean vararg;

    private CommandArgument(Class<T> type, CommandOptions options,
                            Function<String, T> converter, boolean vararg) {
        this.type = type;
        this.options = options;
        this.converter = converter;
        this.vararg = vararg;
    }

    public static <T> CommandArgument<T> of(Class<T> type, CommandOptions options,
                                            Function<String, T> converter, boolean vararg) {
        return new CommandArgument<>(type, options, converter, vararg);
    }

    public static <T> CommandArgument<T> of(Class<T> type,
                                            CommandOptions options,
                                            Function<String, T> converter) {
        return new CommandArgument<>(type, options, converter, false);
    }

    public CommandOptions options() {
        return options;
    }

    public Class<T> type() {
        return type;
    }

    public Function<String, T> converter() {
        return converter;
    }

    public T convert(String convertible) {
        return converter.apply(convertible);
    }

    public boolean vararg() {
        return vararg;
    }

    // Options
    public static class Options {
        private final String name;
        private final String description;

        private Options(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String name() {
            return name;
        }

        public String description() {
            return description;
        }
    }

    public static Options newOptions(String name) {
        return new Options(name, "No description specified");
    }

    public static Options newOptions(String name, String description) {
        return new Options(name, description);
    }
}

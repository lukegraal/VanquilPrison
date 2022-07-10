package com.vanquil.prison.tools.command.argument;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.command.CommandOptions;
import javafx.scene.media.SubtitleTrack;

import java.util.List;
import java.util.function.Function;

public class CommandArgument<T> {
    private final Class<T> type;
    private final ArgumentTranslator<T> translator;
    private final ArgumentSuggester suggester;
    private final Options options;
    private final boolean vararg;

    private CommandArgument(Class<T> type, Options options, ArgumentSuggester suggester,
                            ArgumentTranslator<T> translator, boolean vararg) {
        this.type = type;
        this.suggester = suggester;
        this.options = options;
        this.translator = translator;
        this.vararg = vararg;
    }

    public static <T> CommandArgument<T> of(Class<T> type, Options options,
                                            ArgumentSuggester suggester,
                                            ArgumentTranslator<T> translator,
                                            boolean vararg) {
        return new CommandArgument<>(type, options, suggester, translator, vararg);
    }

    public static <T> CommandArgument<T> of(Class<T> type, Options options,
                                            ArgumentTranslator<T> translator,
                                            boolean vararg) {
        return new CommandArgument<>(type, options, ArgumentSuggester.DEFAULT, translator, vararg);
    }

    public static <T> CommandArgument<T> of(Class<T> type,
                                            Options options, ArgumentSuggester suggester,
                                            ArgumentTranslator<T> translator) {
        return new CommandArgument<>(type, options, suggester, translator, false);
    }

    public static <T> CommandArgument<T> of(Class<T> type,
                                            Options options,
                                            ArgumentTranslator<T> translator) {
        return new CommandArgument<>(type, options, ArgumentSuggester.DEFAULT, translator, false);
    }

    public ArgumentSuggester suggester() {
        return suggester;
    }

    public Options options() {
        return options;
    }

    public Class<T> type() {
        return type;
    }

    public ArgumentTranslator<T> translator() {
        return translator;
    }

    public T convert(String convertible)
            throws ArgumentTranslationException {
        return translator.translate(convertible);
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

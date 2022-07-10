package com.vanquil.prison.tools.command;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.command.argument.CommandArgument;

import javax.annotation.Nullable;
import java.util.List;

public final class CommandOptions {
    public static class Builder {
        private List<String> aliases = Lists.newArrayList();
        private String name, permission = null, description = "No description specified";
        private CommandArgument<?>[] usage = {};

        public Builder(String name) {
            this.name = name;
        }

        public Builder permission(@Nullable String permission) {
            this.permission = permission;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder aliases(String... aliases) {
            this.aliases = Lists.newArrayList(aliases);
            return this;
        }

        public Builder usage(CommandArgument<?>... arguments) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].vararg() && arguments.length-1 != i)
                    throw new IllegalArgumentException("Vararg argument must be at the end of the list");
            }

            this.usage = arguments;
            return this;
        }

        public CommandOptions build() {
            return new CommandOptions(name, description, permission, usage, aliases);
        }
    }

    private final String name, description, permission;
    private final CommandArgument<?>[] arguments;
    private final List<String> aliases;

    private CommandOptions(String name,
                           String description,
                           String permission,
                           CommandArgument<?>[] arguments,
                           List<String> aliases) {
        this.name = name;
        this.permission = permission;
        this.arguments = arguments;
        this.description = description;
        this.aliases = aliases;
    }

    public static Builder newBuilder(String name) {
        return new Builder(name);
    }

    public CommandArgument<?>[] arguments() {
        return arguments;
    }

    public String name() {
        return name;
    }

    public String permission() {
        return permission;
    }

    public String description() {
        return description;
    }

    public List<String> aliases() {
        return aliases;
    }
}

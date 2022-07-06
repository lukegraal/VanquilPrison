package com.vanquil.prison.tools.command;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.CommandOp;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.util.List;

public final class CommandOptions {
    public static class Builder {
        private String name;
        private List<String> aliases = Lists.newArrayList();
        private String permission = null;
        private String description = "No description";

        public Builder(String name) {
            this.name = name;
        }

        public void permission(@Nullable String permission) {
            this.permission = permission;
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

        public CommandOptions build() {
            return new CommandOptions(name, description, permission, aliases);
        }
    }

    public static Builder newBuilder(String name) {
        return new Builder(name);
    }

    private final String name, description, permission;
    private final List<String> aliases;

    private CommandOptions(String name,
                           String description,
                           String permission,
                           List<String> aliases) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.aliases = aliases;
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

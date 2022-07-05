package com.vanquil.prison.tools.command;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.util.List;

public final class CommandOptions {
    public static final class Builder {
        private String name;
        private List<String> aliases = Lists.newArrayList();
        private String description = "No description";

        public Builder(String name) {
            this.name = name;
        }

        public void description(String description) {
            this.description = description;
        }

        public void name(String name) {
            this.name = name;
        }

        public void aliases(String... aliases) {
            this.aliases = Lists.newArrayList(aliases);
        }
    }

    public static Builder newBuilder(String name) {
        return new Builder(name);
    }
}

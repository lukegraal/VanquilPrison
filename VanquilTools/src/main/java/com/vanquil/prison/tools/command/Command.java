package com.vanquil.prison.tools.command;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.command.argument.CommandArgument;
import com.vanquil.prison.tools.command.context.CommandContext;

import java.util.List;

public abstract class Command {
    private final CommandOptions options;
    private final List<Command> children = Lists.newArrayList();
    private final List<CommandArgument<?>> arguments = Lists.newArrayList();
    private Command parent = null;

    public Command(CommandOptions options) {
        this.options = options;
    }

    public List<Command> children() {
        return children;
    }

    public List<CommandArgument<?>> arguments() {
        return arguments;
    }

    protected void addChild(Command command) {
        children.add(command);
    }

    protected void addChildren(Command... commands) {
        children.addAll(Lists.newArrayList(commands));
    }

    protected void addArgument(CommandArgument<?> argument) {
        arguments.add(argument);
    }

    public abstract void execute(CommandContext context);
}

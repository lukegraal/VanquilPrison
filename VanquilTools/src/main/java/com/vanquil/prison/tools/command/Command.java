package com.vanquil.prison.tools.command;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.command.argument.CommandArgument;
import com.vanquil.prison.tools.command.context.CommandContext;
import com.vanquil.prison.tools.util.C;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Command {
    private final CommandOptions options;
    private final List<Command> children = Lists.newArrayList();
    private final List<CommandArgument<?>> arguments = Lists.newArrayList();
    private Command parent = null;

    public Command(CommandOptions options) {
        this.options = options;
    }

    void runSelfOrChild(Player player, String commandString, List<String> arguments) {
        if (!player.hasPermission(options.permission())) {
            C.send(player, CommandRegistry.CONFIG.instance().noPermissionMessage);
            return;
        }

        if (children.size() > 0 && arguments.size() > 0) {
            String nameOrAlias = arguments.get(0);
            Command subCommand = null;
            for (Command child : children) {
                CommandOptions ops = child.options;
                if (ops.name().equalsIgnoreCase(nameOrAlias)) {
                    subCommand = child;
                } else {
                    for (String alias : ops.aliases()) {
                        if (alias.equalsIgnoreCase(nameOrAlias)) {
                            subCommand = child;
                            break;
                        }
                    }
                }
            }

            if (subCommand != null) {
                arguments.remove(0);
                subCommand.runSelfOrChild(player, commandString, arguments);
                return;
            }
        }

        CommandContext context = new CommandContext(player, this, commandString, arguments);
        this.execute(context);
    }

    public CommandOptions options() {
        return options;
    }

    public List<Command> children() {
        return children;
    }

    public Command parent() {
        return parent;
    }

    public List<CommandArgument<?>> arguments() {
        return arguments;
    }

    protected void addChild(Command command) {
        command.parent = this;
        children.add(command);
    }

    protected void addChildren(Command... commands) {
        for (Command command : commands) command.parent = this;
        children.addAll(Lists.newArrayList(commands));
    }

    protected void addArgument(CommandArgument<?> argument) {
        arguments.add(argument);
    }

    public abstract void execute(CommandContext context);
}

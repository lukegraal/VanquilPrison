package com.vanquil.prison.tools.command;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.command.argument.CommandArgument;
import com.vanquil.prison.tools.command.context.CommandException;
import com.vanquil.prison.tools.command.context.CommandContext;
import com.vanquil.prison.tools.util.C;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Command {
    private final CommandOptions options;
    private final List<Command> children = Lists.newArrayList();
    private Command parent = null;
    final CommandImpl impl;

    class CommandImpl extends BukkitCommand {
        protected CommandImpl() {
            super(Command.this.options.name());
        }

        @Override
        public boolean execute(CommandSender sender, String s, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                runSelfOrChild(player, Lists.newArrayList(args));
            }
            return true;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            if (sender instanceof Player) {
                List<String> things = Lists.newArrayList();
                Player player = (Player) sender;
                int index = args.length-1;

                if (Command.this.children.size() > 0 && args.length == 1) {
                    for (Command child : Command.this.children) {
                        things.add(child.options.name());
                        things.addAll(child.options.aliases());
                    }
                }

                if (args.length > 1 && children.size() > 0) {
                    String subcommand = args[0];
                    for (Command c : children) {
                        if (c.options.name().equalsIgnoreCase(subcommand)) {
                            return c.impl.tabComplete(sender, alias, Arrays.copyOfRange(args, 2, args.length));
                        } else {
                            for (String a : c.options.aliases()) {
                                if (a.equalsIgnoreCase(subcommand)) {
                                    return c.impl.tabComplete(sender, alias, Arrays.copyOfRange(args, 2, args.length));
                                }
                            }
                        }
                    }
                }

                CommandArgument<?>[] arguments = options.arguments();
                if (arguments.length > 0) {
                    if (arguments.length > index + 1 && !arguments[arguments.length - 1].vararg()) {
                        return Collections.emptyList();
                    } else {
                        CommandArgument<?> argument = arguments[index];
                        things.addAll(argument.suggester().suggest(player));
                    }
                }
                return things;
            }
            return Collections.emptyList();
        }
    }

    public Command(CommandOptions options) {
        this.options = options;
        this.impl = new CommandImpl();
    }

    void runSelfOrChild(Player player, List<String> arguments) {
        if (options.permission() != null) {
            if (!player.hasPermission(options.permission())) {
                C.send(player, CommandRegistry.CONFIG.instance().noPermissionMessage);
                return;
            }
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
                subCommand.runSelfOrChild(player, arguments);
                return;
            }
        }

        CommandContext context;
        try {
            context = new CommandContext(player, this, arguments);
        } catch (CommandException e) {
            return;
        }

        try {
            execute(context);
        } catch (CommandException e) {
            C.send(player, "Something bad went wrong. :(");
            e.printStackTrace();
        }
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

    protected void addChild(Command command) {
        command.parent = this;
        children.add(command);
    }

    protected void addChildren(Command... commands) {
        for (Command command : commands) command.parent = this;
        children.addAll(Lists.newArrayList(commands));
    }

    public abstract void execute(CommandContext context)
            throws CommandException;
}

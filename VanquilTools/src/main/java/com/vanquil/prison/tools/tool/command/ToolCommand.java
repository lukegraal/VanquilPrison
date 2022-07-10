package com.vanquil.prison.tools.tool.command;

import com.google.common.collect.Lists;
import com.vanquil.prison.tools.command.Command;
import com.vanquil.prison.tools.command.CommandOptions;
import com.vanquil.prison.tools.command.argument.ArgumentTranslationException;
import com.vanquil.prison.tools.command.argument.CommandArgument;
import com.vanquil.prison.tools.command.context.CommandContext;
import com.vanquil.prison.tools.command.context.CommandException;
import com.vanquil.prison.tools.tool.ToolMetadata;
import com.vanquil.prison.tools.tool.ToolType;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.ToolEnchantment;
import com.vanquil.prison.tools.tool.enchantment.UpgradeableEnchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class ToolCommand extends Command {
    public ToolCommand() {
        super(CommandOptions.newBuilder("tool")
                .description("Parent command of all tool actions!")
                .permission("vanquil.tools")
                .build());
        addChildren(
                new GiveCommand(),
                new ApplyEnchantment()
        );
    }

    static class GiveCommand extends Command {
        static final CommandArgument<ToolType> TYPE_ARGUMENT = CommandArgument.of(
                ToolType.class, CommandArgument.newOptions("tool", "The type of the tool to give you"),
                player -> Arrays.stream(ToolType.values()).map(ToolType::name).collect(Collectors.toList()),
                value -> {
                    try {
                        return ToolType.valueOf(value);
                    } catch (Exception e) {
                        throw new ArgumentTranslationException("That's not a valid tool type!");
                    }
                }
        );

        public GiveCommand() {
            super(CommandOptions.newBuilder("give")
                    .description("Add a tool to your inventory")
                    .usage(TYPE_ARGUMENT)
                    .build());
        }

        @Override
        public void execute(CommandContext context) throws CommandException {
            PlayerInventory inventory = context.executor().getInventory();
            if (inventory.firstEmpty() == -1) {
                context.respond("&cYou don't have any space in your inventory!");
                return;
            }
            ToolType type = context.getArgument(TYPE_ARGUMENT);
            ItemStack itemStack = Tools.createNew(type);
            inventory.addItem(itemStack);
            context.respond("&aCheck your inventory!");
        }
    }

    static class ApplyEnchantment extends Command {
        private static final CommandArgument<ToolEnchantment> ENCHANTMENT_ARGUMENT = CommandArgument.of(
                ToolEnchantment.class, CommandArgument.newOptions("enchantment"),
                player -> EnchantmentRegistry.Enchantments.stream().map(ToolEnchantment::uniqueName).collect(Collectors.toList()),
                value -> {
                    ToolEnchantment enchantment = EnchantmentRegistry.findEnchantment(value);
                    if (enchantment == null)
                        throw new ArgumentTranslationException("Couldn't find an enchantment with that name!");
                    return enchantment;
                }
        );
        private static final CommandArgument<Integer> LEVEL_ARGUMENT = CommandArgument.of(
                Integer.class, CommandArgument.newOptions("level"),
                value -> {
                    try {
                        return Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        throw new ArgumentTranslationException("Couldn't figure out what that number is!");
                    }
                }
        );

        public ApplyEnchantment() {
            super(CommandOptions.newBuilder("apply")
                    .description("Add a specific enchantment!")
                    .usage(ENCHANTMENT_ARGUMENT, LEVEL_ARGUMENT)
                    .build());
        }

        @Override
        public void execute(CommandContext context) throws CommandException {
            ItemStack itemInHand = context.executor().getItemInHand();
            ToolMetadata metadata = Tools.metadata(itemInHand);
            if (metadata == null) {
                context.respond("&cYou need to be holding a valid special item to run this command.");
                return;
            }
            ToolEnchantment enchantment = context.getArgument(ENCHANTMENT_ARGUMENT);
            if (enchantment.type() != metadata.toolType()) {
                context.respond("You can't apply that enchantment to this type of tool.");
                return;
            }
            int level = context.getArgument(LEVEL_ARGUMENT);
            if (enchantment instanceof UpgradeableEnchantment) {
                UpgradeableEnchantment ue = (UpgradeableEnchantment) enchantment;
                if (level > ue.maxLevel()) {
                    context.respond("&cThat level is too high for the enchantment!");
                    return;
                }
            }

            ItemStack item = Tools.updateMetadata(itemInHand, meta -> {
                if (enchantment instanceof UpgradeableEnchantment) {
                    meta.setEnchantmentLevel(((UpgradeableEnchantment) enchantment), level);
                } else {
                    meta.addEnchantment(enchantment);
                }
                return meta;
            });
            context.executor().setItemInHand(item);
            context.respond("&aUpdated the item in your hand!");
        }
    }

    @Override
    public void execute(CommandContext context) {
    }
}

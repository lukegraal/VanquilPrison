package com.vanquil.prison.tools;

import com.google.gson.*;
import com.vanquil.prison.tools.command.CommandListener;
import com.vanquil.prison.tools.command.CommandRegistry;
import com.vanquil.prison.tools.minimines.Mines;
import com.vanquil.prison.tools.tool.Tools;
import com.vanquil.prison.tools.tool.command.ToolCommand;
import com.vanquil.prison.tools.tool.enchantment.EnchantmentRegistry;
import com.vanquil.prison.tools.tool.enchantment.task.ToolPotionEffectTask;
import com.vanquil.prison.tools.util.Scheduling;
import com.vanquil.prison.tools.util.listeners.Listeners;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class VanquilTools extends JavaPlugin {
    public static final Gson PRETTY_GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    public static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();
    public static Path basePath;

    @Override
    public void onEnable() {
        basePath = getDataFolder().toPath();
        Listeners.registerListener(new CommandListener());

        // Load objects - don't actually run any code, just call the static initializer.
        EnchantmentRegistry.load();
        Tools.load();
        Mines.load();

        Scheduling.repeat(new ToolPotionEffectTask(), 5, 5);
        CommandRegistry.registerCommand(new ToolCommand());
    }
}

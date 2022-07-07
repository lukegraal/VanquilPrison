package com.vanquil.prison.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vanquil.prison.tools.locale.Locale;
import com.vanquil.prison.tools.minimines.MineManager;
import com.vanquil.prison.tools.tool.enchantment.task.ToolPotionEffectTask;
import com.vanquil.prison.tools.util.Scheduling;
import com.vanquil.prison.tools.util.listeners.Listeners;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class VanquilTools extends JavaPlugin {
    public static final Gson
            PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create(),
            GSON = new GsonBuilder().create();
    public static Path basePath;

    private MineManager mineManager;

    @Override
    public void onEnable() {
        basePath = getDataFolder().toPath();
        Listeners.registerDispatcher();

        Locale.load(this);
        Scheduling.repeat(new ToolPotionEffectTask(), 10, 10);

        this.mineManager = new MineManager();
    }
}

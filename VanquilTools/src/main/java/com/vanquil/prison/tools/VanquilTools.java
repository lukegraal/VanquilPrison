package com.vanquil.prison.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vanquil.prison.tools.locale.Locale;
import com.vanquil.prison.tools.tool.enchantment.task.ToolPotionEffectTask;
import com.vanquil.prison.tools.util.Scheduling;
import org.bukkit.plugin.java.JavaPlugin;

public class VanquilTools extends JavaPlugin {
    public static final Gson
            PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create(),
            GSON = new GsonBuilder().create();

    @Override
    public void onEnable() {
        Locale.load(this);
        Scheduling.repeat(new ToolPotionEffectTask(), 10, 10);
    }
}

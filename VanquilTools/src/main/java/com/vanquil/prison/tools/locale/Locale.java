package com.vanquil.prison.tools.locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.vanquil.prison.tools.VanquilTools;

import java.io.*;
import java.nio.file.Paths;
import java.util.function.Function;

public class Locale {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "lang.json";
    private static LocaleObject instance;

    public static void load(VanquilTools plugin) {
        File file = Paths.get(plugin.getDataFolder().getAbsolutePath(), FILE_NAME).toFile();
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.append(GSON.toJson(new LocaleObject()));
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to write default locale file");
                e.printStackTrace();
            }
        } else {
            try (FileReader reader = new FileReader(file)) {
                instance = GSON.fromJson(reader, LocaleObject.class);
            } catch (IOException | JsonIOException e) {
                plugin.getLogger().warning("Failed to read locale file");
                e.printStackTrace();
            }
        }
    }

    public String get(Function<LocaleObject, String> getter) {
        return getter.apply(instance);
    }
}

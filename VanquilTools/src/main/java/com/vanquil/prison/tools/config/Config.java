package com.vanquil.prison.tools.config;

import com.vanquil.prison.tools.VanquilTools;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.bukkit.util.FileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class Config<T> {
    private final Path path;
    private final Supplier<T> defaultSupplier;
    private final Class<T> type;
    private T instance;

    public Config(Path path, Class<T> type, Supplier<T> defaultSupplier) {
        this.path = path;
        this.type = type;
        this.defaultSupplier = defaultSupplier;
        this.instance = defaultSupplier.get();
        this.read();
    }

    public T instance() {
        return instance;
    }

    public void read() {
        if (!Files.exists(path)) {
            this.instance = defaultSupplier.get();
            this.write();
        } else try (BufferedReader reader = Files.newBufferedReader(path)) {
            this.instance = VanquilTools.PRETTY_GSON.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write() {
        File file = path.toFile();
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("Couldn't make directories :c");
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            VanquilTools.PRETTY_GSON.toJson(instance, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

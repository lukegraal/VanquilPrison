package com.vanquil.prison.tools.config;

import com.vanquil.prison.tools.VanquilTools;

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
            this.write();
            this.instance = defaultSupplier.get();
        } else try (BufferedReader reader = Files.newBufferedReader(path)) {
            this.instance = VanquilTools.GSON.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            VanquilTools.PRETTY_GSON.toJson(instance, bufferedWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

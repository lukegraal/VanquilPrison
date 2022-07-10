package com.vanquil.prison.tools.util;

import com.vanquil.prison.tools.VanquilTools;
import org.bukkit.Bukkit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduling {
    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    private static final VanquilTools PLUGIN = VanquilTools.getPlugin(VanquilTools.class);

    public static ScheduledFuture<?> repeat(Runnable runnable,
                                            long initialDelay, long period, TimeUnit unit) {
        return SERVICE.scheduleAtFixedRate(
                () -> Bukkit.getScheduler().runTask(PLUGIN, runnable),
                initialDelay, period, unit
        );
    }

    public static ScheduledFuture<?> repeat(Runnable runnable,
                                            long initialDelaySeconds, long periodSeconds) {
        return repeat(
                runnable, initialDelaySeconds,
                periodSeconds, TimeUnit.SECONDS
        );
    }

    public static ScheduledFuture<?> delay(Runnable runnable,
                                           long delay, TimeUnit unit) {
        return SERVICE.schedule(
                () -> Bukkit.getScheduler().runTask(PLUGIN, runnable),
                delay, unit
        );
    }

    public static ScheduledFuture<?> delay(Runnable runnable,
                                           long delaySeconds) {
        return delay(runnable, delaySeconds, TimeUnit.SECONDS);
    }
}

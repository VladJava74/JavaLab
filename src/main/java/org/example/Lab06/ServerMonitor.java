package org.example.Lab06;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ServerMonitor implements AutoCloseable {
    // Аналог события DataReceived
    @FunctionalInterface
    public interface DataListener {
        void onDataReceived(double cpu, double ram, double temperature);
    }

    private final Random random = new Random();
    private final List<DataListener> listeners = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService timer =
            Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable);
                thread.setDaemon(true);
                return thread;
            });

    private ScheduledFuture<?> task;

    private double cpu = 35;
    private double ram = 42;
    private double temperature = 48;

    public void addDataListener(DataListener listener) {
        listeners.add(listener);
    }

    public void removeDataListener(DataListener listener) {
        listeners.remove(listener);
    }

    public synchronized void start() {
        if (task == null || task.isCancelled() || task.isDone()) {
            // Первый вызов — через секунду, затем каждую секунду
            task = timer.scheduleAtFixedRate(
                    this::generateData, 1, 1, TimeUnit.SECONDS);
        }
    }

    public synchronized void stop() {
        if (task != null) {
            task.cancel(false);
            task = null;
        }
    }

    private void generateData() {
        cpu += random.nextDouble() * 10 - 5;
        ram += random.nextDouble() * 6 - 3;
        temperature += random.nextDouble() * 4 - 2;

        cpu = clamp(cpu, 0, 100);
        ram = clamp(ram, 10, 100);
        temperature = clamp(temperature, 30, 95);

        double roundedCpu = roundToOneDecimal(cpu);
        double roundedRam = roundToOneDecimal(ram);
        double roundedTemperature = roundToOneDecimal(temperature);

        for (DataListener listener : listeners) {
            listener.onDataReceived(
                    roundedCpu, roundedRam, roundedTemperature);
        }
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    private static double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    @Override
    public void close() {
        stop();
        timer.shutdownNow();
    }
}
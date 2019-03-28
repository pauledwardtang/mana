package io.phatcat.mana.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class AppExecutors {
    private Executor main;
    private ExecutorService io;
    private ExecutorService network;

    AppExecutors(Executor main, ExecutorService io, ExecutorService network) {
        this.main = main;
        this.io = io;
        this.network = network;
    }

    public Executor main() {
        return main;
    }

    public ExecutorService io() {
        return io;
    }

    public ExecutorService network() {
        return network;
    }
}

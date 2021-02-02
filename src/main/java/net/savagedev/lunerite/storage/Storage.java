package net.savagedev.lunerite.storage;

import net.savagedev.lunerite.storage.implementation.StorageImplementation;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Storage {
    private final StorageImplementation implementation;

    public Storage(final StorageImplementation implementation) {
        this.implementation = implementation;
    }

    public void init() {
        this.implementation.init();
    }

    public void shutdown() {
        this.implementation.shutdown();
    }

    public CompletableFuture<Void> create(final UUID uuid, String name, final double defaultBalance) {
        return CompletableFuture.runAsync(() -> this.implementation.create(uuid, name, defaultBalance));
    }

    public CompletableFuture<Void> setBalance(final UUID uuid, final double balance) {
        return CompletableFuture.runAsync(() -> this.implementation.setBalance(uuid, balance));
    }

    public CompletableFuture<Double> getBalance(final UUID uuid) {
        return CompletableFuture.supplyAsync(() -> this.implementation.getBalance(uuid));
    }

    public CompletableFuture<Boolean> exists(final UUID uuid) {
        return CompletableFuture.supplyAsync(() -> this.implementation.exists(uuid));
    }
}

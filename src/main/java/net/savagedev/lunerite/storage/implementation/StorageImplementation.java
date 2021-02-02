package net.savagedev.lunerite.storage.implementation;

import java.util.UUID;

public interface StorageImplementation {
    void init();

    void shutdown();

    void create(final UUID uuid, final String name, final double defaultBalance);

    void setBalance(final UUID uuid, final double balance);

    double getBalance(final UUID uuid);

    boolean exists(final UUID uuid);
}

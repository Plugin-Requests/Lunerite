package net.savagedev.lunerite.storage.implementation.file;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.savagedev.lunerite.storage.implementation.StorageImplementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractFileStorage implements StorageImplementation {
    private final Cache<UUID, ReadWriteLock> lockCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(5, TimeUnit.MINUTES).build();

    private final Path path;

    public AbstractFileStorage(final Path path) {
        this.path = path;
    }

    @Override
    public void init() {
        if (Files.notExists(this.path)) {
            try {
                Files.createDirectories(this.path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public boolean exists(UUID uuid) {
        return Files.exists(this.getPath().resolve(uuid.toString() + ".yml"));
    }

    protected Cache<UUID, ReadWriteLock> getLockCache() {
        return this.lockCache;
    }

    protected Path getPath() {
        return this.path;
    }
}

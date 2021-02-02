package net.savagedev.lunerite.storage.implementation.file.yml;

import net.savagedev.lunerite.storage.implementation.file.AbstractFileStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class YamlImplementation extends AbstractFileStorage {
    public YamlImplementation(Path path) {
        super(path);
    }

    @Override
    public void create(UUID uuid, String name, double defaultBalance) {
        final Path path = this.getPath().resolve(uuid.toString() + ".yml");
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setBalance(uuid, defaultBalance);
        }
    }

    @Override
    public void setBalance(UUID uuid, double balance) {
        Lock lock = null;
        try {
            lock = this.getLockCache().get(uuid, ReentrantReadWriteLock::new).writeLock();
            lock.lock();
            final FileConfiguration dataFile = this.load(uuid);
            if (dataFile != null) {
                dataFile.set("balance", balance);
                this.save(dataFile, uuid);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    private void save(FileConfiguration dataFile, UUID uuid) {
        try {
            dataFile.save(this.getPath().resolve(uuid.toString() + ".yml").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getBalance(UUID uuid) {
        Lock lock = null;
        try {
            lock = this.getLockCache().get(uuid, ReentrantReadWriteLock::new).readLock();
            lock.lock();
            final FileConfiguration dataFile = this.load(uuid);
            if (dataFile != null) {
                return dataFile.getDouble("balance");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
        return -1;
    }

    private FileConfiguration load(final UUID uuid) {
        try (final BufferedReader reader = Files.newBufferedReader(this.getPath().resolve(uuid.toString() + ".yml"))) {
            return YamlConfiguration.loadConfiguration(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

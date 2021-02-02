package net.savagedev.lunerite.storage.implementation.file.json;

import net.savagedev.lunerite.storage.implementation.file.AbstractFileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class JsonImplementation extends AbstractFileStorage {
    public JsonImplementation(Path path) {
        super(path);
    }

    @Override
    public void create(UUID uuid, String name, double defaultBalance) {
        final Path path = this.getPath().resolve(uuid.toString() + ".json");
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
    }

    @Override
    public double getBalance(UUID uuid) {
        return 0;
    }
}

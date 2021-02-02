package net.savagedev.lunerite.model;

import net.savagedev.lunerite.Lunerite;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BalanceManager {
    private final Map<UUID, Double> balances = new HashMap<>();
    private final Lunerite lunerite;

    public BalanceManager(final Lunerite lunerite) {
        this.lunerite = lunerite;
    }

    public void init() {
        this.balances.clear();
        for (Player player : this.lunerite.getServer().getOnlinePlayers()) {
            this.load(player.getUniqueId(), player.getName());
        }
    }

    public CompletableFuture<Void> load(UUID uuid, String name) {
        return CompletableFuture.runAsync(() -> {
            this.lunerite.getStorage().create(uuid, name, this.lunerite.getConfig().getDouble("default-balance")).join();
            if (this.isLoaded(uuid)) {
                return;
            }
            this.balances.put(uuid, this.lunerite.getStorage().getBalance(uuid).join());
        });
    }

    public void unload(final UUID uuid) {
        this.balances.remove(uuid);
    }

    public CompletableFuture<Void> setBalance(final UUID uuid, final double balance) {
        return CompletableFuture.runAsync(() -> {
            if (this.isLoaded(uuid)) {
                this.balances.replace(uuid, balance);
            } else {
                this.balances.put(uuid, balance);
            }
            this.lunerite.getStorage().setBalance(uuid, balance).join();
        });
    }

    public CompletableFuture<Double> getBalance(final UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            if (this.isLoaded(uuid)) {
                return this.balances.get(uuid);
            }
            return this.lunerite.getStorage().getBalance(uuid).join();
        });
    }

    private boolean isLoaded(final UUID uuid) {
        return this.balances.containsKey(uuid);
    }
}

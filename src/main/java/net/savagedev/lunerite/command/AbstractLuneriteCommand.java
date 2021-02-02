package net.savagedev.lunerite.command;

import net.savagedev.lunerite.Lunerite;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractLuneriteCommand implements LuneriteCommand {
    private final Lunerite lunerite;

    public AbstractLuneriteCommand(final Lunerite lunerite) {
        this.lunerite = lunerite;
    }

    protected CompletableFuture<OfflinePlayer> getPlayer(final String username) {
        return CompletableFuture.supplyAsync(() -> {
            final Player player = this.lunerite.getServer().getPlayer(username);
            if (player == null) {
                return this.lunerite.getServer().getOfflinePlayer(username);
            }
            return player;
        });
    }

    protected boolean isNumber(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ignored) {
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException ignored1) {
            }
        }
        return false;
    }

    protected Lunerite getLunerite() {
        return this.lunerite;
    }
}

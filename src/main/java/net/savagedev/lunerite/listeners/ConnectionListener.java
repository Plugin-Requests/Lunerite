package net.savagedev.lunerite.listeners;

import net.savagedev.lunerite.Lunerite;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    private final Lunerite lunerite;

    public ConnectionListener(final Lunerite lunerite) {
        this.lunerite = lunerite;
    }

    @EventHandler
    public void on(final AsyncPlayerPreLoginEvent e) {
        this.lunerite.getBalanceManager().load(e.getUniqueId(), e.getName()).join();
    }

    @EventHandler
    public void on(final PlayerQuitEvent e) {
        this.lunerite.getBalanceManager().unload(e.getPlayer().getUniqueId());
    }
}

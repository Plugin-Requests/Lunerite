package net.savagedev.lunerite.hook.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.savagedev.lunerite.Lunerite;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LuneritePapiExpansion extends PlaceholderExpansion {
    private final Lunerite plugin;

    public LuneritePapiExpansion(Lunerite plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lunerite";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Mesmeralis";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("balance")) {
            Double balance = this.plugin.getBalanceManager().getBalance(player);
            return String.format("%.1f", balance);
        }
        return null;
    }
}

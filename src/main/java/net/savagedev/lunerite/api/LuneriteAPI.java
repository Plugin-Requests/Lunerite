package net.savagedev.lunerite.api;

import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public interface LuneriteAPI {
    void setBalance(Player player, double amount);

    double getBalance(Player player);

    DecimalFormat getDecimalFormat();
}

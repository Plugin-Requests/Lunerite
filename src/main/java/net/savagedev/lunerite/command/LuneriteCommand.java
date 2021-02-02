package net.savagedev.lunerite.command;

import org.bukkit.command.CommandSender;

public interface LuneriteCommand {
    void execute(final CommandSender sender, final String label, final String[] args);

    String getPermission();
}

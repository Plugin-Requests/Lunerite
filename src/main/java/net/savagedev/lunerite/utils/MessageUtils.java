package net.savagedev.lunerite.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MessageUtils {
    public static void message(CommandSender sender, List<String> messages) {
        messages.forEach(message -> message(sender, message));
    }

    public static void message(CommandSender sender, String message) {
        if (!message.equalsIgnoreCase("none")) {
            sender.sendMessage(color(message));
        }
    }

    private static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}

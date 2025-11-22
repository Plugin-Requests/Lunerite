package net.savagedev.lunerite.commands;

import net.savagedev.lunerite.Lunerite;
import net.savagedev.lunerite.command.AsyncCommandExecutor;
import net.savagedev.lunerite.command.LuneriteCommand;
import net.savagedev.lunerite.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LuneriteCmd extends AsyncCommandExecutor {
    private final Map<String, LuneriteCommand> commandMap = new HashMap<>();

    public LuneriteCmd(final Lunerite lunerite) {
        super(lunerite);
    }

    public void registerCommand(final String name, final LuneriteCommand executor) {
        this.commandMap.putIfAbsent(name.toLowerCase(), executor);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                MessageUtils.message(sender, this.getLunerite().getLang().getString("balance").replace("%balance%", String.valueOf(this.getLunerite().getBalanceManager().getBalance((Player) sender))));
            } else {
                MessageUtils.message(sender, this.getLunerite().getLang().getString("invalid-arguments").replace("%command%", this.getSuggestion("")));
            }
            return;
        }

        final String commandName = args[0].toLowerCase();

        if (!this.commandMap.containsKey(commandName)) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("invalid-arguments").replace("%command%", this.getSuggestion(commandName)));
            return;
        }

        final LuneriteCommand command = this.commandMap.get(commandName);

        if (!sender.hasPermission(command.getPermission())) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("no-permission"));
            return;
        }

        command.execute(sender, label, args);
    }

    private String getSuggestion(final String str) {
        if (str != null && !str.isEmpty()) {
            for (String commandName : this.commandMap.keySet()) {
                if (commandName.startsWith(str)) {
                    return commandName;
                }
            }
        }
        return "help";
    }

    @Override
    public String getPermission() {
        return null;
    }
}

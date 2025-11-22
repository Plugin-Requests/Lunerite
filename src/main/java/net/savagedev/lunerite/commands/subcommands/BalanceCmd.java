package net.savagedev.lunerite.commands.subcommands;

import net.savagedev.lunerite.Lunerite;
import net.savagedev.lunerite.command.AbstractLuneriteCommand;
import net.savagedev.lunerite.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCmd extends AbstractLuneriteCommand {
    public BalanceCmd(Lunerite lunerite) {
        super(lunerite);
    }

    @Override // balance <username>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                MessageUtils.message(sender, this.getLunerite().getLang().getString("balance").replace("%balance%", String.valueOf(this.getLunerite().getBalanceManager().getBalance((Player) sender))));
            } else {
                MessageUtils.message(sender, this.getLunerite().getLang().getString("invalid-arguments").replace("%command%", "balance <username>"));
            }
            return;
        }

        final OfflinePlayer player = this.getPlayer(args[1]).join();

        double balance = this.getLunerite().getBalanceManager().getBalanceAsync(player.getUniqueId()).join();

        if (balance == -1) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("unknown-player"));
        } else {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("balance-other").replace("%username%", player.getName()).replace("%balance%", String.valueOf(balance)));
        }
    }

    @Override
    public String getPermission() {
        return "lunerite.balance.others";
    }
}

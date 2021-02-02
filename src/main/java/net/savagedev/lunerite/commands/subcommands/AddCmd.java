package net.savagedev.lunerite.commands.subcommands;

import net.savagedev.lunerite.Lunerite;
import net.savagedev.lunerite.command.AbstractLuneriteCommand;
import net.savagedev.lunerite.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class AddCmd extends AbstractLuneriteCommand {
    public AddCmd(Lunerite lunerite) {
        super(lunerite);
    }

    @Override // add <username> <amount>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("invalid-arguments").replace("%command%", "add <username> <amount>"));
            return;
        }

        final String targetName = args[1];

        if (args.length == 2) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("invalid-arguments").replace("%command%", "add " + targetName + " <amount>"));
            return;
        }

        final String amountStr = args[2];

        if (!this.isNumber(amountStr)) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("not-a-number"));
            return;
        }

        double amount = Double.valueOf(amountStr);

        final OfflinePlayer player = this.getPlayer(args[1]).join();

        final double balance = this.getLunerite().getBalanceManager().getBalance(player.getUniqueId()).join();
        if (balance == -1) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("unknown-player"));
        } else {
            this.getLunerite().getStorage().setBalance(player.getUniqueId(), balance + amount).join();
            MessageUtils.message(sender, this.getLunerite().getLang().getString("balance-add").replace("%amount%", String.valueOf(amount)).replace("%username%", player.getName()).replace("%balance%", String.valueOf(balance + amount)));
        }
    }

    @Override
    public String getPermission() {
        return "lunerite.add";
    }
}

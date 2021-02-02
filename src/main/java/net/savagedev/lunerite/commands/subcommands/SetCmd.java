package net.savagedev.lunerite.commands.subcommands;

import net.savagedev.lunerite.Lunerite;
import net.savagedev.lunerite.command.AbstractLuneriteCommand;
import net.savagedev.lunerite.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SetCmd extends AbstractLuneriteCommand {
    public SetCmd(Lunerite lunerite) {
        super(lunerite);
    }

    @Override // set <username> <amount>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("invalid-arguments").replace("%command%", "set <username> <amount>"));
            return;
        }

        final String targetName = args[1];

        if (args.length == 2) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("invalid-arguments").replace("%command%", "set " + targetName + " <amount>"));
            return;
        }

        final String balanceStr = args[2];

        if (!this.isNumber(balanceStr)) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("not-a-number"));
            return;
        }

        double amount = Double.valueOf(balanceStr);

        if (amount < 0) {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("invalid-balance"));
            return;
        }

        final OfflinePlayer player = this.getPlayer(targetName).join();

        if (this.getLunerite().getStorage().exists(player.getUniqueId()).join()) {
            this.getLunerite().getBalanceManager().setBalance(player.getUniqueId(), amount);
            MessageUtils.message(sender, this.getLunerite().getLang().getString("balance-set").replace("%username%", player.getName()).replace("%balance%", String.valueOf(amount)));
        } else {
            MessageUtils.message(sender, this.getLunerite().getLang().getString("unknown-player"));
        }
    }

    @Override
    public String getPermission() {
        return "lunerite.set";
    }
}

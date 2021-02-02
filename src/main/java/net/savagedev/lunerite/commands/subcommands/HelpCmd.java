package net.savagedev.lunerite.commands.subcommands;

import net.savagedev.lunerite.Lunerite;
import net.savagedev.lunerite.command.AbstractLuneriteCommand;
import net.savagedev.lunerite.utils.MessageUtils;
import org.bukkit.command.CommandSender;

public class HelpCmd extends AbstractLuneriteCommand {
    public HelpCmd(Lunerite lunerite) {
        super(lunerite);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        MessageUtils.message(sender, this.getLunerite().getLang().getStringList("help"));
    }

    @Override
    public String getPermission() {
        return "lunerite.help";
    }
}

package net.savagedev.lunerite.command;

import net.savagedev.lunerite.Lunerite;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public abstract class AsyncCommandExecutor extends AbstractLuneriteCommand implements CommandExecutor {
    public AsyncCommandExecutor(Lunerite lunerite) {
        super(lunerite);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        this.getLunerite().getServer().getScheduler().runTaskAsynchronously(this.getLunerite(), () -> this.execute(sender, label, args));
        return true;
    }
}

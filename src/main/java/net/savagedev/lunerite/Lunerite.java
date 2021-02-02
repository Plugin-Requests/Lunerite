package net.savagedev.lunerite;

import com.zaxxer.hikari.HikariConfig;
import io.sentry.Sentry;
import net.savagedev.lunerite.commands.LuneriteCmd;
import net.savagedev.lunerite.commands.subcommands.*;
import net.savagedev.lunerite.listeners.ConnectionListener;
import net.savagedev.lunerite.model.BalanceManager;
import net.savagedev.lunerite.storage.Storage;
import net.savagedev.lunerite.storage.implementation.file.json.JsonImplementation;
import net.savagedev.lunerite.storage.implementation.file.yml.YamlImplementation;
import net.savagedev.lunerite.storage.implementation.sql.mysql.MySqlImplementation;
import net.savagedev.updatechecker.ResourceUpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class Lunerite extends JavaPlugin {
    private final BalanceManager balanceManager = new BalanceManager(this);

    private Path langPath = this.getDataFolder().toPath();
    private FileConfiguration lang;
    private Storage storage;

    @Override
    public void onEnable() {
        Sentry.init(options -> options.setDsn("https://abc04e0e22b749fbb6231747949325b4@o514350.ingest.sentry.io/5618839"));
        this.initConfig();
        this.initStorage();
        this.initCommands();
        this.initListeners();
        this.checkUpdates();
        this.sendTestSentryEvent();
    }

    @Override
    public void onDisable() {
        this.storage.shutdown();
    }

    private void sendTestSentryEvent() {
        try {
            throw new Exception("Test!");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }

    private void checkUpdates() {
        new ResourceUpdateChecker(1234)
                .addSuccessListener(version -> {
                    if (!this.getDescription().getVersion().equals(version)) {
                        this.getLogger().log(Level.INFO, "Update available! Download at: https://www.spigotmc.org/resource/Lunertie.1234");
                    } else {
                        this.getLogger().log(Level.INFO, "Thank you for using Lunerite!");
                    }
                }).addFailureListener(Throwable::printStackTrace)
                .checkUpdatesAsync();
    }

    private void initConfig() {
        this.saveDefaultConfig();

        final String locale = this.getConfig().getString("locale");

        try (final DirectoryStream<Path> paths = Files.newDirectoryStream(this.langPath, "lang_*.yml")) {
            for (Path path : paths) {
                if (!path.getFileName().toString().equalsIgnoreCase("lang_" + locale + ".yml")) {
                    Files.delete(path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.langPath = this.langPath.resolve("lang_" + locale + ".yml");

        if (Files.notExists(this.langPath)) {
            try (final InputStream resource = this.getResource("lang_en.yml")) {
                Files.createDirectories(this.langPath.getParent());
                if (resource == null) {
                    throw new IOException("Failed to create lang_" + locale + ".yml! InputStream null.");
                } else {
                    Files.copy(resource, this.langPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (final BufferedReader reader = Files.newBufferedReader(this.langPath)) {
            this.lang = YamlConfiguration.loadConfiguration(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStorage() {
        String backendName = this.getConfig().getString("storage.backend");
        if (backendName == null) {
            backendName = "";
        }

        StorageBackend backend;
        try {
            backend = StorageBackend.valueOf(backendName.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            this.getLogger().log(Level.WARNING, "No storage backend specified. Defaulting to YAML.");
            backend = StorageBackend.YAML;
        }

        switch (backend) {
            case MYSQL:
                final HikariConfig config = new HikariConfig();
                config.setJdbcUrl("jdbc:mysql://" + this.getConfig().getString("storage.sql.host") + ":" + this.getConfig().getString("storage.sql.port") + "/" + this.getConfig().getString("storage.sql.database"));
                config.setUsername(this.getConfig().getString("storage.sql.username"));
                config.setPassword(this.getConfig().getString("storage.sql.password"));
                this.storage = new Storage(new MySqlImplementation(config));
                break;
            case JSON:
                this.storage = new Storage(new JsonImplementation(this.getDataFolder().toPath().resolve("data")));
                break;
            case YAML:
                this.storage = new Storage(new YamlImplementation(this.getDataFolder().toPath().resolve("data")));
                break;
        }
        this.storage.init();
        this.balanceManager.init();
    }

    private void initCommands() {
        final PluginCommand luneriteCmd = this.getCommand("lunerite");
        if (luneriteCmd != null) {
            final LuneriteCmd executor = new LuneriteCmd(this);
            executor.registerCommand("add", new AddCmd(this));
            executor.registerCommand("balance", new BalanceCmd(this));
            executor.registerCommand("help", new HelpCmd(this));
            executor.registerCommand("remove", new RemoveCmd(this));
            executor.registerCommand("set", new SetCmd(this));
            luneriteCmd.setExecutor(executor);
        }
    }

    private void initListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(this), this);
    }

    public BalanceManager getBalanceManager() {
        return this.balanceManager;
    }

    public FileConfiguration getLang() {
        return this.lang;
    }

    public Storage getStorage() {
        return this.storage;
    }

    private enum StorageBackend {
        YAML, JSON, MYSQL
    }
}

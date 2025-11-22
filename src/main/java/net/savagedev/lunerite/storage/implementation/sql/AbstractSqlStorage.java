package net.savagedev.lunerite.storage.implementation.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.savagedev.lunerite.storage.implementation.StorageImplementation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSqlStorage implements StorageImplementation {
    private final HikariConfig config;

    private HikariDataSource dataSource;

    public AbstractSqlStorage(final HikariConfig config) {
        this.config = config;
    }

    @Override
    public void init() {
        this.config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.config.setPoolName("lunerite-hikari");

        this.config.setMaximumPoolSize(10);
        this.config.setMinimumIdle(2);
        this.config.setConnectionTimeout(TimeUnit.MINUTES.toMillis(30));

        this.dataSource = new HikariDataSource(this.config);
        this.setupTables();
    }

    protected abstract void setupTables();

    @Override
    public void shutdown() {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
    }

    protected Connection getConnection() {
        try {
            final Connection connection = this.dataSource.getConnection();
            if (connection != null) {
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

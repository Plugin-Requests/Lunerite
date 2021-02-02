package net.savagedev.lunerite.storage.implementation.sql.mysql;

import com.zaxxer.hikari.HikariConfig;
import net.savagedev.lunerite.storage.implementation.sql.AbstractSqlStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlImplementation extends AbstractSqlStorage {
    public MySqlImplementation(final HikariConfig config) {
        super(config);
    }

    @Override
    protected void setupTables() {
        try (final Connection connection = this.getConnection()) {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `lunerite_data` (id VARCHAR(36) PRIMARY KEY NOT NULL, name VARCHAR(16), balance DOUBLE);").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(UUID uuid, String name, double defaultBalance) {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("INSERT INTO `lunerite_data` (id, name, balance) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE id=id;")) {
                statement.setString(1, uuid.toString());
                statement.setString(2, name);
                statement.setDouble(3, defaultBalance);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBalance(UUID uuid, double balance) {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("UPDATE `lunerite_data` SET balance=? WHERE id=?")) {
                statement.setDouble(1, balance);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getBalance(UUID uuid) {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("SELECT balance FROM `lunerite_data` WHERE id=?")) {
                statement.setString(1, uuid.toString());
                final ResultSet result = statement.executeQuery();
                if (result.next()) {
                    return result.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean exists(UUID uuid) {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement("SELECT id FROM `lunerite_data` WHERE id=?")) {
                statement.setString(1, uuid.toString());
                return statement.executeQuery().next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

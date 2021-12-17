package me.frxq15.chaoscurrency.SQLManager;

import me.frxq15.chaoscurrency.ChaosCurrency;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLManager {
    static ChaosCurrency plugin = ChaosCurrency.getPlugin(ChaosCurrency.class);
    static String table = "chaoscurrency";

    public static boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void createPlayer(final UUID uuid, String name) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if (!playerExists(uuid)) {
                PreparedStatement insert = plugin.getConnection()
                        .prepareStatement("INSERT INTO " + table + "(uuid,player,gold,silver,bronze) VALUES (?,?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, name);
                insert.setInt(3, 0);
                insert.setInt(4, 0);
                insert.setInt(5, 0);
                insert.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updatePlayerName(Player player) {
        try {
            PreparedStatement selectPlayer = plugin.getConnection().prepareStatement("SELECT * FROM `" + table + "` WHERE uuid = ?;");
            selectPlayer.setString(1, player.getUniqueId().toString());
            ResultSet playerResult = selectPlayer.executeQuery();
            if (playerResult.next() && !playerResult.getString("player").equals(player.getName())) {
                PreparedStatement updateName = plugin.getConnection().prepareStatement("UPDATE `"+table + "` SET player = ? WHERE uuid = ?;");
                updateName.setString(1, player.getName());
                updateName.setString(2, player.getUniqueId().toString());
                updateName.executeUpdate();
            }
            playerResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createTable(String table) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin.getInstance(), () -> {
            try {
                PreparedStatement statement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + table +
                        "` (uuid VARCHAR(36) PRIMARY KEY, player VARCHAR(16), gold INT(11), silver INT(11), bronze INT(11));");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public void setCurrency(UUID uuid, String currency, int amt) {
        if(!playerExists(uuid)) {
            ChaosCurrency.log("Error whilst setting currency for uuid "+uuid+", please contact the developer about this error.");
            return;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + table + " SET "+currency+"=? WHERE UUID=?");
            statement.setInt(1, amt);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addCurrency(UUID uuid, String currency, int amt) {
        if(!playerExists(uuid)) {
            ChaosCurrency.log("Error whilst setting currency for uuid "+uuid+", please contact the developer about this error.");
            return;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + table + " SET "+currency+"=? WHERE UUID=?");
            int current = getCurrency(uuid, currency);
            statement.setInt(1, (current+amt));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeCurrency(UUID uuid, String currency, int amt) {
        if(!playerExists(uuid)) {
            ChaosCurrency.log("Error whilst setting currency for uuid "+uuid+", please contact the developer about this error.");
            return;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + table + " SET "+currency+"=? WHERE UUID=?");
            int current = getCurrency(uuid, currency);
            statement.setInt(1, (current-amt));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getCurrency(UUID uuid, String currency) {
        if(!playerExists(uuid)) {
            ChaosCurrency.log("Error whilst setting currency for uuid "+uuid+", please contact the developer about this error.");
            return 0;
        }
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt(currency);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void deleteTable() {
        try {
            plugin.getConnection().prepareStatement("DROP TABLE IF EXISTS " + table).executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

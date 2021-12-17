package me.frxq15.chaoscurrency;

import me.frxq15.chaoscurrency.SQLManager.SQLListeners;
import me.frxq15.chaoscurrency.SQLManager.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ChaosCurrency extends JavaPlugin {
    private static ChaosCurrency instance;
    private Connection connection;
    public String host, database, username, password;
    public int port;
    public SQLManager sqlManager;

    @Override
    public void onEnable() {
        instance = this;
        sqlManager = new SQLManager();
        SQLSetup();
        Bukkit.getPluginManager().registerEvents(new SQLListeners(this), this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static ChaosCurrency getInstance() { return instance;}
    public static String formatMsg(String input) { return ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString(input));}
    public static String colourize(String input) { return ChatColor.translateAlternateColorCodes('&', input);}
    public static void log(String input) { Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[ChaosCurrency] "+input);}
    public SQLManager getSqlManager() { return sqlManager; }
    public void SQLSetup() {
        host = getInstance().getConfig().getString("DATABASE." + "HOST");
        port = getInstance().getConfig().getInt("DATABASE." + "PORT");
        database = getInstance().getConfig().getString("DATABASE." + "DATABASE");
        username = getInstance().getConfig().getString("DATABASE." + "USERNAME");
        password = getInstance().getConfig().getString("DATABASE." + "PASSWORD");

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password="
                        + password));
                log("MySQL Connected successfully.");

            }

        }catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            log("Please setup your MySQL database in the config.yml.");
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

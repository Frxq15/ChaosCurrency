package me.frxq15.chaoscurrency.SQLManager;

import me.frxq15.chaoscurrency.ChaosCurrency;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class SQLListeners implements Listener {
    private final ChaosCurrency plugin;

    public SQLListeners(ChaosCurrency plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        plugin.getSqlManager().createPlayer(uuid, name);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqlManager().updatePlayerName(event.getPlayer()));
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                UUID uuid = event.getPlayer().getUniqueId();
                PlayerData playerData = PlayerData.getPlayerData(plugin, uuid);
                playerData.uploadPlayerData(plugin);
                PlayerData.removePlayerData(uuid);
            });
        }
}

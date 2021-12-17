package me.frxq15.chaoscurrency.SQLManager;

import me.frxq15.chaoscurrency.ChaosCurrency;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final static Map<UUID, PlayerData> players = new HashMap<>();

    private final UUID uuid;
    private int gold = 0;
    private int silver = 0;
    private int bronze = 0;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        players.put(uuid, this);
    }

    public void setGold(int amount) { this.gold = amount; }
    public void setSilver(int amount) { this.gold = amount; }
    public void setBronze(int amount) { this.gold = amount; }
    public int getGold() { return gold; }
    public int getSilver() { return silver; }
    public int getBronze() { return bronze; }
    public void addGold(int amount) { this.gold = (getGold()+amount); }
    public void addSilver(int amount) { this.silver = (getSilver()+amount); }
    public void addBronze(int amount) { this.bronze = (getBronze()+amount); }
    public void takeGold(int amount) { this.gold = (getGold()-amount); }
    public void takeSilver(int amount) { this.silver = (getSilver()-amount); }
    public void takeBronze(int amount) { this.bronze = (getBronze()-amount); }

    public static PlayerData getPlayerData(ChaosCurrency Main, UUID uuid) {
        if (!players.containsKey(uuid)) {
            PlayerData playerData = new PlayerData(uuid);
            playerData.setGold(Main.getSqlManager().getCurrency(uuid, "gold"));
            playerData.setSilver(Main.getSqlManager().getCurrency(uuid, "silver"));
            playerData.setBronze(Main.getSqlManager().getCurrency(uuid, "bronze"));
        }
        return players.get(uuid);
    }
    public static void removePlayerData(UUID uuid) { players.remove(uuid); }
    public static Map<UUID, PlayerData> getAllPlayerData() {
        return players;
    }
    public void uploadPlayerData(ChaosCurrency Main) {
        Bukkit.getScheduler().runTaskAsynchronously(Main, () -> Main.getSqlManager().setCurrency(uuid, "gold", gold));
        Bukkit.getScheduler().runTaskAsynchronously(Main, () -> Main.getSqlManager().setCurrency(uuid, "silver", silver));
        Bukkit.getScheduler().runTaskAsynchronously(Main, () -> Main.getSqlManager().setCurrency(uuid, "silver", bronze));
    }
}

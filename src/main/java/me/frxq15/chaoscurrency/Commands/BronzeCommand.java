package me.frxq15.chaoscurrency.Commands;
import me.frxq15.chaoscurrency.ChaosCurrency;
import me.frxq15.chaoscurrency.SQLManager.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BronzeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        ChaosCurrency plugin = ChaosCurrency.getInstance();
        if(!(sender instanceof Player)) {
            ChaosCurrency.log("This command cannot be executed from console.");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("chaosucurrency.bronze")) {
            p.sendMessage(ChaosCurrency.formatMsg("NO_PERMISSION"));
            return true;
        }
        if(strings.length == 0) {
            PlayerData pd = PlayerData.getPlayerData(plugin, p.getUniqueId());
            p.sendMessage(ChaosCurrency.formatMsg("BRONZE_BALANCE").replace("%amount%", pd.getGold()+""));
            return true;
        }
        if(strings.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);
            if(!target.isOnline()) {
                if(!ChaosCurrency.getInstance().getSqlManager().playerExists(target.getUniqueId())) {
                    p.sendMessage(ChaosCurrency.formatMsg("PLAYER_NOT_FOUND"));
                    return true;
                }
                PlayerData pd = new PlayerData(target.getUniqueId());
                p.sendMessage(ChaosCurrency.formatMsg("BRONZE_BALANCE_OTHER").replace("%amount%", pd.getBronze()+"").replace("%player%", target.getName()));
                pd.removePlayerData(target.getUniqueId());
                return true;
            }
            PlayerData pd = PlayerData.getPlayerData(plugin, target.getUniqueId());
            p.sendMessage(ChaosCurrency.formatMsg("BRONZE_BALANCE_OTHER").replace("%amount%", pd.getBronze()+"").replace("%player%", target.getName()));
            return true;
        }
        p.sendMessage(ChaosCurrency.colourize("&cUsage: /bronze <player>"));
        return true;
    }
}

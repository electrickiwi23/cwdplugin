package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class DebugCommand implements CommandExecutor {

    private final Main plugin;

    public DebugCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("test").setExecutor(this);
    }

    Map<String, Integer> playerMana = new HashMap<String, Integer>();
    Map<String, Integer> manaCounter = new HashMap<String, Integer>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }

        Player p = (Player) sender;
        World world = Bukkit.getServer().getWorld("world");

        for (Player player : Bukkit.getOnlinePlayers()) {
            String uuid = player.getUniqueId().toString();
            playerMana.put(uuid, null);
            manaCounter.put(uuid, null);
        }
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (String player : playerMana.keySet()) {
                    if (playerMana.get(player)<10){
                        manaCounter.replace(player, manaCounter.get(player)+1);
                        if (manaCounter.get(player)>=6) {
                            playerMana.replace(player, playerMana.get(player) + 1);
                            Bukkit.getPlayer(UUID.fromString(player)).setLevel(playerMana.get(player));
                            manaCounter.replace(player, 0);
                        }
                    }
                }
            }
        },0,10);



        return false;
    }
}

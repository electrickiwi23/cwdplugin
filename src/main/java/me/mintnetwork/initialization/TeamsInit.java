//package me.mintnetwork.initialization;
//
//import me.mintnetwork.Main;
//import me.mintnetwork.initialization.ScoreboardInit;
//import org.bukkit.Bukkit;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//import java.lang.reflect.Array;
//import java.util.*;
//
//public class TeamsInit implements CommandExecutor {
//
//    private final Main plugin;
//
//    public TeamsInit(Main plugin) {
//        this.plugin = plugin;
//        plugin.getCommand("assign").setExecutor(this);
//    }
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage("Only players may execute this command!");
//            return true;
//        }
//
//        Player p = (Player) sender;
//
//
//        if (p.hasPermission("cwd.assign")) {
//
//            ScoreboardInit.getScoreboard();
//
//            Random random = new Random();
//
//            String team;
//            // if both teams have the same amount of players....
//            if (red.getPlayers().size() == blue.getPlayers().size()) {
//                // add to a random team
//                if (random.nextBoolean()) {
//                    // add to red
//                    red.addPlayer(p);
//                    team = "&c&lred";
//                } else {
//                    // add to blue
//                    blue.addPlayer(p);
//                    team = "&b&lblue";
//                }
//            } else {
//                // add to the team with the smaller amount of players
//                if (red.getPlayers().size() < blue.getPlayers().size()) {
//                    // add to red
//                    red.addPlayer(p);
//                    team = "&c&lred";
//                } else {
//                    // add to blue
//                    blue.addPlayer(p);
//                    team = "&b&lblue";
//                }
//            }
//            p.sendMessage(ChatColor.translateAlternateColorCodes('&', status + "&cYou have joined the " + team + " &ateam."));
//
//
//        }
//
//        return false;
//    }
//}

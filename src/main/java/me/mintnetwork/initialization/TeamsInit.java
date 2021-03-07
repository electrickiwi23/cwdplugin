package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class TeamsInit implements CommandExecutor {

    private final Main plugin;

    public TeamsInit(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("assign").setExecutor(this);
    }

    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }

        Player p = (Player) sender;

        System.out.println("first");

        if (p.hasPermission("cwd.assign")) {

            System.out.println("second");



            // Initialization of teams
            Team red = board.registerNewTeam("Red");
            Team blue = board.registerNewTeam("Blue");
            Team yellow = board.registerNewTeam("Yellow");
            Team green = board.registerNewTeam("Green");

            // Setting player prefixes and team characteristics
            red.setPrefix(ChatColor.RED + "[RED] " + ChatColor.WHITE);
            blue.setPrefix(ChatColor.DARK_BLUE + "[BLUE] " + ChatColor.WHITE);
            yellow.setPrefix(ChatColor.YELLOW + "[YELLOW] " + ChatColor.WHITE);
            green.setPrefix(ChatColor.GREEN + "[GREEN] " + ChatColor.WHITE);

            red.setAllowFriendlyFire(false);
            blue.setAllowFriendlyFire(false);
            yellow.setAllowFriendlyFire(false);
            green.setAllowFriendlyFire(false);


            if (args[0].equalsIgnoreCase("rb")) {
                System.out.println("third");
                int count = 0;
                for (Player online : Bukkit.getOnlinePlayers()) {
                    count++;
                    if (count % 2 == 0) {
                        System.out.println("fifth");
                        red.addEntry(online.getDisplayName());
                    } else {
                        System.out.println("sixth");
                        blue.addEntry(online.getDisplayName());
                    }
                }
            } else if (args[0].equalsIgnoreCase("rbyg")) {
                System.out.println("fourth");
                int count = 0;
                for (Player online : Bukkit.getOnlinePlayers()) {
                    count++;
                    switch(count % 4) {
                        case(1):
                            red.addEntry(online.getName());
                            break;
                        case(2):
                            blue.addEntry(online.getName());
                            break;
                        case(3):
                            yellow.addEntry(online.getName());
                            break;
                        case(0):
                            green.addEntry(online.getName());
                            break;
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "Invalid use of command, correct usage: " + ChatColor.GREEN + "/assign rb " + ChatColor.RED + "or " + ChatColor.GREEN + "/assign rbyg");
            }
            p.setScoreboard(board);
        }
        return false;
    }

    public Team getTeam(Entity entity) {
        for (Team team : board.getTeams()) {
            if (team.hasEntry(String.valueOf(entity))) {
                return team;
            }
        }
        return null;
    }
}

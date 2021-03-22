package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

public class TeamsInit implements Listener {

    private final Main plugin;

    public TeamsInit(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

//  Individual Scoreboard Start ----------------------------------------------------------------------------------------

//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent e){
//        Scoreboard dummy = createScoreboard(e.getPlayer(), 'x');
//        updateScoreboard();
//    }

//    @EventHandler
//    public void onPlayerQuit(PlayerQuitEvent e){
//        updateScoreboard();
//    }

//    public static Scoreboard getScoreboard(Player player){
//        return createScoreboard(player, 'x');
//    }

//    public static Scoreboard addToTeam(Player player, char color){
//        return createScoreboard(player, color);
//    }

//    Temporary placeholders and setting the scoreboard
//      Objective objective = board.registerNewObjective("Stats", "dummy", "Stats");
//      objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//      Score score = objective.getScore("players:");
//      score.setScore(Bukkit.getOnlinePlayers().size());

//  Individual Scoreboard Pause ----------------------------------------------------------------------------------------

    public static void initializeTeams(int teamAmount) {

        System.out.println("second");

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        for (Team team : board.getTeams()) {
            System.out.println("two and half");
            team.unregister();
        }

        System.out.println("third");

        // Initialization of teams
        board.registerNewTeam("red");
        board.registerNewTeam("blue");
        board.registerNewTeam("yellow");
        board.registerNewTeam("green");

        System.out.println("fourth");

        // Setting player prefixes and team characteristics
        board.getTeam("red").setPrefix(ChatColor.RED + "[RED] " + ChatColor.WHITE);
        board.getTeam("blue").setPrefix(ChatColor.BLUE + "[BLUE] " + ChatColor.WHITE);
        board.getTeam("yellow").setPrefix(ChatColor.YELLOW + "[YELLOW] " + ChatColor.WHITE);
        board.getTeam("green").setPrefix(ChatColor.GREEN + "[GREEN] " + ChatColor.WHITE);

        board.getTeam("red").setColor(ChatColor.RED);
        board.getTeam("blue").setColor(ChatColor.BLUE);
        board.getTeam("yellow").setColor(ChatColor.YELLOW);
        board.getTeam("green").setColor(ChatColor.GREEN);

        board.getTeam("red").setAllowFriendlyFire(false);
        board.getTeam("blue").setAllowFriendlyFire(false);
        board.getTeam("yellow").setAllowFriendlyFire(false);
        board.getTeam("green").setAllowFriendlyFire(false);

        System.out.println("fifth");

        if (teamAmount == 2) {
            System.out.println("sixth");
            int count = 0;
            for (Player online : Bukkit.getOnlinePlayers()) {
                count++;
                if (count % 2 == 0) {
                    System.out.println("eighth");
                    board.getTeam("red").addEntry(online.getName());
                } else {
                    System.out.println("ninth");
                    board.getTeam("blue").addEntry(online.getName());
                }
            }
        } else if (teamAmount == 4) {
            System.out.println("seventh");
            int count = 0;
            for (Player online : Bukkit.getOnlinePlayers()) {
                count++;
                switch (count % 4) {
                    case (1):
                        System.out.println("tenth");
                        board.getTeam("red").addEntry(online.getName());
                        break;
                    case (2):
                        board.getTeam("blue").addEntry(online.getName());
                        break;
                    case (3):
                        board.getTeam("yellow").addEntry(online.getName());
                        break;
                    case (0):
                        board.getTeam("green").addEntry(online.getName());
                        break;
                }
            }
        }
    }

//  Individual Scoreboard Resume ---------------------------------------------------------------------------------------
//    public void updateScoreboard() {
//        for (Player online : Bukkit.getOnlinePlayers()){
//            Score score = online.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("players:");
//            score.setScore(Bukkit.getOnlinePlayers().size());
//        }
//    }
//  Individual Scoreboard End -----------------------------------------------------------------------------------------

}

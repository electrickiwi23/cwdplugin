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

public class ScoreboardInit implements Listener {

    private final Main plugin;

    public ScoreboardInit(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Scoreboard dummy = createScoreboard(e.getPlayer());
        updateScoreboard();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        updateScoreboard();
    }

    public Scoreboard getScoreboard(Player player){
        return createScoreboard(player);
    }

    public Scoreboard createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        // Temporary placeholders and setting the scoreboard
        Objective objective = board.registerNewObjective("Stats", "dummy", "Stats");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore("players:");
        score.setScore(Bukkit.getOnlinePlayers().size());

        // Initialization of teams
        Team red = board.registerNewTeam("Red");
        Team blue = board.registerNewTeam("Blue");
        Team yellow = board.registerNewTeam("Yellow");
        Team green = board.registerNewTeam("Green");

        // Setting player prefixes and team characteristics
        red.setPrefix(ChatColor.RED + "[RED]" + ChatColor.WHITE);
        blue.setPrefix(ChatColor.DARK_BLUE + "[BLUE]" + ChatColor.WHITE);
        yellow.setPrefix(ChatColor.RED + "[YELLOW]" + ChatColor.WHITE);
        green.setPrefix(ChatColor.DARK_BLUE + "[GREEN]" + ChatColor.WHITE);

        red.setAllowFriendlyFire(false);
        blue.setAllowFriendlyFire(false);
        yellow.setAllowFriendlyFire(false);
        green.setAllowFriendlyFire(false);

        // Cleanup
        player.setScoreboard(board);
        return board;
    }

    public void updateScoreboard() {
        for (Player online : Bukkit.getOnlinePlayers()){
            Score score = online.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore("players:");
            score.setScore(Bukkit.getOnlinePlayers().size());
        }
    }
}

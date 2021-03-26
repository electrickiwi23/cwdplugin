package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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


        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        for (Team team : board.getTeams()) {
            team.unregister();
        }


        // Initialization of teams
        board.registerNewTeam("red");
        board.registerNewTeam("blue");
        board.registerNewTeam("yellow");
        board.registerNewTeam("green");


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


        if (teamAmount == 2) {

            int count = 0;
            for (Player online : Bukkit.getOnlinePlayers()) {
                count++;
                if (count % 2 == 0) {
                    board.getTeam("red").addEntry(online.getName());
                } else {
                    board.getTeam("blue").addEntry(online.getName());
                }
                updateArmor(online);
            }
        } else if (teamAmount == 4) {
            System.out.println("seventh");
            int count = 0;
            ArrayList<Player> onlinePlayerList = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayerList);
            for (Player online : onlinePlayerList) {
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
                updateArmor(online);
            }
        }

    }

    public static Team getTeam(Entity e){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        for (Team team: manager.getMainScoreboard().getTeams()) {
            if (team.hasEntry(e.getName())){
                return team;
            }
        }
        return null;
    }

    public static String getTeamName(Entity e){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        for (Team team: manager.getMainScoreboard().getTeams()) {
            if (team.hasEntry(e.getName())){
                return team.getName();
            }
        }
        return "";
    }

    public static void updateArmor(Player p) {
        String teamName = getTeamName(p);
        for (ItemStack i : p.getInventory().getArmorContents()) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) i.getItemMeta();
            switch (teamName) {
                case ("blue"):
                    armorMeta.setColor(Color.fromRGB(60, 68, 170));
                    break;
                case ("red"):
                    armorMeta.setColor(Color.fromRGB(176, 46, 38));
                    break;
                case ("green"):
                    armorMeta.setColor(Color.fromRGB(128, 199, 31));
                    break;
                case ("yellow"):
                    armorMeta.setColor(Color.fromRGB(120, 120, 2));
                    break;
            }
            i.setItemMeta(armorMeta);
        }
    }

    public static void addToTeam(Entity e,String team){
        System.out.println("adding to team " + team);
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        if (team!=null) {
            if (e instanceof Player) {
                board.getTeam(team).addEntry(e.getName());
            } else {
                board.getTeam(team).addEntry(String.valueOf(e.getUniqueId()));
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

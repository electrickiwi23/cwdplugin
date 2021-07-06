package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import me.mintnetwork.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

public class ScoreboardInit {


    public static Main plugin;
    public static String timer;
    public static Objective sidebar;
    static HashMap<Team, Integer> points = new HashMap<>();
    static HashMap<Team, String> pointsScore = new HashMap<>();

    public static void clearScoreboards(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        board.clearSlot(DisplaySlot.SIDEBAR);
        board.clearSlot(DisplaySlot.BELOW_NAME);
        board.clearSlot(DisplaySlot.PLAYER_LIST);

    }

    public static void InitLives(int lifeCount){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        if (board.getObjective("lives")!=null) board.getObjective("lives").unregister();

        Objective lives = board.registerNewObjective("lives","dummy", "Lives");

        for (Player p:Bukkit.getOnlinePlayers()) {
            lives.getScore(p.getDisplayName()).setScore(lifeCount);

        }

        lives.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    public static void InitPoints(String string, String time, Main p){
        plugin=p;
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        points.clear();
        pointsScore.clear();

        if (board.getObjective("sidebar")!=null) board.getObjective("sidebar").unregister();

        sidebar = board.registerNewObjective("sidebar","dummy", Utils.chat("&l " + string));

        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        timer = time;


        for (Team team:TeamsInit.currentTeams) {
            points.put(team,0);
        }


        if (GameStart.gameMode.equals("flares")) {
            sidebar.getScore("          " + timer).setScore(9);
            sidebar.getScore("                   ").setScore(8);
            sidebar.getScore("                 ").setScore(4);
            int counter = 3;
            for (Team team: points.keySet()) {
                sidebar.getScore(team.getColor() + "       " + team.getDisplayName() + ": " + points.get(team)).setScore(counter);
                pointsScore.put(team,team.getColor() + "       " + team.getDisplayName() + ": " + points.get(team));
                counter--;
            }
            sidebar.getScore("              ").setScore(counter);


        } else {
            sidebar.getScore("          " + timer).setScore(9);
            sidebar.getScore("                   ").setScore(8);
            int counter = 7;
            for (Team team: points.keySet()) {
                sidebar.getScore(team.getColor() + "       " + team.getDisplayName() + ": " + points.get(team)).setScore(counter);
                pointsScore.put(team,team.getColor() + "       " + team.getDisplayName() + ": " + points.get(team));
                counter--;
            }
            sidebar.getScore("              ").setScore(counter);
        }




    }


    public static void AwardPoints(Team team,int amount) {

        String pointScore = pointsScore.get(team);

        points.replace(team,points.get(team)+amount);

        int score = sidebar.getScore(pointScore).getScore();
        sidebar.getScoreboard().resetScores(pointScore);
        sidebar.getScore(team.getColor() + "       " + team.getDisplayName() + ": " + points.get(team)).setScore(score);
        pointsScore.put(team,team.getColor() + "       " + team.getDisplayName() + ": " + points.get(team));

    }




}

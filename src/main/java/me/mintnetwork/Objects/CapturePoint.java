package me.mintnetwork.Objects;

import me.mintnetwork.initialization.ScoreboardInit;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.eclipse.sisu.Nullable;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class CapturePoint {

    private static Plugin plugin;

    public static void Shutdown(){
        for (CapturePoint capturePoint : capturePoints) {
            capturePoint.bossBar.removeAll();
            capturePoint.cloud.remove();
            capturePoint.tick.cancel();
        }
    }

    public static void CreateHill(World world, Plugin p) {
        plugin = p;
        FileConfiguration config = p.getConfig();
        new CapturePoint(world,config.getDouble("FlareBx"),config.getDouble("FlareBy"),config.getDouble("FlareBz"), 7,null,true);
    }

    public static void CreateFlares(World world, Plugin p){
        plugin = p;
        FileConfiguration config = p.getConfig();
        new CapturePoint(world,config.getDouble("FlareAx"),config.getDouble("FlareAy"),config.getDouble("FlareAz"), 5,'a',false);
        new CapturePoint(world,config.getDouble("FlareBx"),config.getDouble("FlareBy"),config.getDouble("FlareBz"), 5,'b',false);
        new CapturePoint(world,config.getDouble("FlareCx"),config.getDouble("FlareCy"),config.getDouble("FlareCz"), 5,'c',false);


    }

    String AScore;
    String BScore;
    String CScore;


    public static ArrayList<CapturePoint> capturePoints = new ArrayList<>();

    public BukkitTask tick;
    public Location location;
    AreaEffectCloud cloud;
    int capture;
    float radius;
    String capturingTeam = null;
    String  currentTeam = null;
    public BossBar bossBar;

    public CapturePoint(World world, double x, double y, double z, float r, @Nullable Character letter, boolean isHill){

        if (letter==null){
            bossBar = Bukkit.createBossBar(ChatColor.BOLD + ("Objective"), BarColor.WHITE, BarStyle.SOLID);

        } else bossBar = Bukkit.createBossBar(ChatColor.BOLD + ("Objective " + Character.toUpperCase(letter)), BarColor.WHITE, BarStyle.SOLID);
        bossBar.setProgress(0);

        radius = r;

        if (letter!=null){
            String string;

            switch (letter){
                case ('a'):
                    string = ChatColor.GRAY + "§l   A:□□□□□□□□□□";
                    AScore = string;
                    ScoreboardInit.sidebar.getScore(string).setScore(7);
                    break;
                case ('b'):
                    string = ChatColor.GRAY + "§l   B:□□□□□□□□□□";
                    BScore = string;
                    ScoreboardInit.sidebar.getScore(string).setScore(6);
                    break;
                case ('c'):
                    string = ChatColor.GRAY + "§l   C:□□□□□□□□□□";
                    CScore = string;
                    ScoreboardInit.sidebar.getScore(string).setScore(5);
                    break;
            }
        }



        location = new Location(world,x,y,z);
        capturePoints.add(this);

        cloud = (AreaEffectCloud) world.spawnEntity(location, EntityType.AREA_EFFECT_CLOUD);
        cloud.setRadius(radius);
        cloud.setDuration(1000000);
        cloud.setParticle(Particle.REDSTONE,new Particle.DustOptions(Color.GRAY,1));


        tick = new BukkitRunnable() {
            @Override
            public void run() {

                if (letter!=null){
                    StringBuilder first = new StringBuilder();
                    StringBuilder second = new StringBuilder();
                    String string;
                    int num = (int) Math.ceil(10*(capture/200.0));
                    for (int i = 0; i < num; i++) {
                        first.append("■");
                    }
                    for (int i = 0; i < 10 - num; i++) {
                        second.append("□");
                    }
                    if (currentTeam!=null){
                        string = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(currentTeam).getColor()+first.toString()+ second;
                    } else if (capturingTeam!=null) {
                        string = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(capturingTeam).getColor()+first.toString() + "§7" + second;
                    } else {
                        string = "§7" +first + second;

                    }




                    int score;
                    String color = ChatColor.WHITE.toString();
                    if (currentTeam!=null){
                        color = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(currentTeam).getColor().toString();
                    }


                    switch (letter){
                        case ('a'):
                            score = ScoreboardInit.sidebar.getScore(AScore).getScore();
                            ScoreboardInit.sidebar.getScoreboard().resetScores(AScore);
                            ScoreboardInit.sidebar.getScore( color + "§l   A:" + string).setScore(score);
                            AScore = color + "§l   A:" + string;
                            break;
                        case ('b'):
                            score = ScoreboardInit.sidebar.getScore(BScore).getScore();
                            ScoreboardInit.sidebar.getScoreboard().resetScores(BScore);
                            ScoreboardInit.sidebar.getScore(color + "§l   B:" + string).setScore(score);
                            BScore = color + "§l   B:" + string;
                            break;

                        case ('c'):
                            score = ScoreboardInit.sidebar.getScore(CScore).getScore();
                            ScoreboardInit.sidebar.getScoreboard().resetScores(CScore);
                            ScoreboardInit.sidebar.getScore(color + "§l   C:" + string).setScore(score);
                            CScore = color + "§l   C:" + string;
                            break;
                    }
                }

                HashMap<String,Integer> playersPerTeam = new HashMap<>();

                for (Player p:Bukkit.getOnlinePlayers()) {
                    if (isHill) bossBar.addPlayer(p);

                    boolean isIn = false;

                    if (p.getLocation().distance(location)<=radius){
                        if (!p.isDead()) {
                            if (TeamsInit.getTeam(p) != null) {
                                if (playersPerTeam.containsKey(TeamsInit.getTeamName(p))) {
                                    playersPerTeam.replace(TeamsInit.getTeamName(p), playersPerTeam.get(TeamsInit.getTeamName(p)) + 1);
                                } else {
                                    playersPerTeam.put(TeamsInit.getTeamName(p), +1);

                                }
                                isIn = true;

                                if (!isHill) bossBar.addPlayer(p);
                            }
                        }
                    }
                    if (!isIn){
                        if (!isHill) bossBar.removePlayer(p);
                    }
                }
                //code for when it's being captured and not contested
                if (playersPerTeam.size()==1) {
                    for (String team: playersPerTeam.keySet()) {
                        //going back to fully captured
                        if (team.equals(currentTeam)){
                            capture = Math.min(200,capture + 1 + 2 * playersPerTeam.get(team));
                            if (capture == 200) capturingTeam = null;
                        //code for decapturing an objective
                        } else if (currentTeam != null){
                            capture = Math.max(0, capture - 2 * playersPerTeam.get(team));;
                            if (capture == 0) {
                                decapture();
                            }
                        //code for decaping a middle capture objective
                        } else if (capturingTeam != null && !capturingTeam.equals(team)){
                            capture = Math.max(0, capture - 2 * playersPerTeam.get(team));
                            if (capture == 0) {
                               capturingTeam = null;
                            }
                        //code for entering an empty objective
                        }  else if (capturingTeam==null){
                            capturingTeam = team;
                            capture = capture + 2 * playersPerTeam.get(team);
                        //code for continuing to capture an objective
                        } else {
                            capture = Math.min(200,capture + 2 * playersPerTeam.get(team));
                            if (capture == 200) capture(team);
                        }
                    }
                    //code for when the site is left alone
                } else if (playersPerTeam.size()==0){
                        //going back to fully captured
                        if (currentTeam != null) {
                            capture = Math.min(200, capture + 1);
                            if (capture == 200) capturingTeam = null;
                        //going back to not at all captured
                        } else {
                            capture = Math.max(0, capture - 1);
                            if (capture == 0) capturingTeam = null;
                        }
                }

                if (currentTeam==null){
                  bossBar.setTitle("§7" + ChatColor.stripColor(bossBar.getTitle()));
                } else {
                    bossBar.setTitle(getTeam().getColor() + ChatColor.stripColor(bossBar.getTitle()));
                }

                bossBar.setProgress(capture/200.0);


            }
        }.runTaskTimer(plugin,1,2);







    }

    public Team getTeam(){
        if (currentTeam==null){
            return null;
        } else {
            return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(currentTeam);
        }
    }

    private void decapture(){
        currentTeam = null;
        cloud.setParticle(Particle.REDSTONE,new Particle.DustOptions(Color.GRAY,1));
        bossBar.setColor(BarColor.WHITE);
    }

    private void capture(String team){
        currentTeam = team;
        Color color = Color.GRAY;
        if (team!=null){
            switch (currentTeam){
                case ("red"):
                    color = Color.RED;
                    bossBar.setColor(BarColor.RED);
                    break;
                case ("blue"):
                    color = Color.BLUE;
                    bossBar.setColor(BarColor.BLUE);
                    break;
                case ("yellow"):
                    color = Color.YELLOW;
                    bossBar.setColor(BarColor.YELLOW);
                    break;
                case ("green"):
                    color = Color.fromBGR(0, 255, 0);
                    bossBar.setColor(BarColor.GREEN);
                    break;
            }
            cloud.setParticle(Particle.REDSTONE,new Particle.DustOptions(color,1));
        }
    }
}

package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.CapturePoint;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class GameStart {

    public static boolean gameRunning = false;
    public static GameMode gameMode = GameMode.SKIRMISH;
    public static boolean hasStarted = false;
    public static int timer = 0;
    public static World world;

    public static void SetGlass(World world){
        if (world.getName().equals("woodlands")) {

            Location[] spawns = new Location[]{new Location(world, 148.5, 68.5, 32.5, 90, 0),
                    new Location(world, 5.5, 68.5, -110.5, 0, 0),
                    new Location(world, -137.5, 68.5, 32.5, -90, 0),
                    new Location(world, 5.5, 68.5, 175.5, 180, 0)};

            for (Location location : spawns) {
                Material mat = Material.GLASS;

                switch ((int) location.getYaw()){
                    case 90:
                        mat = Material.BLUE_STAINED_GLASS;
                        break;
                    case 0:
                        mat = Material.LIME_STAINED_GLASS;
                        break;
                    case (-90):
                        mat = Material.RED_STAINED_GLASS;
                        break;
                    case (180):
                        mat = Material.YELLOW_STAINED_GLASS;
                        break;



                }


                Location window = location.clone().add(location.clone().getDirection().multiply(11));
                for (int i = -1; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        Location current = window.clone();
                        current.add(location.getDirection().rotateAroundY(Math.toRadians(-90)).multiply(i));
                        current.add(0, j, 0);
                        if (current.getBlock().getType()==Material.AIR) {
                            current.getBlock().setType(mat);
                        }
                    }
                }
                window = location.clone().add(location.clone().getDirection().normalize().multiply(8));
                window.add(location.clone().getDirection().rotateAroundY(Math.toRadians(-90)).normalize().multiply(8));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Location current = window.clone();
                        current.add(location.clone().getDirection().rotateAroundY(Math.toRadians(-90)).normalize().multiply(i));
                        current.add(0, j, 0);
                        if (current.getBlock().getType()==Material.AIR) {
                            current.getBlock().setType(mat);
                        }
                    }
                }
                window = location.clone().add(location.getDirection().normalize().multiply(8));
                window.add(location.getDirection().rotateAroundY(Math.toRadians(90)).normalize().multiply(8));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Location current = window.clone();
                        current.add(location.clone().getDirection().rotateAroundY(Math.toRadians(90)).normalize().multiply(i));
                        current.add(0, j, 0);
                        if (current.getBlock().getType()==Material.AIR) {
                            current.getBlock().setType(mat);
                        }
                    }
                }
            }
        }else if(world.getName().equals("oasis")){
            Location[] spawns = new Location[]{new Location(world,67.5,63.5,8.5,0,0),
                    new Location(world,67.5,63.5,240.5,180,0)};
            for (Location location:spawns) {
                Material mat = Material.GLASS;

                switch ((int) location.getYaw()){
                    case 0:
                        mat = Material.BLUE_STAINED_GLASS;
                        break;
                    case (180):
                        mat = Material.RED_STAINED_GLASS;
                        break;
                }

                Location window = location.clone().add(location.clone().getDirection().multiply(25)).add(0,-1,0);
                for (int i = -2; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        Location current = window.clone();
                        current.add(location.getDirection().rotateAroundY(Math.toRadians(-90)).multiply(i));
                        current.add(0, j, 0);
                        if (current.getBlock().getType()==Material.AIR) {
                            current.getBlock().setType(mat);
                        }
                    }
                }
                window = location.clone().add(location.clone().getDirection().normalize().multiply(20));
                window.add(location.clone().getDirection().rotateAroundY(Math.toRadians(-90)).normalize().multiply(15));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 6; j++) {
                        Location current = window.clone();
                        current.add(location.clone().getDirection().rotateAroundY(Math.toRadians(-90)).normalize().multiply(i));
                        current.add(0, j, 0);
                        if (current.getBlock().getType()==Material.AIR) {
                            current.getBlock().setType(mat);
                        }
                    }
                }
                window = location.clone().add(location.getDirection().normalize().multiply(20));
                window.add(location.getDirection().rotateAroundY(Math.toRadians(90)).normalize().multiply(15));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 6; j++) {
                        Location current = window.clone();
                        current.add(location.clone().getDirection().rotateAroundY(Math.toRadians(90)).normalize().multiply(i));
                        current.add(0, j, 0);
                        if (current.getBlock().getType()==Material.AIR) {
                            current.getBlock().setType(mat);
                        }
                    }
                }
            }
        }
    }

    public static void ClearGlass(World world){
        System.out.println(world.getName());
        ArrayList<Material> glass = new ArrayList<>(Arrays.asList(Material.LIME_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS));
        if (world.getName().equals("woodlands")){

            Location[] spawns = new Location[]{new Location(world,148.5,68.5,32.5,90,0),
            new Location(world,5.5,68.5,-110.5,0,0),
            new Location(world,-137.5,68.5,32.5,-90,0),
            new Location(world,5.5,68.5,175.5,180,0)};

            for (Location location:spawns) {
                Location window = location.clone().add(location.clone().getDirection().multiply(11));
                for (int i = -1; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        Location current = window.clone();
                        current.add(location.getDirection().rotateAroundY(Math.toRadians(-90)).multiply(i));
                        current.add(0, j, 0);
                        if (glass.contains(current.getBlock().getType())) {
                            current.getBlock().breakNaturally();
                        }
                    }
                }
                window = location.clone().add(location.clone().getDirection().normalize().multiply(8));
                window.add(location.clone().getDirection().rotateAroundY(Math.toRadians(-90)).normalize().multiply(8));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Location current = window.clone();
                        current.add(location.clone().getDirection().rotateAroundY(Math.toRadians(-90)).normalize().multiply(i));
                        current.add(0, j, 0);
                        if (glass.contains(current.getBlock().getType())) {
                            current.getBlock().breakNaturally();
                        }
                    }
                }
                window = location.clone().add(location.getDirection().normalize().multiply(8));
                window.add(location.getDirection().rotateAroundY(Math.toRadians(90)).normalize().multiply(8));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Location current = window.clone();
                        current.add(location.clone().getDirection().rotateAroundY(Math.toRadians(90)).normalize().multiply(i));
                        current.add(0, j, 0);
                        if (glass.contains(current.getBlock().getType())) {
                            current.getBlock().breakNaturally();
                        }
                    }
                }
            }
        } else if(world.getName().equals("oasis")){

            Location[] spawns = new Location[]{new Location(world,67.5,63.5,8.5,0,0),
                    new Location(world,67.5,63.5,240.5,180,0)};
            for (Location location:spawns) {
                Location window = location.clone().add(location.clone().getDirection().multiply(25)).add(0,-1,0);
                for (int i = -2; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        Location current = window.clone();
                        current.add(location.getDirection().rotateAroundY(Math.toRadians(-90)).multiply(i));
                        current.add(0, j, 0);
                        if (glass.contains(current.getBlock().getType())) {
                            current.getBlock().breakNaturally();
                        }
                    }
                }
                window = location.clone().add(location.clone().getDirection().normalize().multiply(20));
                window.add(location.clone().getDirection().rotateAroundY(Math.toRadians(-90)).normalize().multiply(15));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 6; j++) {
                        Location current = window.clone();
                        current.add(location.clone().getDirection().rotateAroundY(Math.toRadians(-90)).normalize().multiply(i));
                        current.add(0, j, 0);
                        if (glass.contains(current.getBlock().getType())) {
                            current.getBlock().breakNaturally();
                        }
                    }
                }
                window = location.clone().add(location.getDirection().normalize().multiply(20));
                window.add(location.getDirection().rotateAroundY(Math.toRadians(90)).normalize().multiply(15));
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 6; j++) {
                        Location current = window.clone();
                        current.add(location.clone().getDirection().rotateAroundY(Math.toRadians(90)).normalize().multiply(i));
                        current.add(0, j, 0);
                        if (glass.contains(current.getBlock().getType())) {
                            current.getBlock().breakNaturally();
                        }
                    }
                }
            }
        }
    }

    public static void startCountdown(Main plugin, GameMode gameMode, World world, int additional){
        GameStart.world = world;
        final int[] count = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                count[0]++;
                String message = ChatColor.GREEN + "GO";
                switch (count[0]){
                    case 1:
                        message = ChatColor.RED + "3";
                        break;
                    case 2:
                        message = ChatColor.GOLD + "2";
                        break;
                    case 3:
                        message = ChatColor.YELLOW + "1";
                        break;
                    case 4:
                        switch (gameMode){
                            case SKIRMISH:
                                startGeneric(plugin);
                                break;
                            case FLARES:
                                startFlares(plugin,world,additional);
                                break;
                            case ELIMINATION:
                                startElimination(plugin,additional);
                                break;
                            case KING_OF_THE_HILL:
                                startKoth(plugin,world,additional);
                                break;
                            case BATTLE_ROYAL:
                                startBR(plugin,world);
                                break;

                        }
                        break;
                }
                for (Player p:Bukkit.getOnlinePlayers()) {
                    p.sendTitle(message,"",0,20,0);
                }
                if (count[0]==4) {
                    this.cancel();
                    ClearGlass(world);
                }
            }
        }.runTaskTimer(plugin,20,20);




    }
    
    public static void startGeneric(Main plugin) {
        if (!hasStarted) {
            // Startup mana generation
            Mana.mana(plugin);
            // Startup ult generation
            Ultimate.ult(plugin);
            hasStarted = true;
        }

        for (UUID uuid: WizardInit.playersWizards.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p!=null) {
                Wizard w = WizardInit.playersWizards.get(uuid);
                w.Mana = 3;
                w.Ult = 0;
                p.setLevel(3);
                TeamsInit.updateArmor(p);
            }
        };

        //stops objectives that are running from previous games
        CapturePoint.Shutdown();
        TeamsInit.refreshTeams();
        ScoreboardInit.clearScoreboards();
        // Set gameRunning to true
        gameRunning = true;

    }
    public static void startBR(Main plugin,World world){
            startGeneric(plugin);
            for (UUID uuid : WizardInit.playersWizards.keySet()) {
                Wizard w = WizardInit.playersWizards.get(uuid);
                w.ElimLives = 1;
            }

            gameMode = GameMode.BATTLE_ROYAL;

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {

                    Bukkit.getServer().getWorld("world");
                    System.out.println(world.getName());
                    world.getWorldBorder().setSize(300);
                    world.getWorldBorder().setSize(45, 120);

                }
            }, 1200);
    }

    public static void startFlares(Main plugin,World world,int time){
        startGeneric(plugin);

        gameMode = GameMode.FLARES;
        ScoreboardInit.InitPoints("Zone Capture", time + ":00",plugin);
        startTimer(time * 60,plugin);
        CapturePoint.CreateFlares(world,plugin);
    }

    public static void startKoth(Main plugin, World world, int i){
        startGeneric(plugin);
        gameMode = GameMode.KING_OF_THE_HILL;

        String string = (i + ":" + "00");

        ScoreboardInit.InitPoints("King of the Hill", string,plugin);
        startTimer(i * 60,plugin);
        CapturePoint.CreateHill(world,plugin);
    }

    public static void startElimination(Main plugin,int lives){
        startGeneric(plugin);
        // Set gameMode
        gameMode = GameMode.ELIMINATION;
        // Set wizard's lives
        for (UUID uuid: WizardInit.playersWizards.keySet()) {
            Wizard w = WizardInit.playersWizards.get(uuid);
            w.ElimLives= lives;
        }
        ScoreboardInit.InitLives(lives);
    }

    public static void endGame(Team winner){
        for (CapturePoint capturePoint : CapturePoint.capturePoints) {
            capturePoint.bossBar.removeAll();
            capturePoint.tick.cancel();
        }

        SetGlass(world);

        for (Player p:Bukkit.getOnlinePlayers()) {
            String str = winner.getDisplayName();
            p.sendTitle(winner.getColor()+ str.substring(0, 1).toUpperCase() + str.substring(1) + " team has won.", "", 10 ,80, 20);
        }

        gameRunning = false;
        gameMode = GameMode.SKIRMISH;
    }

    public static void startTimer(int amount,Main plugin){
        timer = amount;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (timer > 0) {
                    timer--;

                    String mins = String.valueOf(timer % 60);

                    if (mins.length() == 1) mins = "0" + mins;

                    String string = (timer / 60 + ":" + mins);


                    int score = ScoreboardInit.sidebar.getScore("          " + ScoreboardInit.timer).getScore();
                    ScoreboardInit.sidebar.getScoreboard().resetScores("          " + ScoreboardInit.timer);
                    ScoreboardInit.sidebar.getScore("          " + string).setScore(score);
                    ScoreboardInit.timer = string;
                } else {
                    Team winner = TeamsInit.currentTeams.get(0);
                    int currentPoints = ScoreboardInit.points.get(TeamsInit.currentTeams.get(0));
                    for (Team team : ScoreboardInit.points.keySet()) {
                        if (ScoreboardInit.points.get(team) > currentPoints) {
                            currentPoints = ScoreboardInit.points.get(team);
                            winner = team;
                        }
                    }
                    endGame(winner);
                    this.cancel();
                                    }

                for (CapturePoint site : CapturePoint.capturePoints) {
                    if (site.getTeam() != null) {
                        ScoreboardInit.AwardPoints(site.getTeam(), 1);
                    }
                }


            }
        }.runTaskTimer(plugin,0,20);




    }
}

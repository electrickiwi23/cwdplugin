package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.Objects.Wizard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GameStart {

    private final Main plugin;

    public static boolean gameRunning = false;
    public static String gameMode = "skirmish";

    public GameStart(Main plugin) {
        this.plugin = plugin;
    };
    
    public static void startGeneric(Main plugin) {
        // Startup mana generation
        Mana.mana(plugin);
        // Startup ult generation
        Ultimate.ult(plugin);
        for (Player p: WizardInit.playersWizards.keySet()) {
            Wizard w = WizardInit.playersWizards.get(p);
            w.Mana = 3;
            p.setLevel(3);
        }

        // Set gameRunning to true
        gameRunning = true;

    }
    public static void startBR(Main plugin, World world){
        startGeneric(plugin);
        for (Player p: WizardInit.playersWizards.keySet()) {
            Wizard w = WizardInit.playersWizards.get(p);
            w.ElimLives= 1;
        }

        gameMode = "battle royale";

        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {

                Bukkit.getServer().getWorld("world");
                System.out.println(world.getName());
                world.getWorldBorder().setSize(300);
                world.getWorldBorder().setSize(45,120);

            }
        },1200);

    }

    public static void startElimination(Main plugin,int lives){
        startGeneric(plugin);
        // Set gameMode
        gameMode = "elimination";
        // Set wizard's lives
        for (Player p: WizardInit.playersWizards.keySet()) {
            Wizard w = WizardInit.playersWizards.get(p);
            w.ElimLives= lives;
        }
    }
}

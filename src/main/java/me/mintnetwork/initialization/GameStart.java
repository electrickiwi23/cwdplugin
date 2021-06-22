package me.mintnetwork.initialization;

import com.mojang.util.UUIDTypeAdapter;
import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.Objects.Wizard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

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
        for (UUID uuid: WizardInit.playersWizards.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            Wizard w = WizardInit.playersWizards.get(uuid);
            w.Mana = 3;
            p.setLevel(3);
        }

        // Set gameRunning to true
        gameRunning = true;

    }
    public static void startBR(Main plugin, World world){
        startGeneric(plugin);
        for (UUID uuid: WizardInit.playersWizards.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            Wizard w = WizardInit.playersWizards.get(uuid);
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
        for (UUID uuid: WizardInit.playersWizards.keySet()) {
            Wizard w = WizardInit.playersWizards.get(uuid);
            w.ElimLives= lives;
        }
    }
}

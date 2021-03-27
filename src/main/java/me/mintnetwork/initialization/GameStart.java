package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;

public class GameStart {

    private final Main plugin;

    public static boolean gameRunning = false;
    public static String gameMode = "";

    public GameStart(Main plugin) {
        this.plugin = plugin;
    };
    
    public static void start(Main plugin) {
        // Startup mana generation
        Mana.mana(plugin);
        // Startup ult generation
        Ultimate.ult(plugin);
        // Set gamemode and start gamemode
        gameMode = "elimination";
        // Set wizard's lives
        for (Wizard w: WizardInit.playersWizards.values()) {
            w.ElimLives=3;
            w.Mana = 3;
        }
        // Set gameRunning to true
        gameRunning = true;



    }
}

package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;

public class GameStart {

    private final Main plugin;

    public boolean gameRunning = false;

    public GameStart(Main plugin) {
        this.plugin = plugin;
    };
    
    public void start() {
        // Startup mana generation
        Mana.mana(plugin);
        // Startup ult generation
        Ultimate.ult(plugin);
        // Set gamemode and start gamemode
        //
        // Set wizard's lives
        // 
        // Set gameRunning to true
        gameRunning = true;
    }
}

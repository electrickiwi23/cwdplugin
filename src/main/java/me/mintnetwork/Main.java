package me.mintnetwork;

import me.mintnetwork.commands.DebugCommand;
import me.mintnetwork.commands.GiveWand;
import me.mintnetwork.listeners.RightClickListener;
import me.mintnetwork.repeaters.Mana;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new GiveWand(this);
        new RightClickListener(this);
        new Mana(this);
        new DebugCommand(this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}

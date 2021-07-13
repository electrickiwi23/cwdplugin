package me.mintnetwork.utils;

import me.mintnetwork.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static void configSetup(Main plugin){

        FileConfiguration config = plugin.getConfig();

        config.addDefault("FlareAx",0.0);
        config.addDefault("FlareAy",0.0);
        config.addDefault("FlareAz",0.0);
        config.addDefault("FlareBx",0.0);
        config.addDefault("FlareBy",0.0);
        config.addDefault("FlareBz",0.0);
        config.addDefault("FlareCx",0.0);
        config.addDefault("FlareCy",0.0);
        config.addDefault("FlareCz",0.0);

        config.options().copyDefaults(true);
        plugin.saveConfig();

        plugin.saveDefaultConfig();


    }
}

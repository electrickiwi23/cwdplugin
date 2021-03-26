package me.mintnetwork.wizard;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class WizardInit{

    public static HashMap<Player, Wizard> playersWizards = new HashMap<>();

    public void WizardInitialization(Main plugin) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            //on start up add player to the list
            playersWizards.put(player, new Wizard(player));
        }
    }


}

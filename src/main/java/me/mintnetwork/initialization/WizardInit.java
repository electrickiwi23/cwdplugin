package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Wizard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class WizardInit{

    public static HashMap<Player, Wizard> playersWizards = new HashMap<>();

    public void WizardInitialization(Main plugin) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            //on start up add player to the list
            playersWizards.put(player, new Wizard(player));
        }
    }


}

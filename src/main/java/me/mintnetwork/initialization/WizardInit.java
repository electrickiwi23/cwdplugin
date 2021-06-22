package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Wizard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class WizardInit{

    public static HashMap<UUID, Wizard> playersWizards = new HashMap<>();

    public void WizardInitialization(Main plugin) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            //on start up add player to the list
            playersWizards.put(player.getUniqueId(), new Wizard(player));
        }
    }


}

package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {


    public PlayerRespawnListener(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void EntityDeath(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        Wizard w = WizardInit.playersWizards.get(p.getUniqueId());
        if (w.kitID.equals(Kit.AVIATOR)){
            StatusEffects.cloudFloating.remove(p);
            p.setAllowFlight(true);
        }
    }
}

package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerFlyListener implements Listener {

    public PlayerFlyListener(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerToggleFlightEvent event) {
        Player p = event.getPlayer();
        if (WizardInit.playersWizards.get(p.getUniqueId()).kitID.equals(Kit.AVIATOR)){
            if (StatusEffects.cloudFloating.contains(p)){
                p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                StatusEffects.cloudFloating.remove(p);
            }else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100000, 3, false, false));
                StatusEffects.cloudFloating.add(p);
            }
            event.setCancelled(true);
        }

    }
}

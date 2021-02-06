package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDismountListener implements Listener {

    private final Main plugin;

    public PlayerDismountListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        Entity entity = e.getDismounted();
        Map<LivingEntity, Integer> shadowTimer = StatusEffects.getShadowGrappleTimer();
        if (entity instanceof LivingEntity) {
            if (shadowTimer.containsKey(entity)) {
                shadowTimer.replace((LivingEntity) entity,shadowTimer.get(entity)+2);
                e.setCancelled(true);
            }
        }

    }
}

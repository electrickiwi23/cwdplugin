package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.ShadowGrapple;
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
        Entity entity = e.getEntity();
        if (entity instanceof LivingEntity) {
            for (int i = 0; i < ShadowGrapple.allGrapples.size(); i++) {
                ShadowGrapple grapple = ShadowGrapple.allGrapples.get(i);
                if (grapple.getVictim()==entity){
                    if (grapple.tick(2)){
                        i--;
                    }

                    Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            grapple.getShadow().addPassenger(entity);
                        }
                    });
                    e.setCancelled(true);

                }
            }
        }

    }
}

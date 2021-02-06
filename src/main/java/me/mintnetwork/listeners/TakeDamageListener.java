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
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TakeDamageListener implements Listener {

    private final Main plugin;

    public TakeDamageListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onHit(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player){
            Player p = (Player) entity;
            Collection<Player> shadowList = StatusEffects.getShadowInvis();
            if (shadowList.contains(p)) {
                Map<Player, Runnable> CancelMap = StatusEffects.getShadowCancel();
                System.out.println("invis damage");
                CancelMap.get(p).run();
            }
        }
        if (entity instanceof LivingEntity){
            LivingEntity living = (LivingEntity) entity;
        Map<LivingEntity, Player> ShadowGrappler = StatusEffects.getShadowGrappler();
        if (ShadowGrappler.containsKey(living)) {
            StatusEffects.ShadowGrappleCancel(living);
        }
        }
    }
}

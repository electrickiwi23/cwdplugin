package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.Bukkit;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Map;

public class EntityExplodeListener implements Listener {

    private final Main plugin;

    public EntityExplodeListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void EntityDeath(EntityExplodeEvent event) {
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        Entity e = event.getEntity();
        if (ID.containsKey(e)){
            if (ID.get(e).equals("Cluster 0")){
                for (int i = 0; i < 6; i++) {
                    Entity tnt = e.getWorld().spawnEntity(e.getLocation(), EntityType.PRIMED_TNT);
                    tnt.setVelocity(e.getLocation().getDirection().rotateAroundY((i+.5)*(Math.PI)/3).add(new Vector(0,1,0)).multiply(.4));
                    ((TNTPrimed) tnt).setFuseTicks(30);
                    ID.put(tnt, "Cluster 1");

                }
                tick.get(e).cancel();
            }

            if (ID.get(e).equals("Cluster 1")){
                for (int i = 0; i < 6; i++) {
                    Entity tnt = e.getWorld().spawnEntity(e.getLocation(), EntityType.PRIMED_TNT);
                    tnt.setVelocity(e.getLocation().getDirection().rotateAroundY((i+.5)*(Math.PI)/3).add(new Vector(0,1,0)).multiply(.4));
                    ((TNTPrimed) tnt).setFuseTicks(30);

                }
            }

        }
    }
}

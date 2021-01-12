package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProjectileHitListener implements Listener {

    private final Main plugin;

    public ProjectileHitListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile e = event.getEntity();
        System.out.println("Hit");
        if (e instanceof Snowball) {
            if (e.doesBounce()) {
                Vector d = e.getVelocity();
                Vector n = event.getHitBlockFace().getDirection().normalize();
                Vector v = d.subtract(n.multiply(d.dot(n) * 2));
                Snowball b = (Snowball) e.getWorld().spawnEntity(e.getLocation().add(n.multiply(-.5)), EntityType.SNOWBALL);
                b.setVelocity(v.multiply(.4));
            }
        }
    }
}

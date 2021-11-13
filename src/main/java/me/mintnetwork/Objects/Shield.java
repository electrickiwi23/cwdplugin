package me.mintnetwork.Objects;

import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Shield {


    public static Map<Entity, Shield> shieldMap = new HashMap<>();

    public void loop(Plugin plugin){
        tick = new BukkitRunnable() {
            @Override
            public void run() {
                    tick();
            }
        }.runTaskTimer(plugin,1,1);
    }

    Entity entity;
    double radius;
    BukkitTask tick;

    public Shield (Entity e,int r,Plugin plugin){
        entity = e;
        radius = r;
        shieldMap.put(e,this);
        loop(plugin);
    }

    public double getRadius(){
        return radius;
    }

    public void tick(){
        for (Entity e: entity.getNearbyEntities(radius+2.5,radius+2.5,radius+2.5)) {
            if (e instanceof Projectile) {
                this.reflectEntity(e);
            }
        }
    }

    public void reflectEntity(Entity projectile){
        if (projectile.getLocation().distance(entity.getLocation()) >= radius && projectile.getLocation().add(projectile.getVelocity()).distance(entity.getLocation())<=radius) {
            Vector d = projectile.getVelocity();
            Vector n = projectile.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
            if (d.dot(n) <= 0 && projectile.getLocation().distance(entity.getLocation()) >= radius-1) {
                if (SkeletonTurret.turretMap.containsKey(entity)){
                    SkeletonTurret.turretMap.get(entity).reflect();
                }

                Vector v = d.subtract(n.multiply(d.dot(n) * 2));
                projectile.setVelocity(v);

                Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                if (velocity.containsKey(projectile)) {
                    velocity.replace(projectile, v);
                }
            }
        }
    }

    public void remove(){
        shieldMap.remove(entity);
        entity.remove();
        tick.cancel();
    }

    public Vector reflectVector(Location location, Vector velocity) {
        if (!SkeletonTurret.turretMap.containsKey(entity) || SkeletonTurret.turretMap.get(entity).active) {

            Vector n = location.toVector().subtract(entity.getLocation().toVector()).normalize();
            if (velocity.dot(n) <= 0 && location.distance(entity.getLocation()) >= radius - 1) {
                if (SkeletonTurret.turretMap.containsKey(entity)) {
                    SkeletonTurret.turretMap.get(entity).reflect();
                }
                return velocity.subtract(n.multiply(velocity.dot(n) * 2));
            }
        }
        return velocity;
    }

}

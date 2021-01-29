package me.mintnetwork.spells.projectiles;

import me.mintnetwork.Main;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectileInfo {

    private final Main plugin;

    public ProjectileInfo(Main plugin) {
        this.plugin = plugin;
    };


    //per tick code of the projectile
    public static Map<Entity, BukkitTask> tickCode = new HashMap<Entity, BukkitTask>();

    public static Map<Entity, BukkitTask> getTickCode() {return tickCode; };

    //code that runs when it goes off
    public static Map<Entity, BukkitTask> activateCode = new HashMap<Entity, BukkitTask>();

    public static Map<Entity, BukkitTask> getActivateCode() {return activateCode; };

    //speed of linear projectiles
    public static Map<Entity, Vector> LockedVelocity = new HashMap<Entity, Vector>();

    public static Map<Entity, Vector> getLockedVelocity() {return LockedVelocity;}

    //type of projectile
    public static Map<Entity, String> projectileID = new HashMap<Entity, String>();

    public static Map<Entity, String> getProjectileID() {return projectileID;}

    //snowball linked to the armor stand that runs the grenade code
    public static Map<Entity,Entity> linkedSnowball = new HashMap<Entity, Entity>();

    public static Map<Entity,Entity> getLinkedSnowball() {return linkedSnowball;}

    //location on the entity that the grenade sticks to
    public static Map<Entity, List<Double>> stickyOffset = new HashMap<Entity, List<Double>>();

    public static Map<Entity, List<Double>> getStickyOffset() {return stickyOffset;}

    public static Map<Entity, Boolean> pillarCoolDown = new HashMap<>();

    public static Map<Entity, Boolean> getPillarCoolDown() {return pillarCoolDown;}

}

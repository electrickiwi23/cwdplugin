package me.mintnetwork.Objects;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class SkeletonTurret {

    Skeleton entity;
    int charge;
    int count;
    public boolean active;
    EffectManager em;
    BukkitTask tick;

    public static Map<Entity, SkeletonTurret> turretMap = new HashMap<>();

    public void loop(Plugin plugin){
        tick = new BukkitRunnable() {
            @Override
            public void run() {
                    tick();
            }
        }.runTaskTimer(plugin,1,1);
    }

    public SkeletonTurret(Player p, Block block, BlockFace face, EffectManager em,Plugin plugin){
        entity = (Skeleton) p.getWorld().spawnEntity(block.getLocation().add(face.getDirection().normalize().multiply(4)).add(.5, 0, .5), EntityType.SKELETON);
        entity.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
        entity.getEquipment().setItemInOffHand(new ItemStack(Material.BOW));
        entity.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        entity.setInvisible(true);
        entity.setAI(false);

        charge = 0;
        count = 0;
        active = false;

        this.em = em;

        CircleEffect effect = new CircleEffect(em);
        effect.enableRotation = false;
        effect.setLocation(entity.getLocation());
        effect.radius = 15;
        effect.particle = Particle.SMOKE_NORMAL;
        effect.particleCount = 1;
        effect.particleSize = 3;
        effect.wholeCircle = true;
        effect.iterations = 2;
        em.start(effect);

        new Shield(entity, 3,plugin);

        turretMap.put(entity,this);

        loop(plugin);
    }

    public void reflect(){
        SphereEffect sphere = new SphereEffect(em);
        sphere.setLocation(entity.getLocation());
        sphere.particle = Particle.SMOKE_NORMAL;
        sphere.radius = 3;
        sphere.iterations = 1;
        em.start(sphere);

        charge -= 12;
    }

    public void tick(){

        if (active) {
            Entity aimed = null;
            double aimedDistance = 20.0;

            for (Entity e : entity.getNearbyEntities(16, 16, 16)) {
                if (e instanceof LivingEntity) {
                    if (!(e instanceof ArmorStand)) {
                        if (entity.getLocation().distance(e.getLocation()) < aimedDistance) {
                            aimed = e;
                            aimedDistance = entity.getLocation().distance(e.getLocation());
                        }
                    }
                }
            }

            for (Entity projectile : entity.getNearbyEntities(5, 5, 5)) {
                if (projectile instanceof Projectile) {
                    if (projectile.getLocation().distance(entity.getLocation()) <= 3.5) {


                        Vector d = projectile.getVelocity();
                        Vector n = entity.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize().multiply(-1);
                    }
                }
            }
            if (aimed != null) {
                if (aimed.getLocation().distance(entity.getLocation()) <= 15) {
                    entity.teleport(entity.getLocation().setDirection(entity.getLocation().toVector().subtract(aimed.getLocation().toVector()).normalize().multiply(-1)));
                    count++;
                    if (count >= 8) {
                        entity.launchProjectile(Arrow.class);
                        count = 0;
                        charge = charge - 2;
                    }
                }
            }
            if (charge <= 0) {
                charge = 0;
                entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                entity.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
                active = false;
            }
        } else {
            charge++;
            entity.getWorld().spawnParticle(Particle.SMOKE_NORMAL, entity.getLocation().add(0, 1, 0), 1, .2, .4, .2, .1);
            if (charge >= 80) {
                entity.getEquipment().setHelmet(new ItemStack(Material.SKELETON_SKULL));
                active = true;
            }
        }

        if (entity.isDead()) remove();


    }

    private void remove(){
        Shield.shieldMap.get(entity).remove();
        turretMap.remove(entity);
        tick.cancel();
    }
}

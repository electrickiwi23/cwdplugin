package me.mintnetwork.Objects;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AtomEffect;
import me.mintnetwork.repeaters.BlockDecay;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Objects;

public class BlackHole {
    AtomEffect effect;
    Location location;
    BukkitTask task;
    boolean active;
    int age;


    public BlackHole(Location location, EffectManager effectManger, Plugin plugin) {
        Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();



        this.location = location;
        task = new BukkitRunnable() {
            @Override
            public void run() {

                if (age>60) {
                    if ((age&10)==0&&age<150) location.getWorld().playSound(location, Sound.AMBIENT_CAVE, .1f,1);

                    for (Block block : BlockDecay.decay.keySet()) {
                        if (block.getLocation().add(.5, .5, .5).distance(location) <= 4) {
                            BlockDecay.decay.get(block).damage((float) ((5 - block.getLocation().add(.5, .5, .5).distance(location)) / 3.0F));
                        }

                    }

                    for (Entity entity : Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, 12, 12, 12)) {
                        if (entity instanceof Projectile) {
                            if (entity.getLocation().distance(location) <= 9) {
                                Vector n = location.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(2 - (location.distance(entity.getLocation())) / 10);
                                double len = entity.getVelocity().length();
                                Vector suck = entity.getVelocity().add(n).multiply(.5).normalize().multiply(len);

                                entity.setVelocity(suck);
                                if (velocity.containsKey(entity)) {
                                    velocity.replace(entity, suck);
                                }
                            }

                        } else if (entity instanceof Damageable) {
                            if (entity.getLocation().distance(location) <= 8) {
                                Vector n = location.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.025 + .015 * (1 - (location.distance(entity.getLocation())) / 10));
                                entity.setVelocity(entity.getVelocity().add(n));
                                if (entity.getLocation().distance(location) <= 3) {
                                    ((Damageable) entity).damage(3);
                                }
                            }
                        }
                    }
                } else if (age==60){
                    effect = new AtomEffect(effectManger);
                    effect.setLocation(location.add(0, 0, 0));
                    effect.yaw = location.getYaw();
                    effect.pitch = location.getPitch();
                    effect.particleNucleus = Particle.SQUID_INK;
                    effect.particlesNucleus = 30;
                    effect.radiusNucleus = (float) .3;
                    effect.particleOrbital = Particle.FLAME;
                    effect.orbitals = 3;
                    effectManger.start(effect);
                    effect.iterations = 140;
                } else {
                    location.getWorld().spawnParticle(Particle.SQUID_INK, location, 8, .2, .2, .2, 0);

                }


                age++;
                if (age>=200) remove();
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    public void remove(){
        task.cancel();
        effect.cancel();
    }


}

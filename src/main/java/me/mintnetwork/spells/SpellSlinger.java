package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.Objects.Shield;
import me.mintnetwork.repeaters.BlockDecay;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Map;

public class SpellSlinger {

    public static void LightningBolt(Player p, Plugin plugin, EffectManager em) {
        if (Mana.spendMana(p, 4)) {
            Vector v = p.getEyeLocation().getDirection();
            Location l = p.getEyeLocation().add(v.normalize());
            BukkitTask cloud = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(50, 50, 50), 2);
                    p.getWorld().spawnParticle(Particle.REDSTONE, l, 20, .15, .15, .15, dust);
                }
            }, 1, 1);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    cloud.cancel();
                    Vector direction = v;
                    Location current = l;
                    p.getWorld().playSound(current, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                    int range = 0;
                    boolean hasHit = false;
                    while (!hasHit) {
                        if (current.isWorldLoaded()) {
                            RayTraceResult ray = p.getWorld().rayTrace(current, direction, 1, FluidCollisionMode.NEVER, true, .1, null);
                            Location hitLocation = null;
                            LivingEntity hitEntity = null;
                            if (ray != null) {
                                try {
                                    hitEntity = (LivingEntity) ray.getHitEntity();
                                } catch (Exception ignore) {
                                }
                                try {
                                    hitLocation = ray.getHitPosition().toLocation(p.getWorld());
                                } catch (Exception ignore) {
                                }
                                if (hitLocation != null && hitEntity == null) {
                                    hasHit = true;
                                    if (BlockDecay.decay.containsKey(ray.getHitBlock())){
                                        DecayBlock block = BlockDecay.decay.get(ray.getHitBlock());
                                        block.damage(120);
                                        block.setForceful(true);
                                        if (block.health<=0){
                                            block.remove();
                                            hasHit = false;
                                        }
                                    }

                                }
                                if (hitEntity != null) {
                                    hasHit = true;
                                    LivingEntity finalHitEntity = hitEntity;
                                    final int[] hits = {0};
                                    BukkitTask task = new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            hits[0]++;
                                            finalHitEntity.setNoDamageTicks(0);
                                            finalHitEntity.damage(1, p);
                                            if (hits[0]>=6) this.cancel();
                                            finalHitEntity.setVelocity(new Vector(0,0,0));
                                        }
                                    }.runTaskTimer(plugin,0,2);
                                }

                            }
                            if (!hasHit) {
                                current = current.add(direction);
                                Particle.DustOptions dust = new Particle.DustOptions(Color.YELLOW,2);
                                p.getWorld().spawnParticle(Particle.REDSTONE, current, 2, .1, .1, .1, 0,dust);
                                range++;
                                if (range >= 80) hasHit = true;
                                Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                                for (Entity shield : Shield.shieldMap.keySet()) {
                                    if (shield.getLocation().distance(current)<Shield.shieldMap.get(shield).getRadius()+.5){
                                        direction = Shield.shieldMap.get(shield).reflectVector(current,direction);
                                    }
                                }
                            }
                        } else {
                            hasHit = true;
                        }
                    }
                }
            }, 10);
        }
    }

    public static void SnowBolt(Player p,Plugin plugin){
        if (Mana.spendMana(p, 3)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setGravity(false);
            ID.put(bolt, "SnowBolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    bolt.getWorld().spawnParticle(Particle.SNOW_SHOVEL, bolt.getLocation(), 2, .1, .1, .1);
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    tick.get(bolt).cancel();
                    bolt.remove();
                }
            }, 100);
        }
    }

    public static void FireBolt(Player p,Plugin plugin){
        if (Mana.spendMana(p, 2)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.FIRE_CHARGE));
            bolt.setFireTicks(600);
            bolt.setGravity(false);
            ID.put(bolt, "FireBolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    bolt.getWorld().spawnParticle(Particle.FLAME, bolt.getLocation(), 1, .1, .1, .1,.1);
                }
            }, 1, 1));
        }
    }

    public static void ElementBlast(Player p, Plugin plugin, EffectManager em) {
        if (Ultimate.spendUlt(p)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(.7));
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.FIREWORK_STAR));
            bolt.setGravity(false);
            ID.put(bolt, "Element Blast");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
                Map<Entity, Vector> velocity1 = ProjectileInfo.getLockedVelocity();
                bolt.setVelocity(velocity1.get(bolt));
                bolt.getWorld().spawnParticle(Particle.FLAME, bolt.getLocation(), 20, .2, .2, .2, 0);
                bolt.getWorld().spawnParticle(Particle.SNOW_SHOVEL, bolt.getLocation(), 20, .3, .3, .3, 0);
                LineEffect line = new LineEffect(em);
                line.setLocation(bolt.getLocation());
                line.setTargetLocation(bolt.getLocation().add(Math.random() * 4 - 2, Math.random() * 4 - 2, Math.random() * 4 - 2));
                line.isZigZag = true;
                line.zigZags = 2;
                line.zigZagOffset = new Vector(Math.random() * .06 - .03, Math.random() * .06 - .03, Math.random() * .06 - .03);
                line.particle = Particle.REDSTONE;
                line.color = Color.YELLOW;
                line.start();
                for (Entity e : bolt.getNearbyEntities(5, 5, 5)) {
                    if (e.getLocation().distance(bolt.getLocation()) <= 4) {
                        if (e instanceof LivingEntity) {
                            if (!(e.equals(p))) {
                                ((LivingEntity) e).damage(1);
                                ((LivingEntity) e).setNoDamageTicks(0);
                                e.setVelocity(new Vector(0, 0, 0));
                            }
                        }
                    }
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                tick.get(bolt).cancel();
                bolt.remove();
            }, 100);
        }
    }
}

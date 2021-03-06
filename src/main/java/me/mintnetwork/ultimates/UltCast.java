package me.mintnetwork.ultimates;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.DnaEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.WarpEffect;
import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class UltCast {

    private final Main plugin;

    public UltCast(Main plugin) {
        this.plugin = plugin;
    }

    public static void ClusterBomb(Player p, Plugin plugin) {
        //Spend Ult
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.PRIMED_TNT);
        tnt.setVelocity(p.getEyeLocation().getDirection().multiply(1.5));
        tnt.setFuseTicks(120);
        ID.put(tnt, "Cluster 0");
        tick.put(tnt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                tnt.getWorld().spawnParticle(Particle.SMOKE_LARGE, tnt.getLocation(), 1, .4, .4, .4, 0);
                tnt.getWorld().spawnParticle(Particle.REDSTONE, tnt.getLocation(), 1, .5, .5, .5, 0, dust);
            }
        }, 1, 1));


    }

    public static void AirStrike(Player p, Plugin plugin) {
        //Spend Ult
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        Map<Player, Entity> tracked = ProjectileInfo.getStrikeTrackedEntity();
        if (tracked.containsKey(p)) {
            AirStrikeRelease(p, plugin);
        } else {
            Arrow arrow = p.launchProjectile(Arrow.class);
            tracked.put(p, arrow);
            ID.put(arrow, "Tracker Arrow");
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            arrow.setDamage(.35);
            tick.put(arrow, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                    arrow.getWorld().spawnParticle(Particle.REDSTONE, tracked.get(p).getLocation(), 1, .1, .1, .1, 0, dust);
                }
            }, 20, 20));
        }
    }

    public static void AirStrikeRelease(Player p, Plugin plugin) {
        //Spend Ult
        Map<Player, Entity> tracked = ProjectileInfo.getStrikeTrackedEntity();
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        Firework firework = (Firework) p.getWorld().spawnEntity(tracked.get(p).getLocation().add(0, 1, 0), EntityType.FIREWORK);
        FireworkEffect.Builder effect = FireworkEffect.builder();
        Random random = new Random();
        FireworkMeta meta = firework.getFireworkMeta();
        effect.with(FireworkEffect.Type.BALL);
        effect.withColor(Color.RED);
        meta.addEffect(effect.build());
        meta.setPower(1);
        firework.setShooter(p);
        firework.setShotAtAngle(true);
        firework.setVelocity(new Vector(0, 1, 0));
        firework.setFireworkMeta(meta);
        final Location origin = tracked.get(p).getLocation();

        BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                Location l = origin.toVector().toLocation(origin.getWorld());
                Location strike = l.add((random.nextGaussian() * 15), 80, (random.nextGaussian() * 15));
                Fireball fireball = (Fireball) strike.getWorld().spawnEntity(strike, EntityType.FIREBALL);
                fireball.setVelocity(new Vector(0, -2.5, 0));
                fireball.setDirection(new Vector(0, -2.5, 0));
                fireball.setYield(3);
            }
        }, 40, 2);
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                tick.get(tracked.get(p)).cancel();
                tracked.get(p).remove();
                tracked.remove(p);
                task.cancel();
            }
        }, 220);
    }

    public static void BloodUlt(Player p, Plugin plugin) {
        //Spend Ult
        Snowball bolt = p.launchProjectile(Snowball.class);
        Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
        velocity.put(bolt, p.getEyeLocation().getDirection().multiply(1.5));
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        bolt.setItem(new ItemStack(Material.RED_DYE));
        bolt.setGravity(false);
        ID.put(bolt, "BloodUlt");
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                bolt.setVelocity(velocity.get(bolt));
                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 4);
                bolt.getWorld().spawnParticle(Particle.REDSTONE, bolt.getLocation(), 3, .1, .1, .1, dust);
            }
        }, 1, 1));
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                tick.get(bolt).cancel();
                bolt.remove();
            }
        }, 160);
    }

    public static void ImmortalPotionUlt(Player p) {
        //Spend Ult
        ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
        ItemStack item = (new ItemStack(Material.SPLASH_POTION));
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        potionMeta.setColor(Color.fromRGB(255, 215, 0));
        item.setItemMeta(potionMeta);
        potion.setItem(item);
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        ID.put(potion, "Immortal Potion");
    }

    public static void PaintActivateUlt(Player p, Plugin plugin) {
        Map<LivingEntity, Integer> painted = StatusEffects.getPaintTimer();
        if (painted.keySet().size() > 0) {
            //Spend Ult
            ArrayList<LivingEntity> list = new ArrayList<LivingEntity>(painted.keySet());
            for (LivingEntity e : list) {
                e.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1));
                e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 3));
            }
            final int[] paintActivateCount = {0};
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    paintActivateCount[0]++;
                    for (LivingEntity e : list) {
                        Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                        e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                        dust = new Particle.DustOptions(Color.ORANGE, 2);
                        e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                        dust = new Particle.DustOptions(Color.YELLOW, 2);
                        e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                        dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 2);
                        e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                        dust = new Particle.DustOptions(Color.BLUE, 2);
                        e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                        dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 2);
                        e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                    }
                    if(paintActivateCount[0]>=20){
                        for (LivingEntity e : list) {
                            Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 3);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                            dust = new Particle.DustOptions(Color.ORANGE, 3);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                            dust = new Particle.DustOptions(Color.YELLOW, 3);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                            dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 3);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                            dust = new Particle.DustOptions(Color.BLUE, 3);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                            dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 3);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                            for (Entity entity : e.getWorld().getNearbyEntities(e.getLocation(), 6, 6, 6)) {
                                if (entity instanceof LivingEntity) {
                                    LivingEntity live = (LivingEntity) entity;
                                    if (entity.getLocation().distance(e.getLocation()) <= 5) {
                                        live.setNoDamageTicks(0);
                                        live.damage(5, p);
                                        live.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 2));
                                        live.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 1));
                                        if (painted.containsKey(live)) {
                                            painted.remove(live);
                                        }
                                    }
                                }
                            }
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin,1,1);
        }
    }

    public static void ElementBlast(Player p, Plugin plugin, EffectManager em) {
        Snowball bolt = p.launchProjectile(Snowball.class);
        Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
        velocity.put(bolt, p.getEyeLocation().getDirection().multiply(.7));
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        bolt.setItem(new ItemStack(Material.FIREWORK_STAR));
        bolt.setGravity(false);
        ID.put(bolt, "Element Blast");
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

        float shootYaw = p.getLocation().getYaw();
        float shootPitch = p.getLocation().getPitch();

        tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                bolt.setVelocity(velocity.get(bolt));
                bolt.getWorld().spawnParticle(Particle.FLAME,bolt.getLocation(),20,.2,.2,.2,0);
                bolt.getWorld().spawnParticle(Particle.SNOW_SHOVEL,bolt.getLocation(),20,.3,.3,.3,0);
                LineEffect line = new LineEffect(em);
                line.setLocation(bolt.getLocation());
                line.setTargetLocation(bolt.getLocation().add( Math.random()*4-2,Math.random()*4-2,Math.random()*4-2));
                line.isZigZag = true;
                line.zigZags = 2;
                line.zigZagOffset = new Vector(Math.random()*.06-.03,Math.random()*.06-.03,Math.random()*.06-.03);
                line.particle = Particle.REDSTONE;
                line.color = Color.YELLOW;
                line.start();
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


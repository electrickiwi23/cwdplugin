package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Map;

public class SkyFlyer {
    public static void AirNeedles(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Arrow arrow = p.launchProjectile(Arrow.class);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(arrow, "Wind Arrow");
            arrow.setDamage(.35 );
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(arrow, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    arrow.getWorld().spawnParticle(Particle.SPELL, arrow.getLocation(), 1, .1, .1, .1, 0);
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {

                    Arrow arrow = p.launchProjectile(Arrow.class);
                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                    ID.put(arrow, "Wind Arrow");
                    arrow.setDamage(.35);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    tick.put(arrow, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                        @Override
                        public void run() {
                            arrow.getWorld().spawnParticle(Particle.SPELL, arrow.getLocation(), 1, .1, .1, .1, 0);
                        }
                    }, 1, 1));

                }
            }, 3);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                    ID.put(arrow, "Wind Arrow");
                    arrow.setDamage(.35);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    tick.put(arrow, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                        @Override
                        public void run() {
                            arrow.getWorld().spawnParticle(Particle.SPELL, arrow.getLocation(), 1, .1, .1, .1, 0);
                        }
                    }, 1, 1));
                }
            }, 6);
        }
    }

    public static void CloudBurst(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 10, .6, .6, .6, 0);
            final int[] count = {0};
            p.setVelocity(new Vector(0, 1.4, 0));
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    count[0]++;
                    p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, .2, .2, .2, 0);
                    if (count[0] >= 5) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }

    public static void AirDash(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            p.setGravity(false);
            p.setVelocity(p.getEyeLocation().getDirection().multiply(1.3));
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    p.setVelocity(p.getEyeLocation().getDirection().multiply(1.3));
                    p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, .1, .1, .1, 0);
                }
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    p.setVelocity(p.getVelocity().multiply(.5));
                    p.setGravity(true);
                    task.cancel();
                }
            }, 6);
        }
    }

    public static void TornadoBlast(Player p, Plugin plugin) {
        if (Ultimate.spendUlt(p)) {
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            grenade.setItem(new ItemStack(Material.BONE_MEAL));
            ID.put(grenade, "TornadoUlt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(grenade, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    p.getWorld().spawnParticle(Particle.CLOUD, grenade.getLocation(), 3, .2, .2, .2, 0);
                }
            }, 1, 1));
        }
    }
}

package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Map;

public class Aviator extends KitItems {


    public Aviator(){
        ultTime = Utils.AVIATOR_ULT_TIME;

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta1 = wand1.getItemMeta();


        wands.add(wand1);
        //create itemstacks for each wand of the class
    }
    public static void AirNeedles(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, Utils.AIR_NEEDLES_COST)) {
            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ARROW_SHOOT,.3F,1);
            Arrow arrow = p.launchProjectile(Arrow.class);
            arrow.setVelocity(arrow.getVelocity().multiply(1.5));
            arrow.setGravity(false);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(arrow, "Wind Arrow");
            arrow.setDamage(.3);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        arrow.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, arrow.getLocation(), 2, .1, .1, .1, 0, null, true);
                        arrow.remove();
                        tick.remove(arrow);
                    }
                }
            }, 10);

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ARROW_SHOOT,.3F,1);

                    Arrow arrow = p.launchProjectile(Arrow.class);
                    arrow.setVelocity(arrow.getVelocity().multiply(1.5));
                    arrow.setGravity(false);
                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                    ID.put(arrow, "Wind Arrow");
                    arrow.setDamage(.3);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

                    Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if (!arrow.isDead()) {
                                arrow.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, arrow.getLocation(), 2, .1, .1, .1, 0, null, true);
                                arrow.remove();
                                tick.remove(arrow);
                            }
                        }
                    }, 10);

                }
            }, 3);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {

                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ARROW_SHOOT,.3F,1);
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    arrow.setVelocity(arrow.getVelocity().multiply(1.5));
                    arrow.setGravity(false);
                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                    ID.put(arrow, "Wind Arrow");
                    arrow.setDamage(.3);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if (!arrow.isDead()) {
                                arrow.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, arrow.getLocation(), 2, .1, .1, .1, 0, null, true);
                                arrow.remove();
                                tick.remove(arrow);
                            }
                        }
                    }, 10);
                }
            }, 6);
        }
    }

    public static void CloudBurst(Player p, Plugin plugin) {
        if (Mana.spendMana(p, Utils.CLOUD_BURST_COST)) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP,.7F,1);
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
        if (Mana.spendMana(p, Utils.AIR_DASH_COST)) {
            p.setGravity(false);
            double tempY = p.getEyeLocation().getDirection().getY();
            p.setVelocity(p.getEyeLocation().getDirection().setY(0).normalize().setY(Math.max(-.3,Math.min(.3,tempY))).normalize().multiply(1.5));
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    double tempY = p.getEyeLocation().getDirection().getY();
                    p.setVelocity(p.getEyeLocation().getDirection().setY(0).normalize().setY(Math.max(-.3,Math.min(.3,tempY))).normalize().multiply(1.5));
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
            }, 7);
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

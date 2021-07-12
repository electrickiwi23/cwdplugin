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

import java.util.ArrayList;
import java.util.Map;

public class Aviator extends KitItems {


    public Aviator(){
        ultTime = Utils.AVIATOR_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();


        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.CLOUD_BURST_COST);
        lore.add(ChatColor.GRAY + "Propel yourself high into the air ");
        lore.add(ChatColor.GRAY +"using a burst of smoke.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Cloud Bust"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.AIR_DASH_COST);
        lore.add(ChatColor.GRAY + "Hurl yourself quickly in the direction ");
        lore.add(ChatColor.GRAY + "you are facing using the air.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Air Dash"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.AIR_NEEDLES_COST);
        lore.add(ChatColor.GRAY + "Shoot out a trio of needles that deal ");
        lore.add(ChatColor.GRAY + "a moderate amount of damage.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Air Needles"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY + "Create a powerful wind current that traps");
        lore.add(ChatColor.GRAY + "those in its radius, and disables abilities.");
        meta.setDisplayName(ChatColor.GOLD+"Tornado Blast");
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Press shift to float on the air ");
        lore.add(ChatColor.GRAY + "and take no fall damage.");
        meta.setDisplayName(ChatColor.WHITE + "Wind Cushion");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add("Dominate the skies with enhanced");
        lore.add("movement and the power of flight.");

        menuItem.setType(Material.FEATHER);
        meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA+"Aviator");
        meta.setLore(lore);

        menuItem.setItemMeta(meta);

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

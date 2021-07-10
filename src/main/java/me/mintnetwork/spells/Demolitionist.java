package me.mintnetwork.spells;

import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Demolitionist extends KitItems {


    public Demolitionist(){
        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta1 = wand1.getItemMeta();


        wands.add(wand1);
        //create itemstacks for each wand of the class
    }

    public static void TNTLine(Player p, Plugin plugin) {
        p.playSound(p.getEyeLocation(),Sound.ENTITY_TNT_PRIMED,1.3F,1);
        if (Mana.spendMana(p, Utils.TNT_LINE_COST)) {
            for (int i = 1; i < 6; i++) {
                int finalI = i;
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Entity tnt = p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.PRIMED_TNT);
                        tnt.setVelocity(p.getEyeLocation().getDirection().multiply(finalI).multiply(.32));
                    }
                }, i);
            }
        }
    }

    public static void TNTGrenade(Player p, Plugin plugin) {
        if (Mana.spendMana(p, Utils.TNT_GRENADE_COST)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setInvisible(true);
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            grenade.setItem(new ItemStack(Material.TNT));
            grenade.setBounce(true);

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(stand, grenade);
            linked.put(grenade, stand);
            ID.put(stand, "TNTGrenade");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            Map<Entity, BukkitTask> activate = ProjectileInfo.getActivateCode();
            tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    stand.teleport(linked.get(stand));
                    stand.getWorld().spawnParticle(Particle.FLAME, stand.getLocation(), 1, .1, .1, .1, .02);
                }
            }, 1, 1));
            activate.put(stand, Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    stand.getWorld().createExplosion(stand.getLocation(), 2);
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    linked.get(stand).remove();
                    tick.get(stand).cancel();
                    stand.remove();

                }
            }, 100));
        }

    }

    public static void StickyTNTGrenade(Player p, Plugin plugin) {
        if (Mana.spendMana(p, Utils.TNT_STICK_GRENADE_COST)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setInvisible(true);
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            grenade.setItem(new ItemStack(Material.TNT));

            Map<Entity, List<Double>> offset = ProjectileInfo.getStickyOffset();
            List<Double> sticky = new ArrayList<>();
            sticky.add(0, 0.0);
            sticky.add(1, 0.0);
            sticky.add(2, 0.0);
            offset.put(stand, sticky);

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(stand, grenade);
            linked.put(grenade, stand);
            ID.put(stand, "StickyTNT");
            ID.put(grenade, "StickyTNT");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            Map<Entity, BukkitTask> activate = ProjectileInfo.getActivateCode();
            tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    Map<Entity, List<Double>> offset = ProjectileInfo.getStickyOffset();
                    if (linked.get(stand) != null) {
                        double x = linked.get(stand).getLocation().getX() + offset.get(stand).get(0);
                        double y = linked.get(stand).getLocation().getY() + offset.get(stand).get(1);
                        double z = linked.get(stand).getLocation().getZ() + offset.get(stand).get(2);

                        stand.teleport(new Location(stand.getWorld(), x, y, z));
                    }
                    stand.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, stand.getLocation(), 1, .1, .1, .1, .02);
                }
            }, 1, 1));
            activate.put(stand, Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    stand.getWorld().createExplosion(stand.getLocation(), (float) 1.5);
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    if (linked.get(stand) != null) {
                        if (!(linked.get(stand) instanceof LivingEntity)) {
                            linked.get(stand).remove();
                        }
                    }
                    tick.get(stand).cancel();

                    stand.remove();

                }
            }, 100));
        }

    }

    public static void ClusterBomb(Player p, Plugin plugin) {
        if (Ultimate.spendUlt(p)) {
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.PRIMED_TNT);
            tnt.setVelocity(p.getEyeLocation().getDirection().multiply(1.5));
            tnt.setFuseTicks(100);
            ID.put(tnt, "Cluster 0");
            tick.put(tnt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                tnt.getWorld().spawnParticle(Particle.SMOKE_LARGE, tnt.getLocation(), 1, .4, .4, .4, 0);
                tnt.getWorld().spawnParticle(Particle.REDSTONE, tnt.getLocation(), 1, .5, .5, .5, 0, dust);
            }, 1, 1));
        }
    }
}

package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.Objects.ShadowGrapple;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Shadow extends KitItems {


    public Shadow() {
        ultTime = Utils.SHADOW_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();


        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SHADOW_INVISIBILITY_COST);
        lore.add(ChatColor.GRAY +"Melt in the shadows to gain");
        lore.add(ChatColor.GRAY +"temporary invisibility until hit.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET + ("Darkness Camouflage"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SHADOW_RETREAT_COST);
        lore.add(ChatColor.GRAY +"Dash smoothly to get away");
        lore.add(ChatColor.GRAY +"or initiate from combat.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET + ("Shadow Dash"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SHADOW_GRAPPLE_COST);
        lore.add(ChatColor.GRAY +"Pick up enemies to separate");
        lore.add(ChatColor.GRAY +"them from teammates.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET + ("Pray Abduction"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY +"Blinds and slows enemies in the");
        lore.add(ChatColor.GRAY +"large cloud created by your ult.");
        meta.setDisplayName(ChatColor.GOLD + ("Consuming Midnight"));
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "If you haven't taken or dealt damage");
        lore.add(ChatColor.GRAY + "in the last 10 seconds, your next");
        lore.add(ChatColor.GRAY + "attack deals extra damage.");
        meta.setDisplayName(ChatColor.WHITE + "Shadow Blade");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Escape into the night to");
        lore.add(ChatColor.GRAY + "bewilder and confuse your foes.");

        menuItem.setType(Material.BLACK_DYE);
        meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "The Shadow");
        meta.setLore(lore);
        menuItem.setItemMeta(meta);
        menuItem.setItemMeta(meta);

        //create itemstacks for each wand of the class
    }

    public static void ShadowRetreat(Player p, Plugin plugin) {

        if (Mana.spendMana(p, Utils.SHADOW_RETREAT_COST)) {
            for (Entity e : p.getNearbyEntities(4, 4, 4)) {
                if (e instanceof LivingEntity) {
                    String teamName = TeamsInit.getTeamName(e);
                    if (!teamName.equals(TeamsInit.getTeamName(p))) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
                    }
                }
            }
            p.getWorld().spawnParticle(Particle.SQUID_INK, p.getEyeLocation(), 30, .1, .1, .1, .3);
            p.setGravity(false);
            double tempY = p.getEyeLocation().getDirection().getY();
            Vector direction = p.getEyeLocation().getDirection().setY(0).normalize().setY(Math.max(-.4, Math.min(.4, tempY))).normalize().multiply(1);
            p.setVelocity(direction);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    p.setVelocity(direction);
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 4, .2, .4, .2, 0, new Particle.DustOptions(Color.BLACK, 2));
                }
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    p.setVelocity(p.getVelocity().multiply(.5));
                    p.setGravity(true);
                    task.cancel();
                }
            }, 8);
        }

//        if (Mana.spendMana(p, 3)) {

//            p.getWorld().spawnParticle(Particle.SQUID_INK, p.getEyeLocation(), 30, .1, .1, .1, .5);
//            p.setVelocity(p.getEyeLocation().getDirection().multiply(-1).add(new Vector(0, .5, 0)).normalize().multiply(1.5));
//        }
    }

    public static void ShadowInvis(Player p, Plugin plugin) {
        Collection<Player> status = StatusEffects.ShadowInvis;
        if (!status.contains(p)) {
            if (Mana.spendMana(p, Utils.SHADOW_INVISIBILITY_COST)) {
                status.add(p);
                ItemStack[] oldArmor = p.getInventory().getArmorContents();
                ItemStack[] noArmor = new ItemStack[4];
                noArmor[0] = null;
                noArmor[1] = null;
                noArmor[2] = null;
                noArmor[3] = null;

                p.getInventory().setArmorContents(noArmor);
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, true, false));
                Map<Player, Runnable> CancelMap = StatusEffects.getShadowCancel();
                Runnable InvisCancel = new Runnable() {
                    @Override
                    public void run() {
                        status.remove(p);
                        p.getInventory().setArmorContents(oldArmor);
                        p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        CancelMap.remove(p);

                    }
                };

                CancelMap.put(p, InvisCancel);
                BukkitTask InvisCancelTask = Bukkit.getServer().getScheduler().runTaskLater(plugin, InvisCancel, 100);

                new BukkitRunnable() {
                    public void run() {
                        if (!status.contains(p)) {
                            InvisCancelTask.cancel();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 1, 1);
            }
        }
    }

    public static void ShadowGrapple(Player p, Plugin plugin) {
        Vector direction = p.getEyeLocation().getDirection();
        RayTraceResult ray = p.getWorld().rayTrace(p.getEyeLocation().add(direction), direction, 5, FluidCollisionMode.NEVER, true, .1, null);
        Particle.DustOptions dust = new Particle.DustOptions(Color.BLACK, 3);
        Entity hit = null;
        p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation().add(direction.multiply(3)), 25, 1, 1, 1, 0, dust);
        if (ray != null) {

            try {
                hit = ray.getHitEntity();
            } catch (Exception ignore) {
            }
            if (hit != null) {
                if (hit != p) {
                    if (hit instanceof LivingEntity && Mana.spendMana(p, Utils.SHADOW_GRAPPLE_COST)) {


                        new ShadowGrapple(p, (LivingEntity) hit, plugin);

                        ((LivingEntity) hit).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1, true, true));
                        ((LivingEntity) hit).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120, 20, true, true));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120, 20, true, true));

                    }
                }
            }
        }
    }

    public static void ConsumingNight(Player p, Plugin plugin, EffectManager em) {
        Vector direction = p.getEyeLocation().getDirection();
        RayTraceResult ray = p.getWorld().rayTrace(p.getEyeLocation().add(direction), direction, 50, FluidCollisionMode.NEVER, true, .1, null);
        if (ray != null) {
            if (Ultimate.spendUlt(p)) {
                Location hit = ray.getHitPosition().toLocation(p.getWorld());

                String TeamName = TeamsInit.getTeamName(p);

                SphereEffect sphere = new SphereEffect(em);
                sphere.radius = 1;
                sphere.particle = Particle.REDSTONE;
                sphere.particleSize = 5;
                sphere.color = Color.BLACK;
                sphere.radiusIncrease = 1;
                sphere.setLocation(hit);
                em.start(sphere);
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                    for (Entity e : p.getWorld().getNearbyEntities(hit, 20, 20, 20)) {
                        if (e.getLocation().distance(hit) <= 20) {
                            if (e instanceof Player) {
                                String VictimTeam = TeamsInit.getTeamName(e);
                                if (!TeamName.equals(VictimTeam)) {
                                    Player victim = (Player) e;
                                    if (StatusEffects.ShadowConsumed.containsKey(victim)) {
                                        StatusEffects.ShadowConsumed.replace(victim, 180);
                                    } else {
                                        StatusEffects.ShadowConsumed.put(victim, 180);
                                    }

                                    victim.setPlayerTime(114000, false);

                                    victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 380, 2));
                                    victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 360, 0));


                                }
                            }
                        }
                    }
                    sphere.cancel();
                }, 20);
            }
        }
    }
}

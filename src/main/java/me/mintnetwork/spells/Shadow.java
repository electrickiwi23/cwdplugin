package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.Objects.ShadowGrapple;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Map;

public class Shadow {
    public static void ShadowRetreat(Player p,Plugin plugin) {

        if (Mana.spendMana(p, 3)) {
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
            Vector direction = p.getEyeLocation().getDirection().setY(0).normalize().setY(Math.max(-.4,Math.min(.4,tempY))).normalize().multiply(1);
            p.setVelocity(direction);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    p.setVelocity(direction);
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 4, .2, .4, .2, 0,new Particle.DustOptions(Color.BLACK,2));
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
            if (Mana.spendMana(p, 3)) {
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

    public static void ShadowGrapple(Player p,Plugin plugin) {
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
                if (hit!=p) {
                    if (hit instanceof LivingEntity && Mana.spendMana(p, 3)) {


                        new ShadowGrapple(p, (LivingEntity) hit, plugin);

                        ((LivingEntity) hit).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1, true, true));
                        ((LivingEntity) hit).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120, 20, true, true));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 120, 20, true, true));

                    }
                }
            }
        }
    }

    public static void ConsumingNight(Player p, Plugin plugin, EffectManager em){
        Vector direction = p.getEyeLocation().getDirection();
        RayTraceResult ray =  p.getWorld().rayTrace(p.getEyeLocation().add(direction),direction,50,FluidCollisionMode.NEVER,true,.1,null);
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

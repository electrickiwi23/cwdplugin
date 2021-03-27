package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.SphereEffect;
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
    public static void ShadowRetreat(Player p, Plugin plugin){
        for (Entity e:p.getNearbyEntities(4,4,4)) {
            if (e instanceof LivingEntity){
                //TEAM CODE make it only effect enemies


                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60,1));
            }
        }
        p.getWorld().spawnParticle(Particle.SQUID_INK,p.getEyeLocation(),30,.1,.1,.1,.5);
        p.setVelocity(p.getEyeLocation().getDirection().multiply(-1).add(new Vector(0,.5,0)).normalize().multiply(1.5));
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

                BukkitTask checker = new BukkitRunnable() {
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

    public static void ShadowGrapple(Player p) {
        Vector direction = p.getEyeLocation().getDirection();
        RayTraceResult ray = p.getWorld().rayTrace(p.getEyeLocation().add(direction), direction, 5, FluidCollisionMode.NEVER, true, .1, null);
        int distance = 5;
        Particle.DustOptions dust = new Particle.DustOptions(Color.BLACK, 3);
        Entity hit = null;
        p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation().add(direction.multiply(3)), 25, 1, 1, 1, 0, dust);
        if (ray != null) {
            distance = (int) Math.ceil(ray.getHitPosition().distance(p.getEyeLocation().toVector()));
            try {
                hit = ray.getHitEntity();
            } catch (Exception ignore) {
            }
            if (hit != null) {
                if (hit instanceof LivingEntity) {
                    ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
                    stand.setSmall(true);
                    stand.setInvisible(true);
                    stand.setInvulnerable(true);
                    p.addPassenger(stand);
                    stand.addPassenger(hit);
                    Map<LivingEntity, Player> ShadowGrappler = StatusEffects.getShadowGrappler();
                    Map<LivingEntity, Integer> ShadowTimer = StatusEffects.getShadowGrappleTimer();
                    Map<Player, LivingEntity> ShadowGrappled = StatusEffects.getShadowGrappled();
                    ((LivingEntity) hit).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,120,1,true,true));
                    ((LivingEntity) hit).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,120,20,true,true));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,120,20,true,true));
                    ShadowGrappler.put((LivingEntity) hit,p);
                    ShadowGrappled.put(p,(LivingEntity) hit);
                    ShadowTimer.put((LivingEntity) hit, 0);
                    hit.setCustomNameVisible(false);

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
                                if (TeamName.equals(VictimTeam)) {
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

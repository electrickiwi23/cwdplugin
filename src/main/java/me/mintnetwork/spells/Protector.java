package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.Objects.Shield;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;

public class Protector {

    public static void Slam(Player p, Plugin plugin) {

        if (Mana.spendMana(p, 3)) {

            Vector v = p.getEyeLocation().getDirection().setY(0).normalize();
            v.add(new Vector(v.getX() * 2, 2, v.getZ() * 2).normalize().multiply(1.2));
            p.setVelocity(v);
            StatusEffects.UsingMove.add(p);

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation().add(0, 1, 0), 2, -.2, .4, -.2, 0);
                    if (p.isOnGround()) {
                        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getLocation(), 50, 2, 0, 2, p.getLocation().add(0, -1, 0).getBlock().getBlockData());
                        p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation(), 40, 0, 0, 0, .3);
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BASALT_BREAK,.6F,1);
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GOAT_RAM_IMPACT,.6F,1);

                        StatusEffects.UsingMove.remove(p);
                        for (Entity e : p.getNearbyEntities(5, 2, 5)) {
                            if (e instanceof LivingEntity) {
                                if (!(e instanceof ArmorStand)) {
                                    if (!TeamsInit.getTeamName(e).equals(TeamsInit.getTeamName(p))){
                                        if (e.isOnGround()) {

                                        ((LivingEntity) e).damage(2, p);
                                        Vector v = e.getLocation().toVector().subtract(p.getLocation().toVector()).setY(0).normalize().multiply(1.6);
                                        e.setVelocity(new Vector(v.getX(), .5, v.getZ()));
                                        }
                                    }
                                }
                            }
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 3, 1);
        }


    }

    public static void ShieldDome(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, 4)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(stand, "ShieldDome");
            stand.setVisible(false);
            stand.setMarker(true);
            SphereEffect effect = new SphereEffect(em);
            effect.setLocation(p.getLocation().add(0, 0, 0));
            effect.particle = Particle.END_ROD;
            effect.particleCount = 1;
            effect.radius = 4;
            effect.iterations = 200;
            em.start(effect);

            Shield shield = new Shield(stand,4,plugin);

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    stand.remove();
                    shield.remove();
                }
            }, 200);
        }
    }

    public static void GiveArmor(Player p,EffectManager em,Plugin plugin){
        Player currentVictim = null;
        double currentAngle = 90;
        for (Player victim:Bukkit.getOnlinePlayers()) {
            System.out.println(victim);
            if (TeamsInit.getTeamName(victim).equals(TeamsInit.getTeamName(p)) && p != victim) {
                if (p.hasLineOfSight(victim)) {
                    Vector direction = victim.getLocation().toVector().subtract(p.getLocation().toVector());
                    double angle = Math.toDegrees(p.getEyeLocation().getDirection().angle(direction));
                    System.out.println("angle: " + angle);
                    System.out.println();
                    if (angle < 20 && angle < currentAngle && victim.getEyeLocation().distance(p.getLocation()) < 30) {
                        currentAngle = angle;
                        currentVictim = victim;
                    }
                }
            }
        }

        System.out.println("final victim: " + currentVictim);


        if (currentVictim!=null && Mana.spendMana(p,3)){
           LineEffect line = new LineEffect(em);
           line.setLocation(p.getEyeLocation());
           line.setTargetEntity(currentVictim);
           line.particle = Particle.END_ROD;
           em.start(line);

           currentVictim.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,300,0));
           StatusEffects.EnergyShield.add(currentVictim);
           currentVictim.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).addModifier(new AttributeModifier("EnergyShield",1,AttributeModifier.Operation.ADD_NUMBER));

        }
    }

    public static void ProtectionUlt(Player p){
        if (Ultimate.spendUlt(p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 4));
            StatusEffects.protectionAura.put(p, 200);
        }
    }
}

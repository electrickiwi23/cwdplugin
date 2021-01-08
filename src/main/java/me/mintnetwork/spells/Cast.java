package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ShieldEffect;
import de.slikey.effectlib.effect.SphereEffect;
import de.slikey.effectlib.effect.TraceEffect;
import jdk.internal.access.JavaIOFileDescriptorAccess;
import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import sun.jvm.hotspot.oops.Metadata;

public class Cast {

    private final Main plugin;

    public Cast(Main plugin) {
        this.plugin = plugin;
    }

    public void tntBolt(){

    }
    public void NeedleBurst(){

    }
    public static void ShieldDome(Player p,EffectManager em,Plugin plugin){
        ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
        stand.addScoreboardTag("ShieldDome");
        stand.setVisible(false);
        stand.setMarker(true);
        SphereEffect effect = new SphereEffect(em);
        effect.setLocation(p.getLocation().add(0,0,0));
        effect.particle = Particle.END_ROD;
        effect.particleCount = 1;
        effect.radius = 4;
        effect.duration = 15;
        em.start(effect);
        BukkitTask reflection = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (Entity projectile:stand.getNearbyEntities(5,5,5)) {
                    if (projectile instanceof Projectile){
                        if(projectile.getLocation().distance(stand.getLocation())<=4.5) {
                            Vector d = projectile.getVelocity();
                            Vector n = stand.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize().multiply(-1);
                            projectile.setVelocity(d.subtract(n.multiply(d.dot(n)*2)));
                        }
                    }

                }
            }
        },0,1);
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        stand.remove();
                                        reflection.cancel();
                                    }
                                }, 500);
    }
    public static Firework FireworkBolt(Player p){
        if (Mana.spendMana(p,2)) {
            Firework firework = (Firework) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREWORK);
            FireworkEffect.Builder effect = FireworkEffect.builder();
            FireworkMeta meta = firework.getFireworkMeta();
            effect.with(FireworkEffect.Type.BALL);
            double r = (Math.ceil(Math.random() * 6));
            if (r == 1.0) effect.withColor(Color.RED);
            if (r == 2.0) effect.withColor(Color.ORANGE);
            if (r == 3.0) effect.withColor(Color.YELLOW);
            if (r == 4.0) effect.withColor(Color.fromBGR(0, 255, 0));
            if (r == 5.0) effect.withColor(Color.BLUE);
            if (r == 6.0) effect.withColor(Color.fromBGR(255, 0, 255));
            meta.addEffect(effect.build());
            System.out.println(meta.getEffectsSize());
            firework.setShooter(p);
            firework.setShotAtAngle(true);
            firework.setVelocity(p.getEyeLocation().getDirection());
            firework.setFireworkMeta(meta);
            return firework;
        }
        return null;
    }
    //casting code for Air needdles
    public static void AirNeedles(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, 2)) {
            Arrow arrow = p.launchProjectile(Arrow.class);
            TraceEffect effect = new TraceEffect(em);
            effect.setEntity(arrow);
            effect.particle = Particle.SPELL;
            effect.duration = 1;
            effect.disappearWithTargetEntity = true;
            em.start(effect);
            arrow.addScoreboardTag("windArrow");
            arrow.setDamage(1);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    TraceEffect effect = new TraceEffect(em);
                    effect.setEntity(arrow);
                    effect.particle = Particle.SPELL;
                    effect.duration = 1;
                    effect.disappearWithTargetEntity = true;
                    em.start(effect);
                    arrow.addScoreboardTag("windArrow");
                    arrow.setDamage(1);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

                }
            }, 3);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    TraceEffect effect = new TraceEffect(em);
                    effect.setEntity(arrow);
                    effect.particle = Particle.SPELL;
                    effect.duration = 1;
                    effect.disappearWithTargetEntity = true;
                    em.start(effect);
                    arrow.addScoreboardTag("windArrow");
                    arrow.setDamage(1);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                }
            }, 6);
        }
    }
    public static void AirDash(Player p, Plugin plugin){
        p.setGravity(false);
        p.setVelocity(p.getVelocity().add(p.getEyeLocation().getDirection()));
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                p.setGravity(true);
            }
        }, 10);
    }

}

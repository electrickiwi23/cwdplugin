package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.*;

public class StatusEffects {

    private final Main plugin;

    public StatusEffects(Main plugin) {
        this.plugin = plugin;
    };

    public static Collection<Player> ShadowInvis = new ArrayList<Player>();

    public static Collection<Player> getShadowInvis(){return ShadowInvis;}

    public static Map<Player,Runnable> ShadowCancel = new HashMap<>();

    public static Map<Player,Runnable> getShadowCancel(){return ShadowCancel;  }

    public static Map<LivingEntity, Integer> paintTimer = new HashMap<LivingEntity, Integer>();

    public static Map<LivingEntity, Integer> getPaintTimer(){return paintTimer; }

    public static Map<LivingEntity, Integer> speedTimer = new HashMap<>();

    public static Map<LivingEntity, Integer> getSpeedTimer(){return speedTimer; }

    public static Map<Player, LivingEntity> ShadowGrappled = new HashMap<>();

    public static Map<Player, LivingEntity> getShadowGrappled(){return ShadowGrappled; }

    public static Map<LivingEntity, Player> ShadowGrappler = new HashMap<>();

    public static Map<LivingEntity, Player> getShadowGrappler(){ return  ShadowGrappler; }

    public static Map<LivingEntity, Integer> ShadowGrappleTimer = new HashMap<>();

    public static Map<LivingEntity, Integer> getShadowGrappleTimer(){return ShadowGrappleTimer; }

    public static Map<LivingEntity, Entity> ShadowGrappleStand = new HashMap<>();

    public static Map<LivingEntity, Entity> getShadowGrappleStand(){return ShadowGrappleStand;}

    public void statusEffects(Main plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (LivingEntity e: paintTimer.keySet()) {
                    paintTimer.replace(e,paintTimer.get(e)-1);
                    double r = (Math.ceil(Math.random() * 6));

                    Particle.DustOptions dust = null;
                    if (r == 1.0) dust = new Particle.DustOptions(org.bukkit.Color.RED, 1);
                    if (r == 2.0) dust = new Particle.DustOptions(org.bukkit.Color.ORANGE, 1);
                    if (r == 3.0) dust = new Particle.DustOptions(org.bukkit.Color.YELLOW, 1);
                    if (r == 4.0) dust = new Particle.DustOptions(org.bukkit.Color.fromBGR(0, 255, 0), 1);
                    if (r == 5.0) dust = new Particle.DustOptions(org.bukkit.Color.BLUE, 1);
                    if (r == 6.0) dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 1);

                    e.getWorld().spawnParticle(Particle.REDSTONE,e.getLocation().add(0,1,0),1,.25,.45,.25,0, dust);

                    if (paintTimer.get(e)<=0){
                        paintTimer.remove(e);
                    }
                    if (e.isDead()){
                        paintTimer.remove(e);
                    }
                }
                for (LivingEntity e: speedTimer.keySet()) {
                    speedTimer.replace(e,speedTimer.get(e)-1);

                    if (speedTimer.get(e)<=0){
                        speedTimer.remove(e);
                    }
                    if (e.isDead()){
                        speedTimer.remove(e);
                    }

                }
                for (LivingEntity e: ShadowGrappleTimer.keySet()) {
                    ShadowGrappleTimer.replace(e,ShadowGrappleTimer.get(e)+1);
                    if (ShadowGrappleTimer.get(e)>=60){
                        ShadowGrappleCancel(e);
                    }
                }
            }

        }, 0, 2);
    }

    public static void ShadowGrappleCancel(LivingEntity e){
        e.setCustomNameVisible(true);

        e.removePotionEffect(PotionEffectType.BLINDNESS);
        e.removePotionEffect(PotionEffectType.WEAKNESS);
        ShadowGrappler.get(e).removePotionEffect(PotionEffectType.WEAKNESS);

        if (e.getVehicle()!=null){
            Entity vehicle = e.getVehicle();
            vehicle.eject();
            vehicle.remove();

        }

        ShadowGrappleTimer.remove(e);
        ShadowGrappled.remove(ShadowGrappler.get(e));
        ShadowGrappler.remove(e);
    }




}

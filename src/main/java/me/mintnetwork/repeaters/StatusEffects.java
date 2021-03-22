package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class StatusEffects {

    public static Collection<Player> ShadowInvis = new ArrayList<>();

    public static Map<Player,Runnable> ShadowCancel = new HashMap<>();

    public static Map<Player,Runnable> getShadowCancel(){return ShadowCancel;  }

    public static Map<LivingEntity, Integer> paintTimer = new HashMap<>();

    public static Map<LivingEntity, Integer> speedTimer = new HashMap<>();

    public static Map<Player, LivingEntity> ShadowGrappled = new HashMap<>();

    public static Map<Player, LivingEntity> getShadowGrappled(){return ShadowGrappled; }

    public static Map<LivingEntity, Player> ShadowGrappler = new HashMap<>();

    public static Map<LivingEntity, Player> getShadowGrappler(){ return  ShadowGrappler; }

    public static Map<LivingEntity, Integer> ShadowGrappleTimer = new HashMap<>();

    public static Map<LivingEntity, Integer> getShadowGrappleTimer(){return ShadowGrappleTimer; }

    public static Map<LivingEntity, Entity> ShadowGrappleStand = new HashMap<>();

    public static Map<LivingEntity, Entity> getShadowGrappleStand(){return ShadowGrappleStand;}

    public static Map<Block, Integer> ObsidianDecay = new HashMap<>();

    public static Map<LivingEntity, Integer> BloodWeak = new HashMap<>();

    public static Map<LivingEntity, Integer> stunSong = new HashMap<>();

    public static Map<LivingEntity, Integer> healSong = new HashMap<>();

    public static Map<LivingEntity, Integer> speedSong = new HashMap<>();

    public static Map<Player, Integer> ShadowConsumed = new HashMap<>();

    public static boolean CanCast(Player p){

        if (ShadowGrappler.containsKey(p)) return false;
        if (stunSong.containsKey(p)) return false;
        if (ShadowConsumed.containsKey(p)) return false;

        //ugly code for if you are in a tornado
        for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(),7,16,7)) {
            if (e instanceof ArmorStand) {
                if (ProjectileInfo.TornadoTeam.containsKey(e)) {

                    String teamName = "Red";
                    if (!ProjectileInfo.TornadoTeam.get(e).equals(teamName)) {

                        Location entityLocation = p.getLocation().clone();
                        entityLocation.setY(e.getLocation().getY());
                        if (entityLocation.distance(e.getLocation()) <= 6) {
                            if (p.getLocation().getY() >= e.getLocation().getY() - 1) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public void statusEffects(Main plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            List<Block> removeBlocks = new ArrayList<>();
            for (Block b: ObsidianDecay.keySet()) {

                ObsidianDecay.replace(b,ObsidianDecay.get(b)+1);
                if(ObsidianDecay.get(b)==700){
                    b.setType(Material.BLACKSTONE);
                    removeBlocks.add(b);
                }
            }
            for (Block r: removeBlocks) {
                ObsidianDecay.remove(r);
            }
            removeBlocks.clear();

            for (LivingEntity e: BloodWeak.keySet()){
                Particle.DustOptions dustCloud = new Particle.DustOptions(Color.RED, 2);
                e.getWorld().spawnParticle(Particle.REDSTONE,e.getLocation().add(0,1,0),2,.2,.4,.2,0,dustCloud);
                BloodWeak.replace(e, BloodWeak.get(e)-1);
                if (BloodWeak.get(e)<=0){
                    BloodWeak.remove(e);
                }
                if (e.isDead()){
                    BloodWeak.remove(e);
                }
            }

            for (LivingEntity e: paintTimer.keySet()) {
                paintTimer.replace(e,paintTimer.get(e)-1);
                double r = (Math.ceil(Math.random() * 6));

                Particle.DustOptions dust = null;
                if (r == 1.0) dust = new Particle.DustOptions(Color.RED, 1);
                if (r == 2.0) dust = new Particle.DustOptions(Color.ORANGE, 1);
                if (r == 3.0) dust = new Particle.DustOptions(Color.YELLOW, 1);
                if (r == 4.0) dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 1);
                if (r == 5.0) dust = new Particle.DustOptions(Color.BLUE, 1);
                if (r == 6.0) dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 1);

                e.getWorld().spawnParticle(Particle.REDSTONE,e.getLocation().add(0,1,0),1,.25,.45,.25,0, dust);

                if (paintTimer.get(e)<=0){
                    paintTimer.remove(e);
                }
                if (e.isDead()){
                    paintTimer.remove(e);
                }
            }

//start of bard song code------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

            List<LivingEntity> removeStunSong = new ArrayList<>();
            for (LivingEntity e: stunSong.keySet()){

                boolean hasSpeedSong = speedSong.containsKey(e);
                boolean hasHealSong = healSong.containsKey(e);

                long x = e.getWorld().getFullTime();
                Location direction = e.getLocation().clone();
                direction.setPitch(0);

                if (hasHealSong&&hasSpeedSong) {
                    direction.setYaw((x % 40) * 9 - 300);
                } else{
                    direction.setYaw((x % 40) * 9 - 180);
                }



                if (e instanceof Player) {
                    if (stunSong.get(e) == 45||stunSong.get(e) == 70) {
                        ((Player) e).playSound(e.getLocation(),Sound.BLOCK_BELL_RESONATE,1,1);
                    }
                }

                if (hasHealSong&&!hasSpeedSong){
                    e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(-.7)).add(0,.7,0), 0, .6, .2, .92,1, null);
                } else {
                    e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(.7)).add(0, .7, 0), 0, .6, .2, .92, 1, null);
                }

                stunSong.replace(e, stunSong.get(e)-1);
                if (stunSong.get(e)<=0) removeStunSong.add(e);
            }

            for (LivingEntity r: removeStunSong) {
                stunSong.remove(r);
            }
            removeStunSong.clear();

            List<LivingEntity> removeSpeedSong = new ArrayList<>();
            for (LivingEntity e: speedSong.keySet()){

                boolean hasStunSong = stunSong.containsKey(e);
                boolean hasHealSong = healSong.containsKey(e);

                long x = e.getWorld().getFullTime();
                Location direction = e.getLocation().clone();
                direction.setPitch(0);

                if (hasHealSong&&hasStunSong) {
                    direction.setYaw((x % 40) * 9 - 60);
                    e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(.7)).add(0,.7,0), 0, .25, .15, .15,1, null);
                } else{
                    direction.setYaw((x % 40) * 9 - 180);
                    e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(-.7)).add(0, .7, 0), 0, .25, .15, .15, 1, null);
                }

                speedSong.replace(e, speedSong.get(e)-1);
                if (speedSong.get(e)<=0) removeSpeedSong.add(e);
            }

            for (LivingEntity r: removeSpeedSong) {
                speedSong.remove(r);
            }
            removeSpeedSong.clear();

            List<LivingEntity> removeHealSong = new ArrayList<>();
            for (LivingEntity e: healSong.keySet()){

                long x = e.getWorld().getFullTime();
                Location direction = e.getLocation().clone();
                direction.setPitch(0);

                direction.setYaw((x % 40) * 9 - 180);
                e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(.7)).add(0,.7,0), 0, .9, .15, .15, 1, null);

                healSong.replace(e, healSong.get(e)-1);
                if (healSong.get(e)<=0) removeHealSong.add(e);
            }

            for (LivingEntity r: removeHealSong) {
                healSong.remove(r);
            }
            removeHealSong.clear();

//end of bard songs---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

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

            List<Player> removeShadowConsume = new ArrayList<>();
            for (Player e: ShadowConsumed.keySet()){
                String teamName = "Red";

                Particle.DustOptions dustCloud = new Particle.DustOptions(Color.BLACK, 3);
                for (Player p:Bukkit.getOnlinePlayers()){
                    if (e!=p){
                        if (e.getLocation().distance(p.getLocation())<=10){
                            e.spawnParticle(Particle.REDSTONE,p.getLocation().add(0,1,0),5,.2,.4,.2,0,dustCloud);
                        }
                    }
                    p.spawnParticle(Particle.REDSTONE,e.getEyeLocation(),1,.1,.1,.1,0,dustCloud);
                }

                if (ShadowConsumed.get(e)%40==0) {
                    if (Math.random() < .5) {
                        Location l = e.getEyeLocation().clone();
                        l.setYaw((float) (l.getYaw() + Math.random() * 360));
                        l.setPitch(0);
                        e.playSound(e.getEyeLocation().add(l.getDirection().multiply(5)), Sound.AMBIENT_CAVE, 1, 1);
                    } else {
                        ArrayList<Player> validNoise = new ArrayList<>();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (e != p) {
                                if (e.getLocation().distance(p.getLocation()) <= 10) {
                                    String victimTeam = "Blue";
                                    if (!teamName.equals(victimTeam)) {
                                        validNoise.add(p);
                                    }
                                }
                            }
                        }
                        int rnd = new Random().nextInt(validNoise.size());
                        Player p = validNoise.get(rnd);
                        e.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
                    }
                }


                ShadowConsumed.replace(e, ShadowConsumed.get(e)-1);

                if (e.isDead()) ShadowConsumed.replace(e,0);

                if (ShadowConsumed.get(e)<=0){
                    removeShadowConsume.add(e);
                    e.resetPlayerTime();
                    e.resetPlayerWeather();
                }

            }

            for (Player r: removeShadowConsume) {
                ShadowConsumed.remove(r);
            }
            removeShadowConsume.clear();
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

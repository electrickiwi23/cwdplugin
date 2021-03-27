package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

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

    public static Map<Block, Integer> SnowSlow = new HashMap<>();

    public static Map<LivingEntity, Integer> BloodWeak = new HashMap<>();

    public static Map<LivingEntity, Integer> stunSong = new HashMap<>();

    public static Map<LivingEntity, Integer> healSong = new HashMap<>();

    public static Map<LivingEntity, Integer> speedSong = new HashMap<>();

    public static Map<Player, Double> bardInspiration = new HashMap<>();

    public static Map<Player, Integer> sirenSong = new HashMap<>();

    public static Map<Player, Integer> ShadowConsumed = new HashMap<>();

    public static ArrayList<Player> cloudFloating = new ArrayList<>();

    public static  Map<Player, Integer> RageUlt = new HashMap<>();

    public static Map<Player, Integer> healTeam = new HashMap<>();

    public void statusEffects(Main plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                List<Block> removeObsidian = new ArrayList<>();
                for (Block b : ObsidianDecay.keySet()) {

                    ObsidianDecay.replace(b, ObsidianDecay.get(b) + 1);
                    if (ObsidianDecay.get(b) == 700) {
                        b.setType(Material.BLACKSTONE);
                        removeObsidian.add(b);
                    }
                    if (b.getType().isAir()) removeObsidian.add(b);
                }
                for (Block r : removeObsidian) {
                    ObsidianDecay.remove(r);
                }
                removeObsidian.clear();

                List<Block> removeSnow = new ArrayList<>();
                for (Block b : SnowSlow.keySet()) {

                    for (Entity e : b.getWorld().getNearbyEntities(b.getLocation().add(.5, .5, .5), 1, 1.5, 1)) {
                        if (e instanceof LivingEntity) {
                            ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4, 1,false,false));
                        }
                    }

                    SnowSlow.replace(b, SnowSlow.get(b) + 1);



                    if (SnowSlow.get(b) >= 200) {
                        removeSnow.add(b);
                        b.setType(Material.AIR);
                    }

                    if (b.getType().isAir()) removeSnow.add(b);
                }
                for (Block r : removeSnow) {
                    SnowSlow.remove(r);
                }
                removeSnow.clear();

                for (Player p : cloudFloating) {
                    p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation().add(0,-1,0), 1, .1, .1, .1, 0);
                }

                for (Player p: RageUlt.keySet()){
                    RageUlt.replace(p, RageUlt.get(p) - 1);
                    Particle.DustOptions dustCloud = new Particle.DustOptions(Color.RED, 3);
                    for (Player e : Bukkit.getOnlinePlayers()) {
                        if (e != p) {
                            e.spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 1, 0), 3, .2, .4, .2, 0, dustCloud);
                        }
                    }

                    if (p.isDead()){
                        RageUlt.replace(p,0);
                    }

                    if (RageUlt.get(p) <= 0) {
                        RageUlt.remove(p);

                        for (ItemStack i:p.getInventory().getContents()) {
                            if (i.getType().equals(Material.IRON_SWORD)){
                                i.removeEnchantment(Enchantment.KNOCKBACK);
                            }
                        }

                    }

                }

                for (LivingEntity e : BloodWeak.keySet()) {
                    Particle.DustOptions dustCloud = new Particle.DustOptions(Color.RED, 2);
                    e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 2, .2, .4, .2, 0, dustCloud);
                    BloodWeak.replace(e, BloodWeak.get(e) - 1);
                    if (BloodWeak.get(e) <= 0) {
                        BloodWeak.remove(e);
                    }
                    if (e.isDead()) {
                        BloodWeak.remove(e);
                    }
                }

                for (LivingEntity e : paintTimer.keySet()) {
                    paintTimer.replace(e, paintTimer.get(e) - 2);
                    double r = (Math.ceil(Math.random() * 6));

                    if (paintTimer.get(e)>=1500){
                        e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20,1));
                        paintTimer.replace(e, paintTimer.get(e) - 600);

                        BukkitTask task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 5, .2, .4, .2, 0, dust, true);
                                dust = new Particle.DustOptions(Color.ORANGE, 2);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 5, .2, .4, .2, 0, dust, true);
                                dust = new Particle.DustOptions(Color.YELLOW, 2);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 5, .2, .4, .2, 0, dust, true);
                                dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 2);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 5, .2, .4, .2, 0, dust, true);
                                dust = new Particle.DustOptions(Color.BLUE, 2);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 5, .2, .4, .2, 0, dust, true);
                                dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 2);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 5, .2, .4, .2, 0, dust, true);
                            }
                        }.runTaskTimer(plugin, 1, 1);
                        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                e.damage(5);
                                task.cancel();
                            }
                        }, 25);

                    }

                    for (int i = 0; i <Math.ceil(paintTimer.get(e)/500.0); i++) {

                        Particle.DustOptions dust = null;
                        if (r == 1.0) dust = new Particle.DustOptions(Color.RED, 1);
                        if (r == 2.0) dust = new Particle.DustOptions(Color.ORANGE, 1);
                        if (r == 3.0) dust = new Particle.DustOptions(Color.YELLOW, 1);
                        if (r == 4.0) dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 1);
                        if (r == 5.0) dust = new Particle.DustOptions(Color.BLUE, 1);
                        if (r == 6.0) dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 1);

                        e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 1, .25, .45, .25, 0, dust);

                    }
                    if (paintTimer.get(e) <= 0) {
                        paintTimer.remove(e);
                    }
                    if (e.isDead()) {
                        paintTimer.remove(e);
                    }
                }

//start of bard song code------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

                List<LivingEntity> removeStunSong = new ArrayList<>();
                for (LivingEntity e : stunSong.keySet()) {

                    boolean hasSpeedSong = speedSong.containsKey(e);
                    boolean hasHealSong = healSong.containsKey(e);

                    long x = e.getWorld().getFullTime();
                    Location direction = e.getLocation().clone();
                    direction.setPitch(0);

                    if (hasHealSong && hasSpeedSong) {
                        direction.setYaw((x % 40) * 9 - 300);
                    } else {
                        direction.setYaw((x % 40) * 9 - 180);
                    }


                    if (e instanceof Player) {
                        if (stunSong.get(e) == 45 || stunSong.get(e) == 70) {
                            ((Player) e).playSound(e.getLocation(), Sound.BLOCK_BELL_RESONATE, 1, 1);
                        }
                    }

                    if (hasHealSong && !hasSpeedSong) {
                        e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(-.7)).add(0, .7, 0), 0, .6, .2, .92, 1, null);
                    } else {
                        e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(.7)).add(0, .7, 0), 0, .6, .2, .92, 1, null);
                    }

                    stunSong.replace(e, stunSong.get(e) - 1);
                    if (stunSong.get(e) <= 0) removeStunSong.add(e);
                }

                for (LivingEntity r : removeStunSong) {
                    stunSong.remove(r);
                }
                removeStunSong.clear();

                List<LivingEntity> removeSpeedSong = new ArrayList<>();
                for (LivingEntity e : speedSong.keySet()) {

                    boolean hasStunSong = stunSong.containsKey(e);
                    boolean hasHealSong = healSong.containsKey(e);

                    long x = e.getWorld().getFullTime();
                    Location direction = e.getLocation().clone();
                    direction.setPitch(0);

                    if (hasHealSong && hasStunSong) {
                        direction.setYaw((x % 40) * 9 - 60);
                        e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(.7)).add(0, .7, 0), 0, .25, .15, .15, 1, null);
                    } else {
                        direction.setYaw((x % 40) * 9 - 180);
                        e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(-.7)).add(0, .7, 0), 0, .25, .15, .15, 1, null);
                    }

                    speedSong.replace(e, speedSong.get(e) - 1);
                    if (speedSong.get(e) <= 0) removeSpeedSong.add(e);
                }

                for (LivingEntity r : removeSpeedSong) {
                    speedSong.remove(r);
                }
                removeSpeedSong.clear();

                List<LivingEntity> removeHealSong = new ArrayList<>();
                for (LivingEntity e : healSong.keySet()) {

                    long x = e.getWorld().getFullTime();
                    Location direction = e.getLocation().clone();
                    direction.setPitch(0);

                    direction.setYaw((x % 40) * 9 - 180);
                    e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(direction.getDirection().multiply(.7)).add(0, .7, 0), 0, .9, .15, .15, 1, null);

                    healSong.replace(e, healSong.get(e) - 1);
                    if (healSong.get(e) <= 0) removeHealSong.add(e);
                }

                for (LivingEntity r : removeHealSong) {
                    healSong.remove(r);
                }
                removeHealSong.clear();


                List<Player> removeInspiration = new ArrayList<>();
                for(Player p: bardInspiration.keySet()){
                    String teamName = TeamsInit.getTeamName(p);
                    for (Player b:Bukkit.getOnlinePlayers()) {

                        if (WizardInit.playersWizards.get(b).ClassID.equals("bard")){
                            String bardTeamName = TeamsInit.getTeamName(b);
                            if (bardTeamName.equals(teamName)){
                                if (b.getLocation().distance(p.getLocation())<=6) {
                                    bardInspiration.replace(p, Math.ceil(bardInspiration.get(p)));
                                }
                            }
                        }
                    }

                    bardInspiration.replace(p,bardInspiration.get(p)-.01);
                    if (bardInspiration.get(p) <= 0) {
                        removeInspiration.add(p);
                    }
                }
                for (Player r: removeInspiration) {
                    bardInspiration.remove(r);
                }
                removeInspiration.clear();


                for(Player p: sirenSong.keySet()){
                    p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,p.getLocation().add(0,1,0),2,.3,.5,.3,0);


                    for (Entity e:p.getNearbyEntities(30,30,30)){
                        if (e instanceof LivingEntity){
                            if (e.getLocation().distance(p.getLocation())<=30){
                                if (((LivingEntity) e).hasLineOfSight(p)){
                                    e.teleport(e.getLocation().setDirection(p.getLocation().toVector().subtract(e.getLocation().toVector()).normalize()));
                                    e.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,((LivingEntity) e).getEyeLocation(),1,.25,.25,.25,0);
                                }
                            }
                        }
                    }
                    sirenSong.replace(p, sirenSong.get(p) - 1);
                    if (p.isDead()){
                        sirenSong.replace(p,0);
                    }
                    if (sirenSong.get(p) <= 0) {
                        sirenSong.remove(p);
                    }

                }

//end of bard songs---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

                List<Player> removeHealUlt = new ArrayList<>();
                for (Player p : healTeam.keySet()) {
                    long x = p.getWorld().getFullTime();
                    Location direction = p.getLocation().clone();
                    direction.setPitch(0);
                    direction.setYaw((x % 20) * 18 - 180);

                    p.getWorld().spawnParticle(Particle.HEART,p.getLocation().add(direction.getDirection()).add(0,healTeam.get(p)/12.0,0),1,.1,.1,.1,0);
                    p.playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP, (float) .4, (float) (healTeam.get(p)/20.0+.5));

                    healTeam.replace(p, healTeam.get(p) + 1);

                    if (healTeam.get(p) >= 30) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,1,10));
                        p.getWorld().spawnParticle(Particle.HEART,p.getLocation().add(0,1,0),10,.3,.4,.3,0);




                        removeHealUlt.add(p);
                    }
                    if (p.isDead()) {
                        removeHealUlt.add(p);
                    }
                }
                for (Player r: removeHealUlt) {
                    healTeam.remove(r);
                }
                removeHealUlt.clear();

                for (LivingEntity e : speedTimer.keySet()) {
                    speedTimer.replace(e, speedTimer.get(e) - 1);

                    if (speedTimer.get(e) <= 0) {
                        speedTimer.remove(e);
                    }
                    if (e.isDead()) {
                        speedTimer.remove(e);
                    }

                }

                for (LivingEntity e : ShadowGrappleTimer.keySet()) {
                    ShadowGrappleTimer.replace(e, ShadowGrappleTimer.get(e) + 1);
                    if (ShadowGrappleTimer.get(e) >= 60) {
                        ShadowGrappleCancel(e);
                    }
                }

                List<Player> removeShadowConsume = new ArrayList<>();
                for (Player e : ShadowConsumed.keySet()) {
                    String teamName = TeamsInit.getTeamName(e);

                    Particle.DustOptions dustCloud = new Particle.DustOptions(Color.BLACK, 3);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (e != p) {
                            if (e.getLocation().distance(p.getLocation()) < 10) {
                                e.spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 1, 0), 5, .2, .4, .2, 0, dustCloud);
                            }
                        }
                        p.spawnParticle(Particle.REDSTONE, e.getEyeLocation(), 1, .1, .1, .1, 0, dustCloud);
                    }

                    ArrayList<Player> validNoise = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (e != p) {
                            if (e.getLocation().distance(p.getLocation()) <= 30) {
                                String victimTeam = TeamsInit.getTeamName(p);
                                if (!teamName.equals(victimTeam)) {
                                    validNoise.add(p);
                                }
                            }
                        }
                    }

                    if (ShadowConsumed.get(e) % 40 == 0) {
                        if (Math.random() < .5||validNoise.isEmpty()) {
                            Location l = e.getEyeLocation().clone();
                            l.setYaw((float) (l.getYaw() + Math.random() * 360));
                            l.setPitch(0);
                            e.playSound(e.getEyeLocation().add(l.getDirection().multiply(5)), Sound.AMBIENT_CAVE, 1, 1);
                        } else {


                        }
                    }


                    ShadowConsumed.replace(e, ShadowConsumed.get(e) - 1);

                    if (e.isDead()) ShadowConsumed.replace(e, 0);

                    if (ShadowConsumed.get(e) <= 0) {
                        removeShadowConsume.add(e);
                        e.resetPlayerTime();
                        e.resetPlayerWeather();
                    }

                }

                for (Player r : removeShadowConsume) {
                    ShadowConsumed.remove(r);
                }
                removeShadowConsume.clear();

            }
        }, 0, 2);
    }

    public static boolean CanCast(Player p){

        if (ShadowGrappler.containsKey(p)) return false;
        if (stunSong.containsKey(p)) return false;
        if (ShadowConsumed.containsKey(p)) return false;
        if (!GameStart.gameRunning) return false;

        for (Player e:Bukkit.getOnlinePlayers()) {
            if (sirenSong.containsKey(e)) {
                if (e.getLocation().distance(p.getLocation()) <= 30) {
                    if (e.hasLineOfSight(p)) {
                        return false;
                    }
                }
            }
        }

        //ugly code for if you are in a tornado
        for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(),7,16,7)) {
            if (e instanceof ArmorStand) {
                if (ProjectileInfo.TornadoTeam.containsKey(e)) {
                    String teamName = TeamsInit.getTeamName(e);
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

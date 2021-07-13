package me.mintnetwork.repeaters;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import me.mintnetwork.Main;
import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.Objects.ShadowGrapple;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.spells.BloodMage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

    public static Map<Block, Integer> ObsidianDecay = new HashMap<>();

    public static Map<Block, Integer> SnowSlow = new HashMap<>();

    public static Map<LivingEntity, Integer> BloodWeak = new HashMap<>();

    public static Map<LivingEntity, Integer> stunSong = new HashMap<>();

    public static Map<LivingEntity, Integer> healSong = new HashMap<>();

    public static Map<LivingEntity, Integer> speedSong = new HashMap<>();

    public static Map<Player, Double> bardInspiration = new HashMap<>();

    public static Map<Player, Integer> sirenSong = new HashMap<>();

    public static Map<Player, Integer> protectionAura = new HashMap<>();

    public static Map<Player, Integer> ShadowConsumed = new HashMap<>();

    public static ArrayList<Player> cloudFloating = new ArrayList<>();

    public static ArrayList<Player> UsingMove = new ArrayList<>();

    public static ArrayList<Player> EnergyShield = new ArrayList<>();

    public static Map<Player, Integer> RageUlt = new HashMap<>();

    public static Map<Player, Integer> healTeam = new HashMap<>();

    public static Map<Player, Integer> activeBloodUlt = new HashMap<>();

    public static Map<Player, HashMap<Player,Integer>> bloodLink = new HashMap<>();

    public static ArrayList<Player> stormUlt = new ArrayList<>();

    public void statusEffects(Main plugin) {
        EffectManager em = new EffectManager(plugin);
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                ArrayList<Player> RemoveActiveBloodUlt = new ArrayList<>();
                for (Player p:activeBloodUlt.keySet()){
                    activeBloodUlt.replace(p,activeBloodUlt.get(p)-1);
                    p.getWorld().spawnParticle(Particle.REDSTONE,p.getLocation().add(0,1,0),1,.25,.5,.25,0,new Particle.DustOptions(Color.RED,1));

                    for (Player v:bloodLink.get(p).keySet()) {

                        for (int i = 1; i < 5; i++) {
                            Vector vector = v.getLocation().toVector().subtract(p.getLocation().toVector());

                            p.getWorld().spawnParticle(Particle.REDSTONE,p.getLocation().add(0,1,0).add(vector.normalize().multiply(i*.5)),1,0,0,0,new Particle.DustOptions(Color.RED,1));

                            v.getWorld().spawnParticle(Particle.REDSTONE,v.getLocation().add(0,1,0).add(vector.normalize().multiply(i*-.5)),1,0,0,0,new Particle.DustOptions(Color.RED,1));
                        }

                    }

                    if (activeBloodUlt.get(p)<=0||p.isDead()) {
                        RemoveActiveBloodUlt.add(p);
                    }
                }
                if (!RemoveActiveBloodUlt.isEmpty()) {
                    for (Player r : RemoveActiveBloodUlt) {
                        activeBloodUlt.remove(r);
                    }
                }
                RemoveActiveBloodUlt.clear();

                for (Player p:bloodLink.keySet()){
                    ArrayList<Player> RemoveLink = new ArrayList<>();
                    HashMap<Player, Integer> LinkedHashmap = bloodLink.get(p);

                    for (Player v: LinkedHashmap.keySet()){
                        if (p.isDead()||v.isDead()) {
                            RemoveLink.add(v);
                        }
                        LinkedHashmap.replace(v,LinkedHashmap.get(v)-1);
                        if (LinkedHashmap.get(v)<=0) {
                            RemoveLink.add(v);
                        }
                    }
                    if (!RemoveLink.isEmpty()) {
                        for (Player r : RemoveLink) {
                            LinkedHashmap.remove(r);
                        }
                        BloodMage.UpdateUlt();
                    }
                    RemoveLink.clear();

                }

                List<Block> removeObsidian = new ArrayList<>();
                for (Block b : ObsidianDecay.keySet()) {

                    ObsidianDecay.replace(b, ObsidianDecay.get(b) + 1);
                    if (ObsidianDecay.get(b) == 700) {
                        b.setType(Material.BLACKSTONE);
                        new DecayBlock(200,.5F,b);
                        removeObsidian.add(b);
                    }
                    if (b.getType().isAir()) removeObsidian.add(b);
                }
                for (Block r : removeObsidian) {
                    ObsidianDecay.remove(r);
                }
                removeObsidian.clear();

                for (int i = 0; i < EnergyShield.size(); i++) {
                    Player p = EnergyShield.get(i);
                    p.getWorld().spawnParticle(Particle.WAX_OFF, p.getLocation().add(0, 1, 0), 1, -.2, .4, -.2, 0);
                    if (!p.hasPotionEffect(PotionEffectType.ABSORPTION)){
                        EnergyShield.remove(p);
                        i--;
                    }
                }

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

                List<Player> removeProtection = new ArrayList<>();
                for (Player p:protectionAura.keySet()) {
                    for (Player victim:Bukkit.getOnlinePlayers()) {
                        if (victim.getLocation().distance(p.getLocation())<15&&TeamsInit.getTeamName(p).equals(TeamsInit.getTeamName(victim))){
                            LineEffect line = new LineEffect(em);
                            line.particle = Particle.ELECTRIC_SPARK;
                            line.setEntity(p);
                            line.setLocation(p.getLocation().add(0,1,0));
                            line.setTargetEntity(victim);
                            line.setTargetLocation(victim.getLocation().add(0,1,0));
                            line.targetOffset = new Vector(0,-1,0);
                            line.iterations=2;
                            em.start(line);
                        }
                    }

                    protectionAura.replace(p,protectionAura.get(p)-1);

                    if (protectionAura.get(p) <= 0) {
                        removeProtection.add(p);
                    }
                    if (p.isDead()) {
                        removeProtection.add(p);
                    }
                }
                for (Player r : removeProtection) {
                    protectionAura.remove(r);
                }
                removeProtection.clear();


                for (Player p : cloudFloating) {
                    if (stormUlt.contains(p)){
                        p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,-1,0), 1, .1, .1, .1, 0,new Particle.DustOptions(Color.fromBGR(80,80,80), 3));
                    } else p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation().add(0,-1,0), 1, .1, .1, .1, 0);
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
                            if (i!=null) {
                                if (i.getType().equals(Material.STONE_SWORD)) {
                                    i.removeEnchantment(Enchantment.KNOCKBACK);
                                }
                            }
                        }

                    }

                }

                for (int i = 0; i < stormUlt.size(); i++) {
                    Player p = stormUlt.get(i);
                    for (Player e : Bukkit.getOnlinePlayers()) {
                        if (e != p) {
                            p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 1, 0), 2, .2, .4, .2, 0, new Particle.DustOptions(Color.fromBGR(80,80,80), 2));
                        }
                    }
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 1, 0), 1, .2, .4, .2, 0, new Particle.DustOptions(Color.YELLOW, 1));
                    if (p.isDead()){
                        stormUlt.remove(p);
                        i--;
                    }
                }




                List<LivingEntity> removeBloodWeak = new ArrayList<>();

                for (LivingEntity e : BloodWeak.keySet()) {
                    Particle.DustOptions dustCloud = new Particle.DustOptions(Color.RED, 2);
                    e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 2, .2, .4, .2, 0, dustCloud);
                    BloodWeak.replace(e, BloodWeak.get(e) - 1);
                    if (BloodWeak.get(e) <= 0) {
                        removeBloodWeak.add(e);
                    }
                    if (e.isDead()) {
                        removeBloodWeak.add(e);
                    }
                }
                for (LivingEntity r:removeBloodWeak) {
                    BloodWeak.remove(r);
                }
                removeBloodWeak.clear();

                List<LivingEntity> removePaintTimer = new ArrayList<>();

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
                        removePaintTimer.add(e);
                    }
                    if (e.isDead()) {
                        removePaintTimer.add(e);
                    }
                }
                for (LivingEntity r : removePaintTimer) {
                    paintTimer.remove(r);
                }
                removePaintTimer.clear();

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

                        if (WizardInit.playersWizards.get(b.getUniqueId()).kitID.equals(Kit.BARD)){
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
                    String teamName = TeamsInit.getTeamName(p);


                    for (Entity e:p.getNearbyEntities(30,30,30)){
                        if (e instanceof LivingEntity){
                            if (!teamName.equals(TeamsInit.getTeamName(e))) {
                                if (e.getLocation().distance(p.getLocation()) <= 30) {
                                    if (((LivingEntity) e).hasLineOfSight(p)) {
                                        Vector v = e.getVelocity().clone();
                                        e.teleport(e.getLocation().setDirection(p.getLocation().toVector().subtract(e.getLocation().toVector()).normalize()));
                                        e.setVelocity(v);
                                        e.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, ((LivingEntity) e).getEyeLocation(), 1, .25, .25, .25, 0);
                                    }
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

                    p.getWorld().spawnParticle(Particle.HEART,p.getLocation().add(direction.getDirection()).add(0,healTeam.get(p)/8.0,0),1,.1,.1,.1,0);
                    p.playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP, (float) .4, (float) (healTeam.get(p)/20.0+.5));

                    healTeam.replace(p, healTeam.get(p) + 1);

                    if (healTeam.get(p) >= 20) {
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

                for (int i = 0; i < ShadowGrapple.allGrapples.size(); i++) {
                    ShadowGrapple grapple = ShadowGrapple.allGrapples.get(i);
                    if (grapple.tick()){
                        i--;
                    }
                    if (grapple.getVictim() instanceof Player) grapple.getShadow().spawnParticle(Particle.SQUID_INK,grapple.getShadow().getEyeLocation().add(0,2,0),2,.2,.2,.2,0);

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

        String teamName = TeamsInit.getTeamName(p);

        for (ShadowGrapple grapple:ShadowGrapple.allGrapples) {
            if (grapple.getVictim()==p) return false;
        }

        if (stunSong.containsKey(p)) return false;
        if (ShadowConsumed.containsKey(p)) return false;
        if (UsingMove.contains(p)) return false;
        if (!GameStart.gameRunning) return false;

        for (Player e:Bukkit.getOnlinePlayers()) {
            if (sirenSong.containsKey(e)) {
                if (!teamName.equals(TeamsInit.getTeamName(e))) {
                    if (e.getLocation().distance(p.getLocation()) <= 30) {
                        if (e.hasLineOfSight(p)) {
                            return false;
                        }
                    }
                }
            }
        }

//        ugly code for if you are in a tornado

        return true;
    }


}

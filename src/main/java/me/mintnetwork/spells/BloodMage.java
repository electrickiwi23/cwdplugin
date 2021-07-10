package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BloodMage extends KitItems {


    public BloodMage(){
        ultTime = Utils.BLOOD_MAGE_ULT_TIME;

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta1 = wand1.getItemMeta();


        wands.add(wand1);
        //create itemstacks for each wand of the class
    }

    public static void BloodBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, Utils.BLOOD_BOLT_COST)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(1.3));
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.RED_DYE));
            bolt.setGravity(false);
            bolt.setCustomName(p.getDisplayName() + "'s blood bolt");
            ID.put(bolt, "BloodBolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                    bolt.getWorld().spawnParticle(Particle.REDSTONE, bolt.getLocation(), 3, .1, .1, .1, dust);
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    tick.get(bolt).cancel();
                    bolt.remove();
                }
            }, 100);
        }
    }

    public static void BloodSacrifice(Player p) {
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
        wizard.Mana = wizard.Mana + 3;
        p.damage(Utils.BLOOD_SACRIFICE_COST);
        if (wizard.Mana > 10) {
            wizard.Mana = 10;
        }
        p.setLevel(wizard.Mana);

    }

    public static void BloodTracker(Player p,Plugin plugin){
        if (Mana.spendMana(p, Utils.BLOOD_TRACKER_COST)) {
            String teamName = TeamsInit.getTeamName(p);

            Vex vex = (Vex) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.VEX);
            vex.setHealth(1);
            vex.setCharging(false);
            Vector direction = p.getEyeLocation().getDirection().multiply(.4);
            vex.setVelocity(direction);
            final Player[] locked = {null};
            BukkitTask tick = new BukkitRunnable() {
                public void run() {
                    if (locked[0]==null) {
                        //Code for when the vex is searching for a player
                        vex.setTarget(null);
                        vex.setCharging(false);
                        vex.setVelocity(direction);

                        Player tempLocked = null;
                        double tempDistance = 20;

                        for (Entity e : vex.getNearbyEntities(20, 20, 20)) {
                            if (e instanceof Player) {
                                if (e.getLocation().distance(vex.getLocation()) < tempDistance) {
                                    if (!teamName.equals(TeamsInit.getTeamName(e))) {
                                        if (Math.ceil(((Player) e).getMaxHealth()) > Math.ceil(((Player) e).getHealth())) {
                                            tempDistance = e.getLocation().distance(vex.getLocation());
                                            tempLocked = (Player) e;
                                        }
                                    }
                                }
                            }
                        }

                        if (tempLocked!=null) {
                            vex.setTarget(tempLocked);
                            vex.setCharging(true);
                            locked[0] = tempLocked;
                        }

                    } else{
                        //Code for when the vex is locked onto a player

                        Vector target = locked[0].getLocation().toVector().subtract(vex.getLocation().toVector()).normalize().multiply(.05);
                        vex.setVelocity(vex.getVelocity().add(target));

                        Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 4);
                        vex.getWorld().spawnParticle(Particle.REDSTONE,vex.getLocation(),1,0,0,0,0,dust);
                        if (vex.getLocation().distance(locked[0].getLocation())<=2){
                            BloodLink(p,locked[0]);


                            Particle.DustOptions dustCloud = new Particle.DustOptions(Color.RED, 3);
                            vex.getWorld().spawnParticle(Particle.REDSTONE,vex.getLocation(),10,.3,.3,.3,0,dustCloud);
                            vex.getWorld().playSound(vex.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP,1,1);
                            locked[0].addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20,2));

                            if (StatusEffects.BloodWeak.containsKey(locked[0])){
                                StatusEffects.BloodWeak.replace(locked[0],60);
                            } else{
                                StatusEffects.BloodWeak.put(locked[0],60);
                            }

                            vex.remove();
                        }

                        if (locked[0].isDead()){
                            this.cancel();
                            vex.remove();
                        }
                    }

                    if (vex.isDead()) this.cancel();
                }
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (locked[0]==null){
                        tick.cancel();
                        vex.remove();
                    }
                }
            },120);
        }
    }

    public static void BloodLink(Player p,Player v){
        Map<Player, HashMap<Player,Integer>> linkMap = StatusEffects.bloodLink;
        if (linkMap.containsKey(p)){
                if (linkMap.get(p).containsKey(v)) {
                    linkMap.get(p).remove(v, 300);
                } else {
                    linkMap.get(p).put(v, 300);
                }
            UpdateUlt();
        }
    }

    public static void UpdateUlt(){
        for (UUID uuid:WizardInit.playersWizards.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p!=null){
                if (WizardInit.playersWizards.get(uuid).kitID.equals(Kit.BLOOD_MAGE)) {
                    for (ItemStack item : p.getInventory()) {
                        if (item != null) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta.getDisplayName().contains("Blood Link Ritual")) {
                                meta.setDisplayName(ChatColor.RESET + ("Blood Link Ritual (" + StatusEffects.bloodLink.get(p).size() + ")"));
                                item.setItemMeta(meta);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void BloodUlt(Player p, EffectManager em) {
        if (StatusEffects.bloodLink.containsKey(p)) {
            if (!StatusEffects.bloodLink.get(p).isEmpty()) {
                if (Ultimate.spendUlt(p)) {
                    for (Player v : StatusEffects.bloodLink.get(p).keySet()) {
                        LineEffect lineEffect = new LineEffect(em);
                        lineEffect.setTargetPlayer(v);
                        lineEffect.setTargetLocation(v.getLocation().add(0, 1, 0));
                        lineEffect.setLocation(p.getLocation().add(0, 1, 0));
                        lineEffect.setEntity(p);
                        lineEffect.particle = Particle.REDSTONE;
                        lineEffect.color = Color.RED;
                        lineEffect.particleSize = 3;
                        lineEffect.iterations = 20;
                        lineEffect.particles = (int) v.getLocation().distance(p.getLocation());
                        em.start(lineEffect);
                    }
                    StatusEffects.activeBloodUlt.put(p, 100);

                }
            }
        }
    }
}

package me.mintnetwork.spells;

import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Map;

public class BloodMage {

    public static void BloodBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(1.3));
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.RED_DYE));
            bolt.setGravity(false);
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
        Wizard wizard = WizardInit.playersWizards.get(p);
        wizard.Mana = wizard.Mana + 3;
        p.damage(6);
        if (wizard.Mana > 10) {
            wizard.Mana = 10;
        }
        p.setLevel(wizard.Mana);

    }

    public static void BloodTracker(Player p,Plugin plugin){
        if (Mana.spendMana(p, 3)) {
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

                        for (Entity e : vex.getNearbyEntities(20, 20, 20)) {
                            if (e instanceof Player) {
                                if (e.getLocation().distance(vex.getLocation()) <= 20) {
                                    if (!teamName.equals(TeamsInit.getTeamName(e))) {
                                        if (Math.ceil(((Player) e).getMaxHealth()) > Math.ceil(((Player) e).getHealth())) {
                                            vex.setTarget((LivingEntity) e);
                                            vex.setCharging(true);
                                            locked[0] = (Player) e;
                                        }
                                    }
                                }
                            }
                        }
                    } else{
                        //Code for when the vex is locked onto a player

                        Vector target = vex.getLocation().toVector().subtract(locked[0].getLocation().toVector()).normalize().multiply(-.05);
                        vex.setVelocity(vex.getVelocity().add(target));
                        Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 4);
                        vex.getWorld().spawnParticle(Particle.REDSTONE,vex.getLocation(),1,0,0,0,0,dust);
                        if (vex.getLocation().distance(locked[0].getLocation())<=2){
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

    public static void BloodUlt(Player p, Plugin plugin) {
        if (Ultimate.spendUlt(p)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(1.5));
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.RED_DYE));
            bolt.setGravity(false);
            ID.put(bolt, "BloodUlt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
                Map<Entity, Vector> velocity1 = ProjectileInfo.getLockedVelocity();
                bolt.setVelocity(velocity1.get(bolt));
                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 4);
                bolt.getWorld().spawnParticle(Particle.REDSTONE, bolt.getLocation(), 3, .1, .1, .1, dust);
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                tick.get(bolt).cancel();
                bolt.remove();
            }, 160);
        }
    }
}

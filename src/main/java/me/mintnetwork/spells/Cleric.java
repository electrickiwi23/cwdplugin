package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Cake;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;

public class Cleric extends KitItems {


    public Cleric(){
        ultTime = Utils.CLERIC_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();


        lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
        lore.add("TEXT");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Cloud Bust"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
        lore.add("TEXT");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Air Dash"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
        lore.add("TEXT");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Air Needles"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add("TEXT");
        meta.setDisplayName(ChatColor.RESET+("Tornado Blast"));
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Press shift to float on the air ");
        lore.add(ChatColor.GRAY + "and take no fall damage.");
        meta.setDisplayName(ChatColor.WHITE + "Wind Cushion");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "CLASS DESC");
        lore.add(ChatColor.GRAY + "CLASS DESC");
        lore.add(ChatColor.GRAY + "CLASS DESC");

        menuItem.setType(Material.FEATHER);
        meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN+"CLASS");
        meta.setLore(lore);

        //create itemstacks for each wand of the class
    }

    public static void HealBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, Utils.HEAL_BOLT_COST)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.PINK_DYE));
            bolt.setGravity(false);

            String TeamName = TeamsInit.getTeamName(p);

            ID.put(bolt, "HealBolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    bolt.getWorld().spawnParticle(Particle.HEART, bolt.getLocation(), 1, .1, .1, .1);
                    Entity closest = null;
                    double closestAngle = 30;
                    for (Entity e : bolt.getWorld().getNearbyEntities(bolt.getLocation(), 30, 30, 30)) {
                        if (!e.equals(p)) {
                            if (e instanceof LivingEntity) {
                                String VictimTeam = TeamsInit.getTeamName(e);
                                if (TeamName.equals(VictimTeam)) {
                                    if (((LivingEntity) e).hasLineOfSight(bolt)) {
                                        Vector n = e.getLocation().toVector().subtract(bolt.getLocation().toVector()).normalize();
                                        double angle = velocity.get(bolt).angle(n);
                                        if (angle <= .4) {
                                            if (closestAngle > angle) {
                                                closest = e;
                                                closestAngle = angle;
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (closest != null) {
                        Vector n = closest.getLocation().toVector().subtract(bolt.getLocation().toVector()).normalize();
                        velocity.put(bolt, velocity.get(bolt).add(n).normalize());


                    }
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    tick.get(bolt).cancel();
                    bolt.remove();
                }
            }, 60);
        }
    }

    public static void HealPillar(Player p, Plugin plugin, BlockFace face, Block block) {
        if (face.getDirection().equals(new Vector(0, 1, 0))) {
            if (Mana.spendMana(p, Utils.HEAL_PILLAR_COST)) {
                Block pillarLocation = block.getLocation().add(face.getDirection()).getBlock();
                Block cakeLocation = pillarLocation.getLocation().add(0, 1, 0).getBlock();
                if (cakeLocation.isPassable()) {
                    cakeLocation.setType(Material.CAKE);
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            Block alter = pillarLocation.getLocation().add(i - 1, 0, j - 1).getBlock();
                            if (alter.isPassable()) {
                                alter.setType(Material.PINK_WOOL);
                            }
                        }
                    }
                    BukkitTask healing = new BukkitRunnable() {
                        public void run() {
                            pillarLocation.getWorld().spawnParticle(Particle.HEART, pillarLocation.getLocation().add(.5, 1, .5), 1, 1.5, 1.5, 1.5, .2);
                            for (Entity e : pillarLocation.getWorld().getNearbyEntities(pillarLocation.getLocation().add(.5,.5,.5), 6, 6, 6)) {
                                if (e instanceof LivingEntity) {
                                    LivingEntity live = (LivingEntity) e;
                                    if (!live.isDead()) {
                                        if (live.getMaxHealth() - .033 >= Math.ceil(live.getHealth())) {
                                            live.setHealth(live.getHealth() + .033);
                                        }
                                    }
                                }
                            }
                            if (!cakeLocation.getType().equals(Material.CAKE)) {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 1, 1);
                    BukkitTask cake = new BukkitRunnable() {
                        public void run() {
                            if (cakeLocation.getType().equals(Material.CAKE)) {
                                Cake data = (Cake) cakeLocation.getBlockData();
                                if (data.getBites() < 6) {
                                    data.setBites(data.getBites() + 1);
                                    cakeLocation.setBlockData(data);
                                } else {
                                    cakeLocation.setType(Material.AIR);
                                    this.cancel();
                                }
                            }
                        }
                    }.runTaskTimer(plugin, 120, 120);
                }
            }
        }
    }

    public static void PurificationWave(Player p, Plugin plugin, EffectManager em) {
        if (Mana.spendMana(p, Utils.PURIFICATION_WAVE_COST)) {
            String teamName = TeamsInit.getTeamName(p);
            ArrayList<Entity> nearby = new ArrayList<>(p.getNearbyEntities(5, 5, 5));
            nearby.add(p);
            for (Entity e : nearby) {
                if (e instanceof Player) {
                    Player victim = (Player) e;
                    String victimTeam = TeamsInit.getTeamName(e);
                    if (teamName.matches(victimTeam)) {
                        if (e.getLocation().distance(p.getLocation()) <= 4) {
                            victim.removePotionEffect(PotionEffectType.SLOW);
                            victim.removePotionEffect(PotionEffectType.WITHER);
                            victim.removePotionEffect(PotionEffectType.POISON);
                            victim.removePotionEffect(PotionEffectType.GLOWING);

                            if (!StatusEffects.ShadowConsumed.containsKey(victim)) {
                                victim.removePotionEffect(PotionEffectType.WEAKNESS);
                                victim.removePotionEffect(PotionEffectType.BLINDNESS);
                            }


                            StatusEffects.BloodWeak.remove(victim);
                            StatusEffects.stunSong.remove(victim);


                        }
                    }
                }
            }
            SphereEffect circle = new SphereEffect(em);
            circle.particle = Particle.END_ROD;
            circle.radius = 1;
            circle.radiusIncrease = .5;
            circle.setLocation(p.getLocation());
            circle.particleOffsetY = (float) .1;
            circle.particleOffsetX = (float) .1;
            circle.particleOffsetZ = (float) .1;
            circle.start();
            final int[] count = {1};
            new BukkitRunnable() {
                public void run() {
                    circle.cancel();
                }
            }.runTaskLater(plugin,  8);
        }
    }

    public static void TeamHeal(Player p){
        if (Ultimate.spendUlt(p)) {
            String teamName = TeamsInit.getTeamName(p);


            for (Player e : Bukkit.getOnlinePlayers()) {
                if (TeamsInit.getTeamName(e).equals(teamName)) {
                    if (e.getLocation().distance(p.getLocation()) <= 40) {
                        StatusEffects.healTeam.put(e, 0);
                    }
                }
            }
        }
    }
}

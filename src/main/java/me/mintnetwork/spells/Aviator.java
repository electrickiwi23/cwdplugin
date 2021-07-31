package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
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
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;

public class Aviator extends KitItems {


    public Aviator(){
        ultTime = Utils.AVIATOR_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();


        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.CLOUD_BURST_COST);
        lore.add(ChatColor.GRAY + "Propel yourself high into the air ");
        lore.add(ChatColor.GRAY +"using a burst of smoke.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Cloud Burst"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.AIR_DASH_COST);
        lore.add(ChatColor.GRAY + "Hurl yourself quickly in the direction ");
        lore.add(ChatColor.GRAY + "you are facing using the air.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Air Dash"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.AIR_NEEDLES_COST);
        lore.add(ChatColor.GRAY + "Shoot out a trio of needles that deal ");
        lore.add(ChatColor.GRAY + "a moderate amount of damage.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Air Needles"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY + "Charges you with energy that makes");
        lore.add(ChatColor.GRAY + "movement spells cost much less");
        lore.add(ChatColor.GRAY + "and gives them an electric kick.");
        meta.setDisplayName(ChatColor.GOLD+"Thunderstorm Overdrive");
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Press shift to float on the air ");
        lore.add(ChatColor.GRAY + "and take no fall damage.");
        meta.setDisplayName(ChatColor.WHITE + "Wind Cushion");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Dominate the skies with enhanced");
        lore.add(ChatColor.GRAY + "movement and the power of flight.");

        menuItem.setType(Material.FEATHER);
        meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA+"Aviator");
        meta.setLore(lore);

        menuItem.setItemMeta(meta);

    }
    public static void AirNeedles(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, Utils.AIR_NEEDLES_COST)) {
            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ARROW_SHOOT,.3F,1);
            Arrow arrow = p.launchProjectile(Arrow.class);
            arrow.setVelocity(arrow.getVelocity().multiply(1.5));
            arrow.setGravity(false);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(arrow, "Wind Arrow");
            arrow.setDamage(.3);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        arrow.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, arrow.getLocation(), 2, .1, .1, .1, 0, null, true);
                        arrow.remove();
                        tick.remove(arrow);
                    }
                }
            }, 10);

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ARROW_SHOOT,.3F,1);

                    Arrow arrow = p.launchProjectile(Arrow.class);
                    arrow.setVelocity(arrow.getVelocity().multiply(1.5));
                    arrow.setGravity(false);
                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                    ID.put(arrow, "Wind Arrow");
                    arrow.setDamage(.3);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

                    Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if (!arrow.isDead()) {
                                arrow.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, arrow.getLocation(), 2, .1, .1, .1, 0, null, true);
                                arrow.remove();
                                tick.remove(arrow);
                            }
                        }
                    }, 10);

                }
            }, 3);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {

                    p.getWorld().playSound(p.getLocation(),Sound.ENTITY_ARROW_SHOOT,.3F,1);
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    arrow.setVelocity(arrow.getVelocity().multiply(1.5));
                    arrow.setGravity(false);
                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                    ID.put(arrow, "Wind Arrow");
                    arrow.setDamage(.3);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if (!arrow.isDead()) {
                                arrow.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, arrow.getLocation(), 2, .1, .1, .1, 0, null, true);
                                arrow.remove();
                                tick.remove(arrow);
                            }
                        }
                    }, 10);
                }
            }, 6);
        }
    }

    public static void CloudBurst(Player p, Plugin plugin) {
        final boolean storm = StatusEffects.stormUlt.contains(p);
        if ((storm&&Mana.spendMana(p,1))||Mana.spendMana(p, Utils.CLOUD_BURST_COST)) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP,.7F,1);
            if (storm) {
                p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 10, .6, .6, .6, 0,new Particle.DustOptions(Color.fromBGR(80,80,80),3));
            } else   p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 10, .6, .6, .6, 0);
            final int[] count = {0};
            p.setVelocity(new Vector(0, 1.4, 0));
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    count[0]++;
                    if (storm){
                        p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 2, .2, .2, .2, 0,new Particle.DustOptions(Color.fromBGR(80,80,80),3));
                    }else  p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, .2, .2, .2, 0);
                    if (count[0] >= 8) {
                        if (storm) {
                            RayTraceResult ray = p.getWorld().rayTraceBlocks(p.getLocation(), new Vector(0, -1, 0), 20, FluidCollisionMode.NEVER);
                            Location hitLocation = p.getLocation().add(0,-20,0);
                            hitLocation.getWorld().playSound(p.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,1);
                            if (ray != null) {
                                hitLocation = ray.getHitPosition().toLocation(p.getWorld());
                                hitLocation.getWorld().playSound(hitLocation,Sound.ENTITY_LIGHTNING_BOLT_IMPACT,1,1);
                                for (Entity e:hitLocation.getWorld().getNearbyEntities(hitLocation,3,3,3)) {
                                    if (e instanceof LivingEntity&&e.getLocation().distance(hitLocation)<3){
                                        if (!TeamsInit.getTeamName(e).equals("")&&!TeamsInit.getTeamName(e).equals(TeamsInit.getTeamName(p))) {
                                            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                                            stand.setInvisible(true);
                                            stand.setMarker(true);
                                            stand.setCustomNameVisible(false);
                                            stand.setCustomName(p.getDisplayName() + "'s Lightning");
                                            TeamsInit.addToTeam(stand, TeamsInit.getTeamName(p));

                                            e.setVelocity(new Vector(0, 0, 0));
                                            ((LivingEntity) e).damage(3,stand);

                                            stand.remove();
                                            final int[] count = {0};
                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    count[0]++;
                                                    e.setVelocity(new Vector(0, 0, 0));
                                                    if (count[0] ==4) this.cancel();
                                                }
                                            }.runTaskTimer(plugin,0,1);
                                        }
                                    }
                                }
                            }
                            Location current = p.getLocation();
                            for (int i = 0; i < p.getLocation().distance(hitLocation); i++) {
                                current.add(0,-1,0);
                                current.getWorld().spawnParticle(Particle.REDSTONE, current, 1, 0, 0, 0, 0,new Particle.DustOptions(Color.YELLOW,3));
                            }
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }

    public static void AirDash(Player p, Plugin plugin) {
        final boolean storm = StatusEffects.stormUlt.contains(p);
        if ((storm&&Mana.spendMana(p,1))||Mana.spendMana(p, Utils.AIR_DASH_COST)) {
            p.setGravity(false);
            double tempY = p.getEyeLocation().getDirection().getY();
            p.setVelocity(p.getEyeLocation().getDirection().setY(0).normalize().setY(Math.max(-.3,Math.min(.3,tempY))).normalize().multiply(1.5));
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    double tempY = p.getEyeLocation().getDirection().getY();
                    p.setVelocity(p.getEyeLocation().getDirection().setY(0).normalize().setY(Math.max(-.3,Math.min(.3,tempY))).normalize().multiply(1.5));
                    if (storm) {
                        for (Entity e:p.getNearbyEntities(3,3,3)) {
                            if (e instanceof LivingEntity&&e.getLocation().distance(p.getLocation())<3&&((LivingEntity) e).getNoDamageTicks()==0){
                                if (!TeamsInit.getTeamName(e).equals("")&&!TeamsInit.getTeamName(e).equals(TeamsInit.getTeamName(p))) {
                                    Location current = p.getLocation();
                                    Vector direction = e.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(.25);
                                    for (int i = 0; i < 8; i++) {
                                        current.add(direction);
                                        current.getWorld().spawnParticle(Particle.REDSTONE, current, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 1));
                                        if (e.getBoundingBox().contains(current.toVector())) break;
                                    }
                                    ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                                    stand.setInvisible(true);
                                    stand.setMarker(true);
                                    stand.setCustomNameVisible(false);
                                    stand.setCustomName(p.getDisplayName() + "'s Static Shock");
                                    TeamsInit.addToTeam(stand, TeamsInit.getTeamName(p));

                                    ((LivingEntity) e).damage(2,stand);
                                    e.setVelocity(new Vector(0, 0, 0));

                                    stand.remove();
                                    final int[] count = {0};
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            count[0]++;
                                            e.setVelocity(new Vector(0, 0, 0));
                                            if (count[0] ==4) this.cancel();
                                        }
                                    }.runTaskTimer(plugin,0,1);
                                }
                            }
                        }
                        p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 2, .1, .1, .1, 0,new Particle.DustOptions(Color.fromBGR(80,80,80),3));
                    }else   p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, .1, .1, .1, 0);
                }
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    p.setVelocity(p.getVelocity().multiply(.5));
                    p.setGravity(true);
                    task.cancel();
                }
            }, 7);
        }
    }

    public static void StormUlt(Player p, Plugin plugin){
        if (Ultimate.spendUlt(p)) {

            p.getWorld().strikeLightningEffect(p.getLocation());

            p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 3, 1);
            StatusEffects.stormUlt.add(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Utils.STORM_ULT_DURATION,1,false,false));
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    StatusEffects.stormUlt.remove(p);
                }
            },Utils.STORM_ULT_DURATION);
        }
    }

}

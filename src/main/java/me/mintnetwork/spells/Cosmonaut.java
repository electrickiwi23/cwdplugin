package me.mintnetwork.spells;

import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.Objects.Shield;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.BlockDecay;
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
import java.util.Random;

public class Cosmonaut extends KitItems {
    public Cosmonaut(){
        ultTime = Utils.COSMONAUT_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();


        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.COSMIC_RAY_COST);
        lore.add(ChatColor.GRAY +"Shoots a laser which ");
        lore.add(ChatColor.GRAY +"damages enemies on hit.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Cosmic Ray"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SUN_BOMB_COST);
        lore.add(ChatColor.GRAY +"Toss the core of a dying sun ");
        lore.add(ChatColor.GRAY +"that explodes after a few seconds,");
        lore.add(ChatColor.GRAY +"dealing massive damage.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Unstable Nova"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.BLACK_HOLE_COST);
        lore.add(ChatColor.GRAY +"Create a black hole that sucks ");
        lore.add(ChatColor.GRAY +"in anyone around it dealing");
        lore.add(ChatColor.GRAY +"damage to those too close.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Black Hole"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY +"Rockets you into the air and ");
        lore.add(ChatColor.GRAY +"hurls a stream of explosive ");
        lore.add(ChatColor.GRAY +"stars into anyone below.");
        meta.setDisplayName(ChatColor.GOLD+("Starfield Barrage"));
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Double tap the space bar");
        lore.add(ChatColor.GRAY + "to activate or deactivate");
        lore.add(ChatColor.GRAY + "your powers of flight.");
        meta.setDisplayName(ChatColor.WHITE + " Anti-Gravity Boots");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Arrive from the stars and use");
        lore.add(ChatColor.GRAY + "your kit to deny enemies territory");

        menuItem.setType(Material.NETHER_STAR);
        meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+"Cosmonaut");
        meta.setLore(lore);
        menuItem.setItemMeta(meta);
    }

    public static void cosmicRay(Player p,Plugin plugin){
        if (Mana.spendMana(p,Utils.COSMIC_RAY_COST)) {
            final boolean[] hasHit = {false};
            final Location[] current = {p.getEyeLocation().add(p.getEyeLocation().getDirection())};
            final Vector[] direction = {p.getEyeLocation().getDirection()};
            final int[] range = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        if (!hasHit[0]) {
                            if (current[0].isWorldLoaded()) {
                                RayTraceResult ray = p.getWorld().rayTrace(current[0], direction[0], 1, FluidCollisionMode.NEVER, true, .1, null);
                                Location hitLocation = null;
                                LivingEntity hitEntity = null;
                                if (ray != null) {
                                    try {
                                        hitEntity = (LivingEntity) ray.getHitEntity();
                                    } catch (Exception ignore) {
                                    }
                                    try {
                                        hitLocation = ray.getHitPosition().toLocation(p.getWorld());
                                    } catch (Exception ignore) {
                                    }
                                    if (hitLocation != null && hitEntity == null) {
                                        hasHit[0] = true;
                                        if (BlockDecay.decay.containsKey(ray.getHitBlock())) {
                                            DecayBlock block = BlockDecay.decay.get(ray.getHitBlock());
                                            block.damage(120);
                                            block.setForceful(true);
                                        }
                                    }
                                    if (hitEntity != null && (!(hitEntity == p && range[0] <= 2))) {
                                        hasHit[0] = true;
                                        if (TeamsInit.getTeamName(hitEntity).equals("") || !TeamsInit.getTeamName(hitEntity).equals(TeamsInit.getTeamName(p))) {


                                            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                                            stand.setInvisible(true);
                                            stand.setMarker(true);
                                            stand.setCustomNameVisible(false);
                                            stand.setCustomName(p.getDisplayName() + "'s Cosmic Ray");
                                            TeamsInit.addToTeam(stand, TeamsInit.getTeamName(p));

                                            hitEntity.damage(3, stand);
                                            hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                                            p.playSound(p.getEyeLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);

                                            stand.remove();

                                        }
                                        this.cancel();
                                    }
                                }
                                if (!hasHit[0]) {
                                    current[0] = current[0].add(direction[0]);
                                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, current[0], 1, .1, .1, .1, new Particle.DustTransition(Color.FUCHSIA, Color.fromRGB(4, 217, 188), 3));
                                    range[0]++;
                                    if (range[0] >= 50) this.cancel();
                                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();

                                    //code for shield reflection--------------------------------------------------------
                                    for (Entity shield : Shield.shieldMap.keySet()) {
                                        if (shield.getLocation().distance(current[0]) < Shield.shieldMap.get(shield).getRadius() + .5) {
                                            direction[0] = Shield.shieldMap.get(shield).reflectVector(current[0], direction[0]);
                                        }
                                    }
                                    //----------------------------------------------------------------------------------
                                }
                            } else {
                                hasHit[0] = true;
                            }
                        } else {
                            this.cancel();
                        }
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }

    public static void castSunBomb(Player p,Plugin plugin){
        if (Mana.spendMana(p, Utils.SUN_BOMB_COST)) {
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(grenade, "SunBomb");

            grenade.setItem(new ItemStack(Material.ORANGE_DYE));
            ProjectileInfo.getTickCode().put(grenade, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    grenade.getWorld().spawnParticle(Particle.FLAME, grenade.getLocation(), 2, .1, .1, .1, .04);
                }
            }, 1, 1));
        }

    }

    public static void summonSunBomb(Location location,Plugin plugin){
//        location.getWorld().spawnParticle(Particle.LIGHT,location,1);
            Snowball snowball = (Snowball) location.getWorld().spawnEntity(location.clone().subtract(0, .125, 0), EntityType.SNOWBALL);
            snowball.setGravity(false);
            snowball.setItem(new ItemStack(Material.NETHER_STAR));
            snowball.setVelocity(new Vector(0, 0, 0));


            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    location.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, location, 1, 1, 1, 1, 0, new Particle.DustTransition(Color.ORANGE,Color.fromRGB(4, 217, 188), 2));
                    location.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, location, 1, 1, 1, 1, 0, new Particle.DustTransition(Color.fromRGB(4, 217, 188),Color.ORANGE, 2));

                    location.getWorld().spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, .03);
                    location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, location, 1, 0, 0, 0, .03);
//
                }
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    location.getWorld().createExplosion(location, 5);
                    task.cancel();
                    snowball.remove();

                }
            }, 80);
    }

    public static void starShower(Player p, Plugin plugin) {
        if (Ultimate.spendUlt(p)) {
            p.setAllowFlight(false);
            StatusEffects.UsingMove.add(p);

            Vector horizontal = p.getEyeLocation().getDirection().rotateAroundY(Math.toRadians(90)).setY(0);
            p.setVelocity(new Vector(0, 1, 0));
            p.setGravity(false);
            Random random = new Random();
            final boolean[] shooting = {false};

            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    p.getWorld().spawnParticle(Particle.WAX_OFF,p.getLocation().add(0,1,0),2,.2,.4,.2,0);
                    if (shooting[0]) {
                        Vector spread = p.getEyeLocation().getDirection().rotateAroundAxis(horizontal, Math.random() * 1.7 - .85)
                                .rotateAroundAxis(p.getEyeLocation().getDirection().rotateAroundAxis(horizontal, Math.PI / 4), Math.random() * 1.7 - .85);

                        Snowball bolt = p.launchProjectile(Snowball.class, spread.clone());
                        Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                        bolt.teleport(p.getEyeLocation().add(p.getEyeLocation().getDirection().rotateAroundAxis(horizontal, Math.PI / 4.0).rotateAroundAxis(p.getEyeLocation().getDirection(), Math.random() * Math.PI).multiply(Math.random() * 2)));

                        spread = p.getEyeLocation().getDirection().rotateAroundAxis(horizontal, Math.random() * .35 - .125)
                                .rotateAroundAxis(p.getEyeLocation().getDirection().rotateAroundAxis(horizontal, 90), Math.random() * .35 - .125);

                        velocity.put(bolt, spread);
                        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                        bolt.setItem(new ItemStack(Material.NETHER_STAR));
                        bolt.setGravity(false);
                        bolt.setCustomName(p.getDisplayName() + "'s star");
                        ID.put(bolt, "StarBolt");
                        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                        tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                                bolt.setVelocity(bolt.getVelocity().add(velocity.get(bolt).clone().multiply(.1)));
                                bolt.getWorld().spawnParticle(Particle.WAX_OFF, bolt.getLocation().subtract(bolt.getVelocity().multiply(.5)), 2, .1, .1, .1);
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
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    shooting[0] =true;
                    p.setVelocity(new Vector(0, 0, 0));
                }
            }, 20);
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    StatusEffects.UsingMove.remove(p);
                    p.setGravity(true);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 1));
                    task.cancel();

                }
            }, 100);
        }
    }
}

package me.mintnetwork.spells;

import me.mintnetwork.Objects.Shield;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.Map;

public class Painter {
    public static void SprayPaint(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 2)) {
            for (int i = 0; i < 5; i++) {
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Location spread = p.getEyeLocation();
                        float pitch = spread.getPitch() + (float) (Math.random() * 14 - 7);
                        float yaw = spread.getYaw() + (float) (Math.random() * 14 - 7);
                        spread.setPitch(pitch);
                        spread.setYaw(yaw);
                        Vector direction = spread.getDirection();
                        Location current = p.getEyeLocation().add(direction);
                        boolean hasHit = false;
                        int range = 0;
                        while (!hasHit) {
                            if (current.isWorldLoaded()) {
                                RayTraceResult ray = p.getWorld().rayTrace(current, direction, 1, FluidCollisionMode.NEVER, true, .1, null);
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
                                        hasHit = true;
                                    }
                                    if (hitEntity != null && (!(hitEntity == p && range <= 2))) {
                                        if (!(hitEntity instanceof ArmorStand)) {
                                            if (Math.abs(hitLocation.getY() - hitEntity.getEyeLocation().getY()) <= .4) {
                                                hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 2));
                                            }
                                            Map<LivingEntity, Integer> painted = StatusEffects.paintTimer;
                                            if (painted.containsKey(hitEntity)) {
                                                painted.replace(hitEntity, painted.get(hitEntity) + 60);
                                            } else {
                                                painted.put(hitEntity, 60);
                                            }
                                            hasHit = true;
                                            hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 1));
                                        }
                                    }
                                }

                            }
                            if (!hasHit) {
                                p.getWorld().playSound(current, Sound.ENTITY_CAT_HISS, (float) .5, 1);

                                current = current.add(direction);
                                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);

                                p.getWorld().spawnParticle(Particle.REDSTONE, current, 1, .1, .1, .1, 0, dust);

                                dust = new Particle.DustOptions(Color.ORANGE, 2);
                                p.getWorld().spawnParticle(Particle.REDSTONE, current, 1, .1, .1, .1, 0, dust);

                                dust = new Particle.DustOptions(Color.YELLOW, 2);
                                p.getWorld().spawnParticle(Particle.REDSTONE, current, 1, .1, .1, .1, 0, dust);

                                dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 2);
                                p.getWorld().spawnParticle(Particle.REDSTONE, current, 1, .1, .1, .1, 0, dust);

                                dust = new Particle.DustOptions(Color.BLUE, 2);
                                p.getWorld().spawnParticle(Particle.REDSTONE, current, 1, .1, .1, .1, 0, dust);

                                dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 2);
                                p.getWorld().spawnParticle(Particle.REDSTONE, current, 1, .1, .1, .1, 0, dust);
                                range++;
                                if (range >= 5) hasHit = true;

                                Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                                for (Entity shield : Shield.shieldMap.keySet()) {
                                    if (shield.getLocation().distance(current)<Shield.shieldMap.get(shield).getRadius()+.5){
                                        direction = Shield.shieldMap.get(shield).reflectVector(current,direction);
                                    }
                                }
                            }
                        }
                    }
                }, i);
            }
        }
    }

    public static void PaintBomb(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {

                ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
                stand.setMarker(true);
                stand.setInvisible(true);
                Snowball grenade = p.launchProjectile(Snowball.class);
                Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                velocity.put(grenade, p.getEyeLocation().getDirection());
                Map<Entity, String> ID = ProjectileInfo.getProjectileID();

                grenade.setItem(new ItemStack(Material.FIREWORK_STAR));
                grenade.setBounce(true);

                Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                linked.put(stand, grenade);
                linked.put(grenade, stand);
                ID.put(stand, "PaintGrenade");
                Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                Map<Entity, BukkitTask> activate = ProjectileInfo.getActivateCode();
                tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                        stand.teleport(linked.get(stand));

                        Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 1);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 1, .1, .1, .1, 0, dust);

                        dust = new Particle.DustOptions(Color.ORANGE, 1);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 1, .1, .1, .1, 0, dust);

                        dust = new Particle.DustOptions(Color.YELLOW, 1);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 1, .1, .1, .1, 0, dust);

                        dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 1);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 1, .1, .1, .1, 0, dust);

                        dust = new Particle.DustOptions(Color.BLUE, 1);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 1, .1, .1, .1, 0, dust);

                        dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 1);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 1, .1, .1, .1, 0, dust);
                    }
                }, 1, 1));
                activate.put(stand, Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 3);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.ORANGE, 3);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.YELLOW, 3);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 3);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.BLUE, 3);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 3);
                        stand.getWorld().spawnParticle(Particle.REDSTONE, stand.getLocation(), 20, 2, 2, 2, 0, dust);
                        for (Entity entity : stand.getWorld().getNearbyEntities(stand.getLocation(), 6, 6, 6)) {
                            if (entity instanceof LivingEntity) {
                                if (!(entity instanceof ArmorStand)) {
                                    LivingEntity live = (LivingEntity) entity;
                                    if (entity.getLocation().distance(stand.getLocation()) <= 5) {
                                        live.damage(3, stand);
                                        live.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 1));
                                        live.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 1));
                                        Map<LivingEntity, Integer> painted = StatusEffects.paintTimer;
                                        if (painted.containsKey(live)) {
                                            painted.replace(live, painted.get(live) + 300);
                                        } else {
                                            painted.put(live, 300);
                                        }
                                    }
                                }
                            }
                        }
                        stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_CAT_HISS, (float) .8, 1);
                        stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_CAT_HISS, (float) .8, 1);

                        Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                        linked.get(stand).remove();
                        tick.get(stand).cancel();
                        stand.remove();

                    }
                }, 60));

//            Snowball grenade = p.launchProjectile(Snowball.class);
//            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
//            velocity.put(grenade, p.getEyeLocation().getDirection());
//            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
//            grenade.setItem(new ItemStack(Material.FIREWORK_STAR));
//            grenade.setCustomName("A Paint Canister");
//            ID.put(grenade, "PaintBomb");
//            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
//            tick.put(grenade, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            },1,1));
        }
    }

    public static void PaintReveal(Player p){
        Map<LivingEntity, Integer> painted = StatusEffects.paintTimer;
        if (painted.keySet().size()>0){
            if (Mana.spendMana(p,2)){
                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust);
                dust = new Particle.DustOptions(Color.ORANGE, 2);
                p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust);
                dust = new Particle.DustOptions(Color.YELLOW, 2);
                p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust);
                dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 2);
                p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0),20, .7, 4, .7, 0, dust);
                dust = new Particle.DustOptions(Color.BLUE, 2);
                p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust);
                dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 2);
                p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust);
                for (LivingEntity e: painted.keySet()) {
                    e.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,60,1));
                    dust = new Particle.DustOptions(Color.RED, 2);
                    e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust,true);
                    dust = new Particle.DustOptions(Color.ORANGE, 2);
                    e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust,true);
                    dust = new Particle.DustOptions(Color.YELLOW, 2);
                    e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust,true);
                    dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 2);
                    e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0,1,0),20, .7, 4, .7, 0, dust,true);
                    dust = new Particle.DustOptions(Color.BLUE, 2);
                    e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust,true);
                    dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 2);
                    e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0,1,0), 20, .7, 4, .7, 0, dust,true);
                }
            }
        }
    }

    public static void PaintActivateUlt(Player p, Plugin plugin) {
        Map<LivingEntity, Integer> painted = StatusEffects.paintTimer;
        if (painted.keySet().size() > 0) {
            if (Ultimate.spendUlt(p)) {
                ArrayList<LivingEntity> list = new ArrayList<>(painted.keySet());
                for (LivingEntity e : list) {
                    e.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1));
                    e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 3));
                }
                final int[] paintActivateCount = {0};
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        paintActivateCount[0]++;
                        for (LivingEntity e : list) {
                            Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                            dust = new Particle.DustOptions(Color.ORANGE, 2);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                            dust = new Particle.DustOptions(Color.YELLOW, 2);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                            dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 2);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                            dust = new Particle.DustOptions(Color.BLUE, 2);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                            dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 2);
                            e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 1, 0), 10, .2, .4, .2, 0, dust, true);
                        }
                        if (paintActivateCount[0] >= 20) {
                            for (LivingEntity e : list) {
                                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 3);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                                dust = new Particle.DustOptions(Color.ORANGE, 3);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                                dust = new Particle.DustOptions(Color.YELLOW, 3);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                                dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 3);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                                dust = new Particle.DustOptions(Color.BLUE, 3);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                                dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 3);
                                e.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 20, 2, 2, 2, 0, dust);
                                for (Entity entity : e.getWorld().getNearbyEntities(e.getLocation(), 6, 6, 6)) {
                                    if (entity instanceof LivingEntity) {
                                        LivingEntity live = (LivingEntity) entity;
                                        if (entity.getLocation().distance(e.getLocation()) <= 5) {
                                            live.setNoDamageTicks(0);
                                            live.damage(5, p);
                                            live.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 2));
                                            live.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 1));
                                            painted.remove(live);
                                        }
                                    }
                                }
                            }
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 1, 1);
            }
        }
    }
}

package me.mintnetwork.spells;

import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Random;

public class Tactician {
    public static void SniperBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)){
            Location spread = p.getEyeLocation();
            if (!p.isSneaking()) {
                float pitch = spread.getPitch() + (float) (Math.random() * 60 - 30);
                float yaw = spread.getYaw() + (float) (Math.random() * 60 - 30);
                spread.setPitch(pitch);
                spread.setYaw(yaw);
            }
            Vector direction = spread.getDirection();
            Location current = p.getEyeLocation().add(direction);
            p.getWorld().spawnParticle(Particle.FLASH, p.getEyeLocation().add(p.getEyeLocation().getDirection()), 1, 0, 0, 0, 0);
            p.getWorld().playSound(current, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
            int range = 0;
            boolean hasHit = false;
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
                            hasHit = true;
                            if (Math.abs(hitLocation.getY() - hitEntity.getEyeLocation().getY()) <= .25) {
                                hitEntity.damage(10, p);
                                p.playSound(p.getEyeLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                            } else {
                                hitEntity.damage(5, p);
                            }
                        }

                    }
                    if (!hasHit) {
                        current = current.add(direction);
                        p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, current, 1, 0, 0, 0, 0);
                        range++;
                        if (range >= 150) hasHit = true;
                        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                        for (Entity stand : current.getWorld().getNearbyEntities(current, 5, 5, 5)) {
                            if (ID.containsKey(stand)) {
                                if (ID.get(stand).equals("ShieldDome")) {
                                    if (current.distance(stand.getLocation()) <= 4.5) {
                                        Vector d = direction;
                                        Vector n = stand.getLocation().toVector().subtract(current.toVector()).normalize().multiply(-1);
                                        if (d.dot(n) <= 0 && current.distance(stand.getLocation()) >= 3)
                                            direction = (d.subtract(n.multiply(d.dot(n) * 2)));
                                    }

                                }
                            }
                        }
                    }
                } else {
                    hasHit = true;
                }
            }
        }
    }

    public static void Molotov(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setInvisible(true);
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            ItemStack item = new ItemStack(Material.POTION);
            PotionMeta potion = (PotionMeta) item.getItemMeta();
            potion.setColor(Color.fromRGB(217, 134, 61));
            item.setItemMeta(potion);
            grenade.setItem(item);

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(stand, grenade);
            linked.put(grenade, stand);
            ID.put(stand, "Molotov");
            ID.put(grenade, "Molotov");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    stand.teleport(linked.get(stand));
                    stand.getWorld().spawnParticle(Particle.FLAME, stand.getLocation(), 1, .1, .1, .1, .02);
                }
//                linked.get(stand).remove();
//                    tick.get(stand).cancel();
//                    stand.remove();
            }, 1, 1));
        }
    }

    public static void GrappleHook(Player p,Plugin plugin){
        if (Mana.spendMana(p, 3)) {
            Slime slime = (Slime) p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.SLIME);
            slime.setLeashHolder(p);
            slime.setSize(1);
            slime.setAI(false);
            slime.setInvulnerable(true);
            slime.setInvisible(true);
            slime.setMaxHealth(100);
            slime.setHealth(100);
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(1.6));
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.SLIME_BALL));
            bolt.setGravity(false);
            ID.put(bolt, "Grapple Bolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(slime, bolt);
            linked.put(bolt, slime);

            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    slime.teleport(bolt);
                    if (p.isDead()){
                        bolt.remove();
                        slime.remove();
                        tick.get(bolt).cancel();
                    }
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!bolt.isDead()) {
                        bolt.remove();
                        slime.remove();
                    }
                    if (!tick.get(bolt).isCancelled()) {
                        tick.get(bolt).cancel();
                    }

                }
            }, 12);
        }
    }

    public static void AirStrike(Player p, Plugin plugin) {
        //Spend Ult
        Map<Entity, BukkitTask> tick = ProjectileInfo.tickCode;
        Map<Entity, String> ID = ProjectileInfo.projectileID;
        Map<Player, Entity> tracked = ProjectileInfo.StrikeTrackedEntity;
        if (tracked.containsKey(p)) {
            AirStrikeRelease(p, plugin);
        } else {
            Arrow arrow = p.launchProjectile(Arrow.class);
            tracked.put(p, arrow);
            ID.put(arrow, "Tracker Arrow");
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            arrow.setDamage(.35);
            tick.put(arrow, Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
                Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                arrow.getWorld().spawnParticle(Particle.REDSTONE, tracked.get(p).getLocation(), 1, .1, .1, .1, 0, dust);
            }, 20, 20));
        }
    }

    public static void AirStrikeRelease(Player p, Plugin plugin) {
        Map<Player, Entity> tracked = ProjectileInfo.getStrikeTrackedEntity();
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        Firework firework = (Firework) p.getWorld().spawnEntity(tracked.get(p).getLocation().add(0, 1, 0), EntityType.FIREWORK);
        FireworkEffect.Builder effect = FireworkEffect.builder();
        Random random = new Random();
        FireworkMeta meta = firework.getFireworkMeta();
        effect.with(FireworkEffect.Type.BALL);
        effect.withColor(Color.RED);
        meta.addEffect(effect.build());
        meta.setPower(1);
        firework.setShooter(p);
        firework.setShotAtAngle(true);
        firework.setVelocity(new Vector(0, 1, 0));
        firework.setFireworkMeta(meta);
        final Location origin = tracked.get(p).getLocation();

        BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            Location l = origin.toVector().toLocation(origin.getWorld());
            Location strike = l.add((random.nextGaussian() * 15), 80, (random.nextGaussian() * 15));
            Fireball fireball = (Fireball) strike.getWorld().spawnEntity(strike, EntityType.FIREBALL);
            fireball.setVelocity(new Vector(0, -2.5, 0));
            fireball.setDirection(new Vector(0, -2.5, 0));
            fireball.setYield(3);
        }, 40, 2);
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            tick.get(tracked.get(p)).cancel();
            tracked.get(p).remove();
            tracked.remove(p);
            task.cancel();
        }, 220);
    }
}
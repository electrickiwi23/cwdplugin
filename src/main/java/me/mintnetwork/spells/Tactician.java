package me.mintnetwork.spells;

import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.Objects.Shield;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.repeaters.BlockDecay;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
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
                    RayTraceResult ray = p.getWorld().rayTrace(current, direction, 2, FluidCollisionMode.NEVER, true, .2, null);
                    if (ray != null) {
                        Location hitLocation = null;
                        LivingEntity hitEntity = null;
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
                            if (BlockDecay.decay.containsKey(ray.getHitBlock())){
                                DecayBlock block = BlockDecay.decay.get(ray.getHitBlock());
                                block.damage(120);
                                block.setForceful(true);
                                if (block.health<=0){
                                    block.remove();
                                    hasHit = false;
                                }
                            }
                        }
                        if (hitEntity != null && (!(hitEntity == p && range <= 2))) {
                            hasHit = true;

                            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(),EntityType.ARMOR_STAND);
                            stand.setInvisible(true);
                            stand.setMarker(true);
                            stand.setCustomNameVisible(false);
                            stand.setCustomName(p.getDisplayName() + "'s Sniper Bolt");
                            TeamsInit.addToTeam(stand,TeamsInit.getTeamName(p));

                            Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
                            if (hitEntity instanceof Player) {
                                if (wizard.PassiveTick >= 10) {
                                    wizard.PassiveTick = 0;
                                    Player finalHitEntity = (Player) hitEntity;
                                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            Wizard victimWiz = WizardInit.playersWizards.get(finalHitEntity.getUniqueId());
                                            p.sendMessage(ChatColor.UNDERLINE + "" + ChatColor.BOLD + (finalHitEntity.getName() + ":"));
                                            p.sendMessage(ChatColor.RED + ("Health: " + Math.ceil((finalHitEntity).getHealth())));
                                            p.sendMessage(ChatColor.GREEN + ("Mana: " + victimWiz.Mana));
                                            p.sendMessage(ChatColor.GOLD + ("Ultimate: " + (int) (Ultimate.getUltPercentage(finalHitEntity) * 100) + "%"));
                                        }
                                    });
                                }
                            }

                            if (Math.abs(hitLocation.getY() - hitEntity.getEyeLocation().getY()) <= .25) {
                                hitEntity.damage(10, stand);
                                p.playSound(p.getEyeLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                            } else {
                                hitEntity.damage(5, stand);
                            }
                            stand.remove();
                        }

                    }
                    if (!hasHit) {
                        current = current.add(direction.multiply(2));
                        p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, current, 1, 0, 0, 0, 0);
                        p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, current.add(direction), 1, 0, 0, 0, 0);
                        range++;
                        if (range >= 75) hasHit = true;
                        for (Entity shield : Shield.shieldMap.keySet()) {
                            if (shield.getLocation().distance(current)<Shield.shieldMap.get(shield).getRadius()+.5){
                                direction = Shield.shieldMap.get(shield).reflectVector(current,direction);
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
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(1.8));
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
                    if (!bolt.isDead())  slime.teleport(bolt);
                    if (p.isDead()){
                        p.setGravity(true);
                        bolt.remove();
                        slime.remove();
                        tick.get(bolt).cancel();
                    }
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!bolt.isDead())
                        p.setGravity(true);{
                        bolt.remove();
                        slime.remove();
                    }
                    if (!tick.get(bolt).isCancelled()) {
                        tick.get(bolt).cancel();
                    }

                }
            }, 10);
        }
    }

    public static void AirStrike(Player p, Plugin plugin) {
        if (Ultimate.hasUlt(p)) {
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
                    tracked.get(p).getWorld().spawnParticle(Particle.REDSTONE, tracked.get(p).getLocation(), 1, .1, .1, .1, 0, dust);
                }, 20, 20));
            }
        }
    }

    public static void AirStrikeRelease(Player p, Plugin plugin) {
        if (Ultimate.spendUlt(p)) {
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


            BukkitTask task = new BukkitRunnable(){
                @Override
                public void run() {
                    Location l = origin.toVector().toLocation(origin.getWorld());
                    Location strike = l.add((random.nextGaussian() * 15), 80, (random.nextGaussian() * 15));
                    Snowball snowball = (Snowball) strike.getWorld().spawnEntity(strike,EntityType.SNOWBALL);
                    snowball.setItem(new ItemStack(Material.FIRE_CHARGE));
                    snowball.setVelocity(new Vector(0, -2.5, 0));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            snowball.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, snowball.getLocation(), 2, .2, .4, .2,0, new Particle.DustTransition(Color.RED, Color.fromRGB(100,100,100), 3),true);
                            if (snowball.isDead()) {
                                snowball.remove();
                                snowball.getWorld().createExplosion(snowball.getLocation(), 4);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin,1,1);

                }
            }.runTaskTimer(plugin, 40, 2);

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (tick.containsKey(tracked.get(p))) tick.get(tracked.get(p)).cancel();
                    tracked.get(p).remove();
                    tracked.remove(p);
                    task.cancel();
                }
            }, 220);
        }
    }
}

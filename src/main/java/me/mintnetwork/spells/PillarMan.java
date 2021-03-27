package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;

public class PillarMan {
    public static void VoidPillar(Player p, Plugin plugin, Block block, BlockFace face, EffectManager em) {
        if (Mana.spendMana(p, 4)) {
            Zombie pillar = (Zombie) p.getWorld().spawnEntity(block.getLocation().add(face.getDirection().normalize()).add(.5, .5, .5), EntityType.ZOMBIE);
            pillar.setInvisible(true);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(pillar, "VoidPillar");
            pillar.setBaby();
            pillar.setAI(false);
            pillar.setGravity(false);
            pillar.getEquipment().setHelmet(new ItemStack(Material.BLACK_CONCRETE));
            pillar.setSilent(true);

            CircleEffect effect = new CircleEffect(em);
            effect.enableRotation = false;
            effect.setLocation(pillar.getLocation());
            effect.radius = 7;
            effect.particle = Particle.REDSTONE;
            effect.particleCount = 1;
            effect.particleSize = 3;
            effect.color = Color.BLACK;
            effect.wholeCircle = true;
            effect.iterations = 1;
            em.start(effect);

            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(pillar, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    pillar.getWorld().spawnParticle(Particle.REDSTONE, pillar.getLocation().add(0, 1, 0), 8, .2, .4, .2, 0, new Particle.DustOptions(Color.BLACK, 2));
                    pillar.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, pillar.getLocation().add(0, 2, 0), 4, 0, 0, 0, 1);
                    for (Entity e : pillar.getNearbyEntities(7, 7, 7)) {
                        if (e.getLocation().distance(pillar.getLocation())<=7){
                            boolean voidable = false;
                            boolean grenade = false;
                            if (e instanceof TNTPrimed) {
                                voidable = true;
                            }
                            if (e instanceof Firework) {
                                voidable = true;
                            }
                            if (ID.containsKey(e)) {
                                if (ID.get(e).equals("TNTGrenade")) {
                                    voidable = true;
                                    grenade = true;
                                }
                                if (ID.get(e).equals("StickyTNT")) {
                                    voidable = true;
                                    grenade = true;
                                }
                            }
                            if (voidable) {
                                LineEffect line = new LineEffect(em);
                                line.setTargetLocation(e.getLocation());
                                line.setLocation(pillar.getLocation());
                                line.particle = Particle.REDSTONE;
                                line.color = Color.BLACK;
                                line.particleSize = 2;
                                em.start(line);
                                if (grenade) {
                                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                                    Map<Entity, BukkitTask> activate = ProjectileInfo.getActivateCode();
                                    tick.get(e).cancel();
                                    linked.get(e).remove();
                                    activate.get(e).cancel();
                                }
                                e.remove();
                            }
                        }
                        if (pillar.isDead()) {
                            tick.get(pillar).cancel();
                        }

                    }

                }
            }, 1, 1));

        }
    }

    public static void ManaPillar(Player p, Plugin plugin, Block block, BlockFace face, EffectManager em) {
        if (Mana.spendMana(p, 4)) {
            Zombie pillar = (Zombie) p.getWorld().spawnEntity(block.getLocation().add(face.getDirection().normalize()).add(.5, .5, .5), EntityType.ZOMBIE);
            pillar.setInvisible(true);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(pillar, "ManaPillar");
            pillar.setBaby();
            pillar.setAI(false);
            pillar.setGravity(false);
            pillar.getEquipment().setHelmet(new ItemStack(Material.EMERALD_BLOCK));
            pillar.setSilent(true);

            CircleEffect effect = new CircleEffect(em);
            effect.enableRotation = false;
            effect.setLocation(pillar.getLocation());
            effect.radius = 7;
            effect.particle = Particle.VILLAGER_HAPPY;
            effect.particleCount = 1;
            effect.particleSize = 3;
            effect.wholeCircle = true;
            em.start(effect);
            final int[] count = {0};

            Map<Player, ArrayList<Entity>> limitMap = ProjectileInfo.ManaPillarLimit;

            if (limitMap.containsKey(p)){
                ArrayList<Entity> limit = ProjectileInfo.ManaPillarLimit.get(p);

                limit.removeIf(Entity::isDead);

                if (limit.size()>=3){
                    limit.get(0).remove();
                    limit.remove(0);
                }
                limit.add(pillar);
            } else {
                ArrayList<Entity> limit = new ArrayList<Entity>();
                limit.add(pillar);
                limitMap.put(p,limit);
            }



            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            BukkitTask mana = new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity e : pillar.getNearbyEntities(7, 7, 7)) {
                        if (e.getLocation().distance(pillar.getLocation()) <= 7) {
                            if (e instanceof Player) {
                                Mana.tickMana(p);
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin,0,30);
            tick.put(pillar, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    pillar.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, pillar.getLocation().add(0, 1, 0), 8, .2, .4, .2, 0);

                    if (pillar.isDead()) {
                        tick.get(pillar).cancel();
                        mana.cancel();
                    }

                }
            }, 1, 1));
        }
    }

    public static void BeamPillar(Player p, Plugin plugin, Block block, BlockFace face, EffectManager em) {
        if (face.getDirection().equals(new Vector(0, 1, 0))) {
            if (Mana.spendMana(p, 4)) {
                for (int i = 1; i < 4; i++) {
                    Block currentBlock = block.getLocation().add(face.getDirection().normalize().multiply(i)).getBlock();
                    if (currentBlock.isPassable()) {
                        currentBlock.setType(Material.BLACK_CONCRETE);
                    }
                }

                EnderCrystal pillar = (EnderCrystal) p.getWorld().spawnEntity(block.getLocation().add(face.getDirection().normalize().multiply(4)).add(.5, .5, .5), EntityType.ENDER_CRYSTAL);
                pillar.setShowingBottom(false);
                Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                ID.put(pillar, "BeamPillar");
                boolean[] coolDown = {true};
                Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                tick.put(pillar, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Entity aimed = null;
                        double aimedDistance = 20.0;
                        for (Entity e : pillar.getNearbyEntities(9, 9, 9)) {
                            if (e instanceof LivingEntity) {
                                if (pillar.getLocation().distance(e.getLocation()) < aimedDistance) {
                                    if (!(e instanceof ArmorStand)) {
                                        Vector direction = pillar.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(-.8);
                                        RayTraceResult ray = pillar.getWorld().rayTrace(pillar.getLocation(), direction, 18, FluidCollisionMode.NEVER, true, .1, Predicate.isEqual(e));
                                        Vector eyeDirection = pillar.getLocation().toVector().subtract(((LivingEntity) e).getEyeLocation().toVector()).normalize().multiply(-.8);
                                        RayTraceResult eyeRay = pillar.getWorld().rayTrace(pillar.getLocation(), eyeDirection, 18, FluidCollisionMode.NEVER, true, .1, Predicate.isEqual(e));
                                        if (ray != null) {
                                            if (ray.getHitEntity() != null) {
                                                if (e instanceof Player) {
                                                    if (!((Player) e).isInvisible()) {
                                                        aimed = e;
                                                        aimedDistance = pillar.getLocation().distance(e.getLocation());

                                                    }
                                                } else {
                                                    aimed = e;
                                                    aimedDistance = pillar.getLocation().distance(e.getLocation());
                                                }

                                            }
                                        } else if (eyeRay != null) {
                                            if (eyeRay.getHitEntity() != null) {
                                                if (e instanceof Player) {
                                                    if (!((Player) e).isInvisible()) {
                                                        aimed = e;
                                                        aimedDistance = pillar.getLocation().distance(e.getLocation());

                                                    }
                                                } else {
                                                    aimed = e;
                                                    aimedDistance = pillar.getLocation().distance(e.getLocation());
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (aimed != null) {
                            pillar.setBeamTarget(aimed.getLocation().subtract(0, 2, 0));
                            if (!coolDown[0]) {

                                Vector aimedDirection = pillar.getLocation().toVector().subtract(aimed.getLocation().toVector()).normalize().multiply(-.8);
                                RayTraceResult aimedRay = pillar.getWorld().rayTrace(pillar.getLocation(), aimedDirection, 18, FluidCollisionMode.NEVER, true, .1, Predicate.isEqual(aimed));
                                Vector aimedEyeDirection = pillar.getLocation().toVector().subtract(((LivingEntity) aimed).getEyeLocation().toVector()).normalize().multiply(-.8);

                                Firework firework = (Firework) pillar.getWorld().spawnEntity(pillar.getLocation().add(aimedDirection.multiply(2)), EntityType.FIREWORK);
                                FireworkEffect.Builder effect = FireworkEffect.builder();
                                FireworkMeta meta = firework.getFireworkMeta();
                                effect.with(FireworkEffect.Type.BALL);
                                effect.withColor(Color.fromBGR(255, 20, 255));
                                meta.addEffect(effect.build());
                                firework.setShotAtAngle(true);
                                boolean hitFeet = false;
                                if (aimedRay != null) {
                                    if (aimedRay.getHitEntity() != null) {
                                        firework.setVelocity(aimedDirection);
                                        hitFeet = true;
                                    }
                                }
                                if (!hitFeet) {
                                    firework.setVelocity(aimedEyeDirection);
                                }
                                firework.setFireworkMeta(meta);

                                coolDown[0] = true;
                                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        coolDown[0] = false;
                                    }
                                }, 30);
                            }
                        } else {
                            pillar.setBeamTarget(null);
                        }

                        if (coolDown[0]) {
                            pillar.getWorld().spawnParticle(Particle.PORTAL, pillar.getLocation().add(0, .5, 0), 2, 0, 0, 0, 2);
                        }

                        if (pillar.isDead()) {
                            tick.get(pillar).cancel();
                        }
                    }
                }, 1, 1));
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        coolDown[0] = false;
                    }
                }, 100);
            }
        }
    }

    public static void ArrowTurret(Player p, Plugin plugin, Block block, BlockFace face,EffectManager em) {
        if (face.getDirection().equals(new Vector(0, 1, 0))) {
            if (Ultimate.spendUlt(p)) {
                for (int i = 1; i < 4; i++) {
                    Block currentBlock = block.getLocation().add(face.getDirection().normalize().multiply(i)).getBlock();
                    if (currentBlock.isPassable()) {
                        currentBlock.setType(Material.BONE_BLOCK);
                    }
                }


                Skeleton skeleton = (Skeleton) p.getWorld().spawnEntity(block.getLocation().add(face.getDirection().normalize().multiply(4)).add(.5, 0, .5), EntityType.SKELETON);
                skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
                skeleton.getEquipment().setItemInOffHand(new ItemStack(Material.BOW));
                skeleton.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
                skeleton.setInvisible(true);
                skeleton.setAI(false);
                Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                final boolean[] isActive = {false};
                final int[] count = {0};
                final int[] charge = {0};

                CircleEffect effect = new CircleEffect(em);
                effect.enableRotation = false;
                effect.setLocation(skeleton.getLocation());
                effect.radius = 15;
                effect.particle = Particle.SMOKE_NORMAL;
                effect.particleCount = 1;
                effect.particleSize = 3;
                effect.wholeCircle = true;
                effect.iterations = 2;
                em.start(effect);

                tick.put(skeleton, new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (isActive[0]) {
                            Entity aimed = null;
                            double aimedDistance = 20.0;

                            for (Entity e : skeleton.getNearbyEntities(16, 16, 16)) {
                                if (e instanceof LivingEntity) {
                                    if (!(e instanceof ArmorStand)) {
                                        if (skeleton.getLocation().distance(e.getLocation()) < aimedDistance) {
                                            aimed = e;
                                            aimedDistance = skeleton.getLocation().distance(e.getLocation());
                                        }
                                    }
                                }
                            }

                            for (Entity projectile : skeleton.getNearbyEntities(5, 5, 5)) {
                                if (projectile instanceof Projectile) {
                                    if (projectile.getLocation().distance(skeleton.getLocation()) <= 3.5) {


                                        Vector d = projectile.getVelocity();
                                        Vector n = skeleton.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize().multiply(-1);
                                        if (d.dot(n) <= 0) {
                                            SphereEffect sphere = new SphereEffect(em);
                                            sphere.setLocation(skeleton.getLocation());
                                            sphere.particle = Particle.SMOKE_NORMAL;
                                            sphere.radius = 3;
                                            sphere.iterations = 1;
                                            em.start(sphere);

                                            charge[0] = charge[0] - 12;

                                            Vector v = d.subtract(n.multiply(d.dot(n) * 2));
                                            v = v.normalize().multiply(projectile.getVelocity().length());
                                            projectile.setVelocity(v);
                                            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                                            if (velocity.containsKey(projectile)) {
                                                velocity.replace(projectile, v);
                                            }
                                        }
                                    }
                                }
                            }
                            if (aimed != null) {
                                if (aimed.getLocation().distance(skeleton.getLocation()) <= 15) {
                                    skeleton.teleport(skeleton.getLocation().setDirection(skeleton.getLocation().toVector().subtract(aimed.getLocation().toVector()).normalize().multiply(-1)));
                                    count[0]++;
                                    if (count[0] >= 8) {
                                        skeleton.launchProjectile(Arrow.class);
                                        count[0] = 0;
                                        charge[0] = charge[0] - 2;
                                    }
                                }
                            }
                            if (charge[0] <= 0) {
                                charge[0] = 0;
                                skeleton.getWorld().playSound(skeleton.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                                skeleton.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
                                isActive[0] = false;
                            }
                        } else {
                            charge[0]++;
                            skeleton.getWorld().spawnParticle(Particle.SMOKE_NORMAL, skeleton.getLocation().add(0, 1, 0), 1, .2, .4, .2, .1);
                            if (charge[0] >= 80) {
                                skeleton.getEquipment().setHelmet(new ItemStack(Material.SKELETON_SKULL));
                                isActive[0] = true;
                            }
                        }

                        if (skeleton.isDead()) this.cancel();
                    }
                }.runTaskTimer(plugin, 1, 1));

            }
        }
    }
}

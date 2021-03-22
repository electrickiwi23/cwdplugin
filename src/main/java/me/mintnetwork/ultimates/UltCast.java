package me.mintnetwork.ultimates;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
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

public class UltCast {

    public static void ClusterBomb(Player p, Plugin plugin) {
        //Spend Ult
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.PRIMED_TNT);
        tnt.setVelocity(p.getEyeLocation().getDirection().multiply(1.5));
        tnt.setFuseTicks(120);
        ID.put(tnt, "Cluster 0");
        tick.put(tnt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
            tnt.getWorld().spawnParticle(Particle.SMOKE_LARGE, tnt.getLocation(), 1, .4, .4, .4, 0);
            tnt.getWorld().spawnParticle(Particle.REDSTONE, tnt.getLocation(), 1, .5, .5, .5, 0, dust);
        }, 1, 1));


    }

    public static void AirStrike(Player p, Plugin plugin) {
        //Spend Ult
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        Map<Player, Entity> tracked = ProjectileInfo.getStrikeTrackedEntity();
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
        //Spend Ult
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

    public static void BloodUlt(Player p, Plugin plugin) {
        //Spend Ult
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

    public static void ImmortalPotionUlt(Player p) {
        //Spend Ult
        ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
        ItemStack item = (new ItemStack(Material.SPLASH_POTION));
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        potionMeta.setColor(Color.fromRGB(255, 215, 0));
        item.setItemMeta(potionMeta);
        potion.setItem(item);
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        ID.put(potion, "Immortal Potion");
    }

    public static void PaintActivateUlt(Player p, Plugin plugin) {
        Map<LivingEntity, Integer> painted = StatusEffects.paintTimer;
        if (painted.keySet().size() > 0) {
            //Spend Ult
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
                    if(paintActivateCount[0]>=20){
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
            }.runTaskTimer(plugin,1,1);
        }
    }

    public static void ElementBlast(Player p, Plugin plugin, EffectManager em) {
        //Spend Ult
        Snowball bolt = p.launchProjectile(Snowball.class);
        Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
        velocity.put(bolt, p.getEyeLocation().getDirection().multiply(.7));
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        bolt.setItem(new ItemStack(Material.FIREWORK_STAR));
        bolt.setGravity(false);
        ID.put(bolt, "Element Blast");
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

        tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
            Map<Entity, Vector> velocity1 = ProjectileInfo.getLockedVelocity();
            bolt.setVelocity(velocity1.get(bolt));
            bolt.getWorld().spawnParticle(Particle.FLAME,bolt.getLocation(),20,.2,.2,.2,0);
            bolt.getWorld().spawnParticle(Particle.SNOW_SHOVEL,bolt.getLocation(),20,.3,.3,.3,0);
            LineEffect line = new LineEffect(em);
            line.setLocation(bolt.getLocation());
            line.setTargetLocation(bolt.getLocation().add( Math.random()*4-2,Math.random()*4-2,Math.random()*4-2));
            line.isZigZag = true;
            line.zigZags = 2;
            line.zigZagOffset = new Vector(Math.random()*.06-.03,Math.random()*.06-.03,Math.random()*.06-.03);
            line.particle = Particle.REDSTONE;
            line.color = Color.YELLOW;
            line.start();
            for (Entity e:bolt.getNearbyEntities(5,5,5)) {
                if (e.getLocation().distance(bolt.getLocation())<=4){
                    if (e instanceof LivingEntity){
                        if (!(e.equals(p))) {
                            ((LivingEntity) e).damage(1);
                            ((LivingEntity) e).setNoDamageTicks(0);
                            e.setVelocity(new Vector(0, 0, 0));
                        }
                    }
                }
            }
        }, 1, 1));
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            tick.get(bolt).cancel();
            bolt.remove();
        }, 100);
    }

    public static void ObsidianWall(Player p, Plugin plugin) {
        //Spend Ult
        Map<Block, Integer> decay = StatusEffects.ObsidianDecay;
        Location l = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(5));
        l.getBlock().setType(Material.OBSIDIAN);
        l.setYaw(l.getYaw()+90);
        l.setPitch(0);
        final int[] i = {0};

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                Location current = l.clone();
                current.add(l.getDirection().multiply(i[0]));
                current.add(0,-6,0);
                for (int j = 0; j < 13; j++) {
                    current.getBlock().setType(Material.NETHERITE_BLOCK);
                    decay.put(current.getBlock(),(int) (Math.random()*160-80));
                    current = current.add(0,1,0);
                }
                current = l.clone();
                current.add(l.getDirection().multiply(i[0]).multiply(-1));
                current.add(0,-6,0);
                for (int j = 0; j < 13; j++) {
                    current.getBlock().setType(Material.NETHERITE_BLOCK);
                    decay.put(current.getBlock(), (int) (Math.random()*160-80));
                    current = current.add(0,1,0);
                }
                i[0]++;

            }
        }.runTaskTimer(plugin,1,1);
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> task.cancel(),12);
    }

    public static void TornadoBlast(Player p, Plugin plugin) {
        //Spend Ult
        Snowball grenade = p.launchProjectile(Snowball.class);
        Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
        velocity.put(grenade, p.getEyeLocation().getDirection());
        Map<Entity, String> ID = ProjectileInfo.getProjectileID();
        grenade.setItem(new ItemStack(Material.BONE_MEAL));
        ID.put(grenade, "TornadoUlt");
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
        tick.put(grenade, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                p.getWorld().spawnParticle(Particle.CLOUD, grenade.getLocation(), 3, .2, .2, .2, 0);
            }
        }, 1, 1));
    }

    public static void ArrowTurret(Player p, Plugin plugin, Block block, BlockFace face,EffectManager em) {
        //Spend Ult
        if (face.getDirection().equals(new Vector(0, 1, 0))) {
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
            tick.put(skeleton, new BukkitRunnable() {
                @Override
                public void run() {
                    if (isActive[0]) {
                        Entity aimed = null;
                        double aimedDistance = 20.0;

                        for (Entity e : skeleton.getNearbyEntities(9, 9, 9)) {
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
                            skeleton.getWorld().playSound(skeleton.getLocation(),Sound.BLOCK_FIRE_EXTINGUISH,1,1);
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
    public static void ConsumingNight(Player p, Plugin plugin,EffectManager em){
        Vector direction = p.getEyeLocation().getDirection();
        RayTraceResult ray =  p.getWorld().rayTrace(p.getEyeLocation().add(direction),direction,50,FluidCollisionMode.NEVER,true,.1,null);
        if (ray != null) {
            //Spend Ult
            Location hit = ray.getHitPosition().toLocation(p.getWorld());

            SphereEffect sphere = new SphereEffect(em);
            sphere.radius = 1;
            sphere.particle = Particle.REDSTONE;
            sphere.particleSize = 5;
            sphere.color = Color.BLACK;
            sphere.radiusIncrease = 1;
            sphere.setLocation(hit);
            em.start(sphere);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (Entity e : p.getWorld().getNearbyEntities(hit,20,20,20)) {
                    if (e.getLocation().distance(hit)<=20) {
                        if (e instanceof Player) {
                            Player victim = (Player) e;
                            if (StatusEffects.ShadowConsumed.containsKey(victim)){
                                StatusEffects.ShadowConsumed.replace(victim,180);
                            } else {
                                StatusEffects.ShadowConsumed.put(victim,180);
                            }

                            victim.setPlayerTime(114000,false);

                            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,380,2));
                            victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,360,0));


                        }
                    }
                }
                sphere.cancel();
            }, 20);
        }
    }
    public static void SirenSong(Player p, Plugin plugin){

    }
}


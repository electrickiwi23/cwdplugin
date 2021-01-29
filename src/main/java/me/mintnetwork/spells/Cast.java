package me.mintnetwork.spells;

import com.sun.tools.javac.jvm.Items;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.SphereEffect;
import de.slikey.effectlib.effect.TraceEffect;
import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Cake;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Predicate;

public class Cast {

    private final Main plugin;

    public Cast(Main plugin) {
        this.plugin = plugin;
    }

    public static void ShieldDome(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, 4)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(stand, "ShieldDome");
            stand.setVisible(false);
            stand.setMarker(true);
            SphereEffect effect = new SphereEffect(em);
            effect.setLocation(p.getLocation().add(0, 0, 0));
            effect.particle = Particle.END_ROD;
            effect.particleCount = 1;
            effect.radius = 4;
            effect.duration = 15;
            em.start(effect);
            BukkitTask reflection = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    for (Entity projectile : stand.getNearbyEntities(5, 5, 5)) {
                        if (projectile instanceof Projectile) {
                            if (projectile.getLocation().distance(stand.getLocation()) <= 4.5) {

                                Vector d = projectile.getVelocity();
                                Vector n = stand.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize().multiply(-1);
                                if (d.dot(n) <= 0 && projectile.getLocation().distance(stand.getLocation()) >= 3) {
                                    Vector v = d.subtract(n.multiply(d.dot(n) * 2));
                                    projectile.setVelocity(v);
                                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                                    if (velocity.containsKey(projectile)) {
                                        velocity.replace(projectile, v);
                                    }
                                }
                            }
                        }

                    }
                }
            }, 0, 1);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    stand.remove();
                    reflection.cancel();
                }
            }, 500);
        }
    }

    public static void QuickBuild(Player p, Plugin plugin, BlockFace face, Block block) {
        for (int i = 1; i < 5; i++) {
            int finalI = i;
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    block.getLocation().add(face.getDirection().normalize().multiply(finalI)).getBlock().setType(Material.BLUE_WOOL);
                    for (Entity e : block.getWorld().getNearbyEntities(block.getLocation().add(face.getDirection().normalize().multiply(finalI)).add(.5, .5, .5), .5, .5, .5)) {
                        if (e instanceof LivingEntity) e.setVelocity(face.getDirection());
                    }
                }
            }, i);

        }
    }

    public static void PopUpTower(Player p, Plugin plugin, BlockFace face, Block block) {
        if (Mana.spendMana(p, 4)) {
            for (int i = 1; i < 6; i++) {
                int finalI = i;
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < 5; j++) {
                            for (int k = 0; k < 5; k++) {
                                Block place = block.getLocation().add(j - 2, finalI, k - 2).getBlock();

                                if (place.isPassable()) {

                                    if (Math.abs(j - 2) == 2 && Math.abs(k - 2) == 0) {
                                        place.setType(Material.SCAFFOLDING);
                                    } else if (Math.abs(j - 2) == 0 && Math.abs(k - 2) == 2) {
                                        place.setType(Material.SCAFFOLDING);
                                    } else {
                                        place.setType(Material.BLUE_CONCRETE_POWDER);
                                    }
                                }


                            }
                        }
                        for (Entity e : block.getWorld().getNearbyEntities(block.getLocation().add(.5, .5 + finalI, .5), 2.5, .5, 2.5)) {
                            if (e instanceof LivingEntity) e.setVelocity(new Vector(0, 1, 0));
                        }
                    }
                }, i);
            }
        }
    }

    public static void BuildBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(.4));
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setVelocity(p.getEyeLocation().getDirection().multiply(.4));
            bolt.setItem(new ItemStack(Material.BLUE_CONCRETE));
            bolt.setGravity(false);
            ID.put(bolt, "BuildBolt");
            p.teleport(p.getLocation().add(0, 2.5, 0));

            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    Vector v = velocity.get(bolt);
                    bolt.setVelocity(v);
                    if (bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().isPassable()) {
                        bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.BLUE_CONCRETE);
                    }
                }
            }, 3, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    tick.get(bolt).cancel();
                    bolt.remove();
                }
            }, 80);

        }
    }

    public static void SpeedBlink(Player p) {
        if (Mana.spendMana(p, 3)) p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 60, true, false));
    }

    public static void BloodBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
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
        }
    }

    public static void JumpBoost(Player p) {
        if (Mana.spendMana(p, 1)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 2, true, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1, true, true));
        }
    }

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

            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(pillar, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    pillar.getWorld().spawnParticle(Particle.REDSTONE, pillar.getLocation().add(0, 1, 0), 8, .2, .4, .2, 0, new Particle.DustOptions(Color.BLACK, 2));
                    pillar.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, pillar.getLocation().add(0, 2, 0), 4, 0, 0, 0, 1);
                    for (Entity e : pillar.getNearbyEntities(7, 7, 7)) {
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
                        if (pillar.isDead()) {
                            tick.get(pillar).cancel();
                        }

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


    public static void TNTLine(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            for (int i = 1; i < 6; i++) {
                int finalI = i;
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Entity tnt = p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.PRIMED_TNT);
                        tnt.setVelocity(p.getEyeLocation().getDirection().multiply(finalI).multiply(.32));
                    }
                }, i);
            }
        }
    }


    public static void TNTGrenade(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 1)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setInvisible(true);
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            grenade.setItem(new ItemStack(Material.TNT));
            grenade.setBounce(true);

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(stand, grenade);
            linked.put(grenade, stand);
            ID.put(stand, "TNTGrenade");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            Map<Entity, BukkitTask> activate = ProjectileInfo.getActivateCode();
            tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    stand.teleport(linked.get(stand));
                    stand.getWorld().spawnParticle(Particle.FLAME, stand.getLocation(), 1, .1, .1, .1, .02);
                }
            }, 1, 1));
            activate.put(stand, Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    stand.getWorld().createExplosion(stand.getLocation(), 2);
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    linked.get(stand).remove();
                    tick.get(stand).cancel();
                    stand.remove();

                }
            }, 100));
        }

    }


    public static void StickyTNTGrenade(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 1)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setInvisible(true);
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            grenade.setItem(new ItemStack(Material.TNT));

            Map<Entity, List<Double>> offset = ProjectileInfo.getStickyOffset();
            List<Double> sticky = new ArrayList<>();
            sticky.add(0, 0.0);
            sticky.add(1, 0.0);
            sticky.add(2, 0.0);
            offset.put(stand, sticky);

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(stand, grenade);
            linked.put(grenade, stand);
            ID.put(stand, "StickyTNT");
            ID.put(grenade, "StickyTNT");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            Map<Entity, BukkitTask> activate = ProjectileInfo.getActivateCode();
            tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    Map<Entity, List<Double>> offset = ProjectileInfo.getStickyOffset();
                    if (linked.get(stand) != null) {
                        double x = linked.get(stand).getLocation().getX() + offset.get(stand).get(0);
                        double y = linked.get(stand).getLocation().getY() + offset.get(stand).get(1);
                        double z = linked.get(stand).getLocation().getZ() + offset.get(stand).get(2);

                        stand.teleport(new Location(stand.getWorld(), x, y, z));
                    }
                    stand.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, stand.getLocation(), 1, .1, .1, .1, .02);
                }
            }, 1, 1));
            activate.put(stand, Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    stand.getWorld().createExplosion(stand.getLocation(), (float) 1.5);
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    if (linked.get(stand) != null) {
                        if (!(linked.get(stand) instanceof LivingEntity)) {
                            linked.get(stand).remove();
                        }
                    }
                    tick.get(stand).cancel();

                    stand.remove();

                }
            }, 100));
        }

    }


    public static void LightningBolt(Player p, Plugin plugin, EffectManager em) {
        if (Mana.spendMana(p, 4)) {
            Vector v = p.getEyeLocation().getDirection();
            Location l = p.getEyeLocation().add(v.normalize());
            BukkitTask cloud = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(50, 50, 50), 2);
                    p.getWorld().spawnParticle(Particle.REDSTONE, l, 20, .15, .15, .15, dust);
                }
            }, 1, 1);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {

                    RayTraceResult ray = p.getWorld().rayTrace(l, v, 80, FluidCollisionMode.NEVER, true, .2, null);
                    LineEffect effect = new LineEffect(em);
                    LivingEntity hitEntity = null;
                    Location hitLocation = null;
                    System.out.println(effect.particleCount);

                    effect.setLocation(l);
                    effect.isZigZag = true;
                    effect.zigZags = 3;
                    effect.zigZagOffset = new Vector(0, .03, 0);
                    effect.particle = Particle.REDSTONE;
                    effect.color = Color.YELLOW;
                    effect.setTargetLocation(l.add(v.multiply(80)));

                    if (ray != null) {
                        try {
                            hitLocation = ray.getHitPosition().toLocation(p.getWorld());
                        } catch (Exception ignore) {
                        }
                        try {
                            hitEntity = (LivingEntity) ray.getHitEntity();
                        } catch (Exception ignore) {
                        }
                        if (hitLocation != null) {
                            effect.setTargetLocation(hitLocation);
                        }
                        if (hitEntity != null) {
                            hitEntity.damage(4, p);
                        }
                    }
                    effect.particleCount = (int) effect.getTarget().distance(effect.getLocation()) / 20;
                    em.start(effect);
                    cloud.cancel();
                }
            }, 10);
        }
    }


    public static void BloodSacrifice(Player p) {
        Map<Player, Integer> playerMana = Mana.getPlayerMana();
        playerMana.replace(p, playerMana.get(p) + 3);
        p.damage(6);
        if (playerMana.get(p) > 10) {
            playerMana.replace(p, 10);
        }
        p.setLevel(playerMana.get(p));

    }


    public static void HealBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.PINK_DYE));
            bolt.setGravity(false);
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
            if (Mana.spendMana(p, 4)) {
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
                            for (Entity e : pillarLocation.getWorld().getNearbyEntities(pillarLocation.getLocation(), 6, 6, 6)) {
                                if (e instanceof LivingEntity) {
                                    LivingEntity live = (LivingEntity) e;
                                    if (live.getMaxHealth() - 1 >= Math.ceil(live.getHealth())) {
                                        live.getWorld().spawnParticle(Particle.HEART, live.getLocation().add(0, 1, 0), 4, .2, .4, .2);
                                    }
                                }
                            }
                            if (!cakeLocation.getType().equals(Material.CAKE)) {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 1, 30);
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


    public static void EngineBurst(Player p) {
        if (Mana.spendMana(p, 3)) {

            Location center = p.getLocation().add(p.getEyeLocation().getDirection().multiply(2));
            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, center.add(0, 1, 0), 4, .4, .4, .4);

            for (Entity e : p.getWorld().getNearbyEntities(center, 3, 3, 3)) {

                if (e instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) e;
                    Vector v = center.toVector().subtract(e.getLocation().toVector()).multiply(center.distance(e.getLocation()) * -.4);
                    living.setVelocity(v.add(new Vector(0, .8, 0)));

                }
            }
        }
    }


    public static void ShadowInvis(Player p, Plugin plugin) {
        Collection<Player> status = StatusEffects.getShadowInvis();
        if (!status.contains(p)) {
            if (Mana.spendMana(p, 3)) {
                status.add(p);
                ItemStack[] oldArmor = p.getInventory().getArmorContents();
                ItemStack[] noArmor = new ItemStack[4];
                noArmor[0] = null;
                noArmor[1] = null;
                noArmor[2] = null;
                noArmor[3] = null;

                p.getInventory().setArmorContents(noArmor);
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, true, false));
                Map<Player, Runnable> CancelMap = StatusEffects.getShadowCancel();
                Runnable InvisCancel = new Runnable() {
                    @Override
                    public void run() {
                        status.remove(p);
                        p.getInventory().setArmorContents(oldArmor);
                        p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        CancelMap.remove(p);

                    }
                };

                CancelMap.put(p, InvisCancel);
                BukkitTask InvisCancelTask = Bukkit.getServer().getScheduler().runTaskLater(plugin, InvisCancel, 100);

                BukkitTask checker = new BukkitRunnable() {
                    public void run() {
                        if (!status.contains(p)) {
                            InvisCancelTask.cancel();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 1, 1);
            }
        }
    }


    public static void ZombieSpawn(Player p) {
        if (Mana.spendMana(p, 5)) {
        }
    }


    //casting code for Air needdles
    public static void AirNeedles(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, 2)) {
            Arrow arrow = p.launchProjectile(Arrow.class);
            TraceEffect effect = new TraceEffect(em);
            effect.setEntity(arrow);
            effect.particle = Particle.SPELL;
            effect.duration = 1;
            effect.disappearWithTargetEntity = true;
            em.start(effect);
            arrow.addScoreboardTag("windArrow");
            arrow.setDamage(1);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    TraceEffect effect = new TraceEffect(em);
                    effect.setEntity(arrow);
                    effect.particle = Particle.SPELL;
                    effect.duration = 1;
                    effect.disappearWithTargetEntity = true;
                    em.start(effect);
                    arrow.addScoreboardTag("windArrow");
                    arrow.setDamage(1);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

                }
            }, 3);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    TraceEffect effect = new TraceEffect(em);
                    effect.setEntity(arrow);
                    effect.particle = Particle.SPELL;
                    effect.duration = 1;
                    effect.disappearWithTargetEntity = true;
                    em.start(effect);
                    arrow.addScoreboardTag("windArrow");
                    arrow.setDamage(1);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                }
            }, 6);
        }
    }


    public static void CloudBurst(Player p, Plugin plugin) {
        Location l = p.getLocation();
        p.setVelocity(new Vector(0, 2, 0));
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                p.getWorld().createExplosion(l, 2);
            }
        }, 4);
    }


    public static void AirDash(Player p, Plugin plugin) {
        p.setGravity(false);
        p.setVelocity(p.getVelocity().add(p.getEyeLocation().getDirection()));
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                p.setGravity(true);
            }
        }, 10);
    }

    public static void SprayPaint(Player p,Plugin plugin) {
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
                                        if (Math.abs(hitLocation.getY() - hitEntity.getEyeLocation().getY()) <= .4) {
                                            hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 2));
                                        }
                                        Map<LivingEntity, Integer> painted = StatusEffects.getPaintTimer();
                                        if (painted.containsKey(hitEntity)) {
                                            painted.replace(hitEntity, painted.get(hitEntity) + 80);
                                        } else {
                                            painted.put(hitEntity, 80);
                                        }
                                        hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 1));
                                    }
                                }

                            }
                            if (!hasHit) {
                                p.getWorld().playSound(current, Sound.ENTITY_CAT_HISS, 1, 1);

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

                                for (Entity e : current.getWorld().getNearbyEntities(current, 3, 3, 3)) {
                                    if (e instanceof LivingEntity) {
                                        LivingEntity live = (LivingEntity) e;
                                        if (current.distance(live.getEyeLocation()) < 1) {

                                        }
                                    }
                                }

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
                        }
                    }
                }, i);
            }
        }
    }

    public static void PaintBomb(Player p, Plugin plugin){
        if (Mana.spendMana(p, 3)) {
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            grenade.setItem(new ItemStack(Material.FIREWORK_STAR));
            grenade.setCustomName("A Paint Canister");
            ID.put(grenade, "PaintBomb");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(grenade, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {

                    Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                    p.getWorld().spawnParticle(Particle.REDSTONE, grenade.getLocation(), 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.ORANGE, 2);
                    p.getWorld().spawnParticle(Particle.REDSTONE, grenade.getLocation(), 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.YELLOW, 2);
                    p.getWorld().spawnParticle(Particle.REDSTONE, grenade.getLocation(), 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 2);
                    p.getWorld().spawnParticle(Particle.REDSTONE, grenade.getLocation(), 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.BLUE, 2);
                    p.getWorld().spawnParticle(Particle.REDSTONE, grenade.getLocation(), 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 2);
                    p.getWorld().spawnParticle(Particle.REDSTONE, grenade.getLocation(), 1, .1, .1, .1, 0, dust);
                }
    },1,1));
    }
}





    public static void SniperBolt(Player p, Plugin plugin) {
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


    public static void DragonGrenade(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setInvisible(true);
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            grenade.setItem(new ItemStack(Material.CRYING_OBSIDIAN));
            grenade.setBounce(true);

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(stand, grenade);
            linked.put(grenade, stand);
            ID.put(stand, "DragonGrenade");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    stand.teleport(linked.get(stand));
                    stand.getWorld().spawnParticle(Particle.DRAGON_BREATH, stand.getLocation(), 2, .1, .1, .1, .04);
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2, 1);
                    AreaEffectCloud cloud = (AreaEffectCloud) stand.getWorld().spawnEntity(stand.getLocation(), EntityType.AREA_EFFECT_CLOUD);
                    cloud.setParticle(Particle.DRAGON_BREATH);
                    cloud.setRadius(4);
                    cloud.setDuration(200);
                    cloud.setCustomName("A Dragon Grenade");
                    cloud.setReapplicationDelay(10);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 1, false, false), true);
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    linked.get(stand).remove();
                    tick.get(stand).cancel();
                    stand.remove();

                }
            }, 80);

        }
    }

    public static void BatSonar(Player p,Plugin plugin){
        if (Mana.spendMana(p,3)){
            Bat bat = (Bat) p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.BAT);
            bat.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
            bat.setGlowing(true);;
            Vector v = p.getEyeLocation().getDirection().multiply(.4);
            final Location[] set = {null};
            final int[] age = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    age[0]++;
                    if (age[0]==100) { set[0] = bat.getLocation(); }
                    if (age[0]<100){
                        bat.setVelocity(v);
                    } else {
                        if (set[0] !=null) {
                            bat.teleport(set[0]);
                            bat.setVelocity(new Vector(0, 0, 0));
                        }
                    }

                    if (age[0]>30){
                        for (Entity e:bat.getWorld().getNearbyEntities(bat.getLocation(),9,9,9)) {
                            if (e instanceof LivingEntity){
                                if (!(e instanceof ArmorStand)){
                                    ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20,1));
                                }
                            }

                        }
                    }
                    if (age[0]>300){
                        bat.remove();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin,1,1);


        }
    }

    public static void TNTRing(Player p ){
        for (int i = 0; i < 8; i++) {
            TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.PRIMED_TNT);
            Location location = p.getLocation();
            location.setYaw(location.getYaw() + (45 * i));
            location.setPitch(0);
            tnt.setFuseTicks(120);
            tnt.setSource(p);
            tnt.setVelocity(location.getDirection().add(new Vector(0,1,0)).multiply(.22));
        }

    }

    public static void BeeBolt(Player p,Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.HONEYCOMB));
            bolt.setGravity(false);
            ID.put(bolt, "BeeBolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    Particle.DustOptions dust = new Particle.DustOptions(Color.YELLOW, 2);
                    bolt.getWorld().spawnParticle(Particle.REDSTONE, bolt.getLocation(), 1, .1, .1, .1, dust);
                    dust = new Particle.DustOptions(Color.BLACK, 2);
                    bolt.getWorld().spawnParticle(Particle.REDSTONE, bolt.getLocation(), 1, .1, .1, .1, dust);
                }
            }, 1, 1));
        }
    }


    public static Firework FireworkBolt(Player p) {
        if (Mana.spendMana(p, 2)) {
            Firework firework = (Firework) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREWORK);
            FireworkEffect.Builder effect = FireworkEffect.builder();
            FireworkMeta meta = firework.getFireworkMeta();
            effect.with(FireworkEffect.Type.BALL);
            double r = (Math.ceil(Math.random() * 6));
            if (r == 1.0) effect.withColor(Color.RED);
            if (r == 2.0) effect.withColor(Color.ORANGE);
            if (r == 3.0) effect.withColor(Color.YELLOW);
            if (r == 4.0) effect.withColor(Color.fromBGR(0, 255, 0));
            if (r == 5.0) effect.withColor(Color.BLUE);
            if (r == 6.0) effect.withColor(Color.fromBGR(255, 0, 255));
            meta.addEffect(effect.build());
            firework.setShooter(p);
            firework.setShotAtAngle(true);
            firework.setVelocity(p.getEyeLocation().getDirection());
            firework.setFireworkMeta(meta);
            return firework;
        }
        return null;
    }
}
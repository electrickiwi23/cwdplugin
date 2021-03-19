package me.mintnetwork.spells;

import com.sun.tools.javac.jvm.Items;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.*;
import jdk.internal.icu.util.CodePointTrie;
import me.mintnetwork.Main;
import me.mintnetwork.initialization.TeamsInit;
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
import org.bukkit.inventory.meta.LeatherArmorMeta;
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

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;
import java.util.zip.Adler32;

public class Cast {

    public static void SpeedBoost(Player p){
        Map<LivingEntity, Integer> speedMap = StatusEffects.getSpeedTimer();
        if (!speedMap.containsKey(p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1,false,true));
            speedMap.put(p,30);

        }
    }

    public static void ForcePull(Player p,Plugin plugin,EffectManager em){
        int distance = 15;
        Location start = p.getEyeLocation();
        RayTraceResult ray = p.getWorld().rayTraceBlocks(start,start.getDirection(),distance,FluidCollisionMode.NEVER,true);
        if (ray != null){
            distance = (int) Math.ceil(ray.getHitPosition().distance(start.toVector()));
        }

        final Location[] Current = {p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(distance)).setDirection(p.getEyeLocation().getDirection().multiply(-1))};
        VortexEffect effect = new VortexEffect(em);
        effect.setLocation(Current[0]);
        effect.radius = 3;
        effect.circles = 20;
        effect.iterations = distance;
        effect.particle = Particle.BUBBLE_POP;
        List<Entity> CantHit = new ArrayList<>();
        CantHit.add(p);

        em.start(effect);
        final int[] count = {0};
        int finalDistance = distance;
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                count[0]++;
                for (Entity e: Current[0].getWorld().getNearbyEntities(Current[0],3,3,3)) {
                    if (e instanceof LivingEntity){
                        if (!(e instanceof ArmorStand)){
                            if (!CantHit.contains(e)) {
                                e.setVelocity(e.getVelocity().add(e.getLocation().toVector().subtract(start.toVector()).multiply(-1).add(new Vector(0, 1, 0)).normalize().multiply(1.5)));
                                CantHit.add(e);
                            }
                        }
                    }
                }
                Current[0] = Current[0].add(Current[0].getDirection());
                if (count[0]>= finalDistance){
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin,1,1);
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
        if (Mana.spendMana(p, 1)) {
            final Location[] current = {block.getLocation().add(face.getDirection().normalize())};
            final int[] count = {0};
            String TeamName = "Red";
            BukkitTask tick = new BukkitRunnable() {
                @Override
                public void run() {
                    if (current[0].getBlock().isPassable()) {

                        if (TeamName.equals("Blue")) {
                            current[0].getBlock().setType(Material.BLUE_WOOL);
                        } else if (TeamName.equals("Red")) {
                            current[0].getBlock().setType(Material.RED_WOOL);
                        } else if (TeamName.equals("Green")) {
                            current[0].getBlock().setType(Material.LIME_WOOL);
                        } else if (TeamName.equals("Yellow")) {
                            current[0].getBlock().setType(Material.YELLOW_WOOL);
                        } else {
                            current[0].getBlock().setType(Material.GRAY_WOOL);
                        }

                        current[0] = current[0].add(face.getDirection().normalize());

                        for (Entity e : block.getWorld().getNearbyEntities(current[0].clone().add(.5, .5, .5), .5, .5, .5)) {
                            if (e instanceof LivingEntity) e.setVelocity(face.getDirection());
                        }

                        count[0]++;
                        if (count[0] >=4){
                            this.cancel();
                        }
                    }else{
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin,1,1);
        }
    }

    public static void PopUpTower(Player p, Plugin plugin, BlockFace face, Block block) {
        if (Mana.spendMana(p, 4)) {
            String TeamName = "Red";
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
                                        if (TeamName.equals("Blue")){
                                            place.setType(Material.BLUE_CONCRETE_POWDER);
                                        } else if (TeamName.equals("Red")){
                                            place.setType(Material.RED_CONCRETE_POWDER);
                                        } else if (TeamName.equals("Green")){
                                            place.setType(Material.LIME_CONCRETE_POWDER);
                                        } else if (TeamName.equals("Yellow")){
                                            place.setType(Material.YELLOW_CONCRETE_POWDER);
                                        } else {
                                            place.setType(Material.GRAY_CONCRETE_POWDER);
                                        }
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

            String TeamName = "Red";

            if (TeamName.equals("Blue")){
                bolt.setItem(new ItemStack(Material.BLUE_CONCRETE));
            } else if (TeamName.equals("Red")){
                bolt.setItem(new ItemStack(Material.RED_CONCRETE));
            } else if (TeamName.equals("Green")){
                bolt.setItem(new ItemStack(Material.GREEN_CONCRETE));
            } else if (TeamName.equals("Yellow")){
                bolt.setItem(new ItemStack(Material.YELLOW_CONCRETE));
            } else {
                bolt.setItem(new ItemStack(Material.GRAY_CONCRETE));
            }

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
                        if (TeamName.equals("Blue")){
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.BLUE_CONCRETE);
                        } else if (TeamName.equals("Red")){
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.RED_CONCRETE);
                        } else if (TeamName.equals("Green")){
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.LIME_CONCRETE);
                        } else if (TeamName.equals("Yellow")){
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.YELLOW_CONCRETE);
                        } else {
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.GRAY_CONCRETE);
                        }

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

            CircleEffect effect = new CircleEffect(em);
            effect.enableRotation = false;
            effect.setLocation(pillar.getLocation());
            effect.radius = 7;
            effect.particle = Particle.REDSTONE;
            effect.particleCount = 1;
            effect.particleSize = 3;
            effect.color = Color.BLACK;
            effect.wholeCircle = true;
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

    public static void FireBolt(Player p,Plugin plugin){
        if (Mana.spendMana(p, 2)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.FIRE_CHARGE));
            bolt.setFireTicks(600);
            bolt.setGravity(false);
            ID.put(bolt, "FireBolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    bolt.getWorld().spawnParticle(Particle.FLAME, bolt.getLocation(), 1, .1, .1, .1,.1);
                }
            }, 1, 1));
        }
    }

    public static void SnowBolt(Player p,Plugin plugin){
        if (Mana.spendMana(p, 3)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setGravity(false);
            ID.put(bolt, "SnowBolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    bolt.getWorld().spawnParticle(Particle.SNOW_SHOVEL, bolt.getLocation(), 2, .1, .1, .1);
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
                    cloud.cancel();
                    Vector direction = v;
                    Location current = l;
                    p.getWorld().playSound(current, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
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
                                if (hitEntity != null) {
                                    hasHit = true;
                                    LivingEntity finalHitEntity = hitEntity;
                                    final int[] hits = {0};
                                    BukkitTask task = new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            hits[0]++;
                                            finalHitEntity.setNoDamageTicks(0);
                                            finalHitEntity.damage(1, p);
                                            if (hits[0]>=6) this.cancel();
                                            finalHitEntity.setVelocity(new Vector(0,0,0));
                                        }
                                    }.runTaskTimer(plugin,0,2);
                                }

                            }
                            if (!hasHit) {
                                current = current.add(direction);
                                Particle.DustOptions dust = new Particle.DustOptions(Color.YELLOW,2);
                                p.getWorld().spawnParticle(Particle.REDSTONE, current, 2, .1, .1, .1, 0,dust);
                                range++;
                                if (range >= 80) hasHit = true;
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
            }, 10);
        }
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
        Map<Player, Integer> playerMana = Mana.getPlayerMana();
        playerMana.replace(p, playerMana.get(p) + 3);
        p.damage(6);
        if (playerMana.get(p) > 10) {
            playerMana.replace(p, 10);
        }
        p.setLevel(playerMana.get(p));

    }

    public static void BloodTracker(Player p,Plugin plugin){
        if (Mana.spendMana(p, 3)) {
            Vex vex = (Vex) p.getWorld().spawnEntity(p.getEyeLocation(),EntityType.VEX);
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
//                                if (e != p) {
                                if (e.getLocation().distance(vex.getLocation()) <= 20) {
                                    if (Math.ceil(((Player) e).getMaxHealth()) > Math.ceil(((Player) e).getHealth())) {
                                        vex.setTarget((LivingEntity) e);
                                        vex.setCharging(true);
                                        locked[0] = (Player) e;
                                    }
//                                }
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
                            vex.getWorld().playSound(vex.getLocation(),Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP,1,1);
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


    public static void HealBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 2)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setItem(new ItemStack(Material.PINK_DYE));
            bolt.setGravity(false);

            String TeamName = "Red";

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
                                String VictimTeam = "Red";
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
                                        live.setHealth(live.getHealth()+1);
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

    public static void PurificationWave(Player p,Plugin plugin){
        String teamName = "Red";
        for (Entity e:p.getNearbyEntities(5,5,5)) {
            if (e instanceof Player){
                Player victim = (Player) e;
                String victimTeam = "Red";
                if (teamName.matches(victimTeam)) {
                    if (e.getLocation().distance(p.getLocation()) <= 4) {
                        victim.removePotionEffect(PotionEffectType.SLOW);
                        victim.removePotionEffect(PotionEffectType.WITHER);
                        victim.removePotionEffect(PotionEffectType.POISON);
                        victim.removePotionEffect(PotionEffectType.WEAKNESS);


                    }
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

    public static void ShadowRetreat(Player p,Plugin plugin){
        for (Entity e:p.getNearbyEntities(4,4,4)) {
            if (e instanceof LivingEntity){
                //TEAM CODE make it only effect enemies


                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60,1));
            }
        }
        p.getWorld().spawnParticle(Particle.SQUID_INK,p.getEyeLocation(),30,.1,.1,.1,.5);
        p.setVelocity(p.getEyeLocation().getDirection().multiply(-1).add(new Vector(0,.5,0)).normalize().multiply(1.5));
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

    public static void ShadowGrapple(Player p) {
        Vector direction = p.getEyeLocation().getDirection();
        RayTraceResult ray = p.getWorld().rayTrace(p.getEyeLocation().add(direction), direction, 5, FluidCollisionMode.NEVER, true, .1, null);
        int distance = 5;
        Particle.DustOptions dust = new Particle.DustOptions(Color.BLACK, 3);
        Entity hit = null;
        p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation().add(direction.multiply(3)), 25, 1, 1, 1, 0, dust);
        if (ray != null) {
            distance = (int) Math.ceil(ray.getHitPosition().distance(p.getEyeLocation().toVector()));
            try {
                hit = ray.getHitEntity();
            } catch (Exception ignore) {
            }
            if (hit != null) {
                if (hit instanceof LivingEntity) {
                    ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
                    stand.setSmall(true);
                    stand.setInvisible(true);
                    stand.setInvulnerable(true);
                    p.addPassenger(stand);
                    stand.addPassenger(hit);
                    Map<LivingEntity, Player> ShadowGrappler = StatusEffects.getShadowGrappler();
                    Map<LivingEntity, Integer> ShadowTimer = StatusEffects.getShadowGrappleTimer();
                    Map<Player, LivingEntity> ShadowGrappled = StatusEffects.getShadowGrappled();
                    ((LivingEntity) hit).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,120,1,true,true));
                    ((LivingEntity) hit).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,120,20,true,true));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,120,20,true,true));
                    ShadowGrappler.put((LivingEntity) hit,p);
                    ShadowGrappled.put(p,(LivingEntity) hit);
                    ShadowTimer.put((LivingEntity) hit, 0);
                    hit.setCustomNameVisible(false);

                }
            }
        }
    }

    //casting code for Air needdles

    public static void AirNeedles(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, 2)) {
            Arrow arrow = p.launchProjectile(Arrow.class);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(arrow, "Wind Arrow");
            arrow.setDamage(.35 );
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(arrow, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    arrow.getWorld().spawnParticle(Particle.SPELL, arrow.getLocation(), 1, .1, .1, .1, 0);
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {

                    Arrow arrow = p.launchProjectile(Arrow.class);
                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                    ID.put(arrow, "Wind Arrow");
                    arrow.setDamage(.35);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    tick.put(arrow, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                        @Override
                        public void run() {
                            arrow.getWorld().spawnParticle(Particle.SPELL, arrow.getLocation(), 1, .1, .1, .1, 0);
                        }
                    }, 1, 1));

                }
            }, 3);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Arrow arrow = p.launchProjectile(Arrow.class);
                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();
                    ID.put(arrow, "Wind Arrow");
                    arrow.setDamage(.35);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                    Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
                    tick.put(arrow, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                        @Override
                        public void run() {
                            arrow.getWorld().spawnParticle(Particle.SPELL, arrow.getLocation(), 1, .1, .1, .1, 0);
                        }
                    }, 1, 1));
                }
            }, 6);
        }
    }

    public static void CloudBurst(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Location l = p.getLocation();
            p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 10, .6, .6, .6, 0);
            final int[] count = {0};
            p.setVelocity(new Vector(0, 2, 0));
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    count[0]++;
                    p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, .2, .2, .2, 0);
                    if (count[0] >= 5) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }


    public static void AirDash(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Vector v = p.getEyeLocation().getDirection().multiply(1.7);
            p.setGravity(false);
            p.setVelocity(v);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    p.setVelocity(v);
                    p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, .1, .1, .1, 0);
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
                                        if (!(hitEntity instanceof ArmorStand)) {
                                            if (Math.abs(hitLocation.getY() - hitEntity.getEyeLocation().getY()) <= .4) {
                                                hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 2));
                                            }
                                            Map<LivingEntity, Integer> painted = StatusEffects.paintTimer;
                                            if (painted.containsKey(hitEntity)) {
                                                painted.replace(hitEntity, painted.get(hitEntity) + 80);
                                            } else {
                                                painted.put(hitEntity, 80);
                                            }
                                            hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 1));
                                        }
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

    public static void SpeedSong(Player p,Plugin plugin){

    }

    public static void StunSong(Player p, Plugin plugin){
        if (Mana.spendMana(p, 3)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setInvisible(true);
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            grenade.setItem(new ItemStack(Material.NOTE_BLOCK));
            grenade.setBounce(true);

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(stand, grenade);
            linked.put(grenade, stand);
            ID.put(stand, "SongGrenade");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            Map<Entity, BukkitTask> activate = ProjectileInfo.getActivateCode();
            final int[] count = {0};
            tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    stand.teleport(linked.get(stand));
                    count[0]++;
                    if (count[0] >=5){
                        stand.getWorld().spawnParticle(Particle.NOTE, stand.getLocation(), 0, .6, .2, .92,1, null);
                        count[0] = 0;
                    }
                }
            }, 1, 1));



        }
    }

    public static void AcidPotion(Player p){
        if (Mana.spendMana(p, 3)) {
            ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
            ItemStack item = (new ItemStack(Material.SPLASH_POTION));
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.fromRGB(0, 255, 0));
            item.setItemMeta(potionMeta);
            potion.setItem(item);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(potion, "Acid Potion");
        }
    }

    public static void HealPotion(Player p){
        if (Mana.spendMana(p, 4)) {
            ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
            ItemStack item = (new ItemStack(Material.SPLASH_POTION));
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.fromRGB(240, 40, 128));
            item.setItemMeta(potionMeta);
            potion.setItem(item);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(potion, "Heal Potion");
        }
    }

    public static void DebuffPotion(Player p){
        if (Mana.spendMana(p, 3)) {
            ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
            ItemStack item = (new ItemStack(Material.SPLASH_POTION));
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.fromRGB(30, 0, 0));
            item.setItemMeta(potionMeta);
            potion.setItem(item);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(potion, "Debuff Potion");
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
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    tick.get(bolt).cancel();
                    bolt.remove();
                }
            }, 100);
        }
    }

    public static void BlackHole(Player p, Plugin plugin, EffectManager em) {
        if (Mana.spendMana(p, 3)) {
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            Map<Entity, Effect> effectMap = ProjectileInfo.getVisualEffect();

            Snowball bolt = p.launchProjectile(Snowball.class,p.getEyeLocation().getDirection().multiply(.05));
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(.05));
            bolt.setItem(new ItemStack(Material.BLACK_DYE));
            bolt.setGravity(false);
            ID.put(bolt, "Black Hole");
            final Boolean[] active = {false};
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    bolt.setVelocity(velocity.get(bolt));
                    for (Entity entity : bolt.getNearbyEntities(12, 12, 12)) {
                        if (active[0]) {
                            if (entity instanceof Projectile) {
                                if (entity.getLocation().distance(bolt.getLocation()) <= 9) {
                                    Vector n = bolt.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(2 - (bolt.getLocation().distance(entity.getLocation())) / 10);
                                    double len = entity.getVelocity().length();
                                    Vector suck = entity.getVelocity().add(n).multiply(.5).normalize().multiply(len);

                                    entity.setVelocity(suck);
                                    if (velocity.containsKey(entity)){
                                        velocity.replace(entity,suck);
                                    }
                                }

                            } else if (entity instanceof Damageable) {
                                if (entity.getLocation().distance(bolt.getLocation()) <= 8) {
                                    Vector n = bolt.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.015 * (1 - (bolt.getLocation().distance(entity.getLocation())) / 10));
                                    entity.setVelocity(entity.getVelocity().add(n));
                                    if (entity.getLocation().distance(bolt.getLocation()) <= 3) {
                                        ((Damageable) entity).damage(3, bolt);
                                    }
                                }
                            }
                        } else{
                            bolt.getWorld().spawnParticle(Particle.SQUID_INK, bolt.getLocation(),10,.2,.2,.2,0);
                        }
                    }
                }
            }, 1, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            AtomEffect effect = new AtomEffect(em);
                            effect.setLocation(p.getEyeLocation().add(0, 0, 0));
                            effect.yaw = p.getEyeLocation().getYaw();
                            effect.pitch = p.getEyeLocation().getPitch();
                            effect.setEntity(bolt);
                            effect.particleNucleus = Particle.SQUID_INK;
                            effect.particlesNucleus = 30;
                            effect.radiusNucleus = (float) .3;
                            effect.particleOrbital = Particle.FLAME;
                            effect.orbitals = 3;
                            effect.duration = 15;
                            em.start(effect);
                            effectMap.put(bolt,effect);
                            active[0] = true;
                        }
                    },70);


            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!bolt.isDead()) {
                        bolt.remove();
                        tick.get(bolt).cancel();
                        effectMap.get(bolt).cancel();
                    }
                }
            }, 460);
        }
    }

    public static void EndWarp(Player p,Plugin plugin){
        if (Mana.spendMana(p, 3)) {
            EnderPearl bolt = p.launchProjectile(EnderPearl.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setGravity(false);
            ID.put(bolt, "Warp Bolt");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    bolt.setVelocity(velocity.get(bolt));
                    bolt.getWorld().spawnParticle(Particle.REVERSE_PORTAL, bolt.getLocation(), 2, 0, 0, 0, .1);
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
            meta.setPower(2);
            firework.setShooter(p);
            firework.setShotAtAngle(true);
            firework.setVelocity(p.getEyeLocation().getDirection());
            firework.setFireworkMeta(meta);

            return firework;
        }
        return null;
    }

    public static void ChildBomber(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 5)) {
            String TeamName = "Red";
            ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta meta = (LeatherArmorMeta) helm.getItemMeta();
            if (TeamName.equals("Blue")) {
                meta.setColor(Color.fromRGB(60, 68, 170));
            } else if (TeamName.equals("Red")) {
                meta.setColor(Color.fromRGB(176, 46, 38));
            } else if (TeamName.equals("Green")) {
                meta.setColor(Color.fromRGB(128, 199, 31));
            } else if (TeamName.equals("Yellow")) {
                meta.setColor(Color.fromRGB(254, 216, 61));
            } else {
                meta.setColor(Color.fromRGB(71, 79, 82));
            }

            helm.setItemMeta(meta);
            ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
            chest.setItemMeta(meta);
            ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
            legs.setItemMeta(meta);
            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
            boots.setItemMeta(meta);

            Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);

            ItemStack[] armor = {boots,legs,chest,new ItemStack(Material.TNT)};

            zombie.getEquipment().setArmorContents(armor);
            zombie.setBaby();
            zombie.setHealth(10);
            zombie.getEquipment().setHelmetDropChance(0);
            zombie.getEquipment().setChestplateDropChance(0);
            zombie.getEquipment().setLeggingsDropChance(0);
            zombie.getEquipment().setBootsDropChance(0);

            final int[] age = {0};

            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    zombie.getWorld().spawnParticle(Particle.SMOKE_NORMAL,zombie.getEyeLocation(),1,.1,.1,.1,.1);
                    if (age[0] >=140){
                        zombie.getWorld().createExplosion(zombie.getEyeLocation(),3);
                        this.cancel();
                        zombie.remove();
                    }
                    if (zombie.isDead()){
                        this.cancel();
                    }

                    age[0]++;
                }
            }.runTaskTimer(plugin,1,1);


        }
    }

    public static void ZombieSpawn(Player p) {
        if (Mana.spendMana(p, 5)) {
            String TeamName = "Red";
            ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta meta = (LeatherArmorMeta) helm.getItemMeta();
            if (TeamName.equals("Blue")) {
                meta.setColor(Color.fromRGB(60, 68, 170));
            } else if (TeamName.equals("Red")) {
                meta.setColor(Color.fromRGB(176, 46, 38));
            } else if (TeamName.equals("Green")) {
                meta.setColor(Color.fromRGB(128, 199, 31));
            } else if (TeamName.equals("Yellow")) {
                meta.setColor(Color.fromRGB(254, 216, 61));
            } else {
                meta.setColor(Color.fromRGB(71, 79, 82));
            }
            for (int i = 0; i < 3; i++) {

                helm.setItemMeta(meta);
                ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
                chest.setItemMeta(meta);
                ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
                legs.setItemMeta(meta);
                ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                boots.setItemMeta(meta);

                Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
                ItemStack[] armor = {boots,legs,chest,helm};

                zombie.getEquipment().setArmorContents(armor);
                zombie.setAdult();

                zombie.getEquipment().setHelmetDropChance(0);
                zombie.getEquipment().setChestplateDropChance(0);
                zombie.getEquipment().setLeggingsDropChance(0);
                zombie.getEquipment().setBootsDropChance(0);
            }
        }
    }

    public static void SlimeBomb(Player p, Plugin plugin){
        if (Mana.spendMana(p, 2)) {
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            grenade.setItem(new ItemStack(Material.SLIME_BALL));
            ID.put(grenade, "SlimeBomb");

        }
    }

}

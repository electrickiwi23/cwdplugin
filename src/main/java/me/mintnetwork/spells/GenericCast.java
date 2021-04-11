package me.mintnetwork.spells;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AtomEffect;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.BlockDecay;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;

public class GenericCast {


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

    public static Firework FireworkBolt(Player p) {
        if (Mana.spendMana(p, 3)) {
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

    public static void SpeedBlink(Player p) {
        if (Mana.spendMana(p, 3)) p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 60, true, false));
    }

    public static void JumpBoost(Player p) {
        if (Mana.spendMana(p, 1)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 2, true, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1, true, true));
        }
    }

    public static void EngineBurst(Player p) {
        if (Mana.spendMana(p, 3)) {

            Location center = p.getLocation().add(p.getEyeLocation().getDirection().multiply(2));
            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, center.add(0, 1, 0), 4, .4, .4, .4);

            for (Entity e : p.getWorld().getNearbyEntities(center, 3, 3, 3)) {

                if (e instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) e;
                    Vector v = center.toVector().subtract(e.getLocation().toVector()).multiply(center.distance(e.getLocation()) * -.3);
                    living.setVelocity(v.add(new Vector(0, .6, 0)));

                }
            }
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

    public static void BatSonar(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Bat bat = (Bat) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.BAT);
            bat.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
            bat.setGlowing(true);;
            Vector v = p.getEyeLocation().getDirection().multiply(.4);
            final Location[] set = {null};
            final int[] age = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    age[0]++;
                    if (age[0] == 100) {
                        set[0] = bat.getLocation();
                    }
                    if (age[0] < 100) {
                        bat.setVelocity(v);
                    } else {
                        if (set[0] != null) {
                            bat.teleport(set[0]);
                            bat.setVelocity(new Vector(0, 0, 0));
                        }
                    }

                    if (age[0] > 30) {
                        for (Entity e : bat.getWorld().getNearbyEntities(bat.getLocation(), 9, 9, 9)) {
                            if (e instanceof LivingEntity) {
                                if (!(e instanceof ArmorStand)) {
                                    ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1));
                                }
                            }

                        }
                    }
                    if (age[0] > 300) {
                        bat.remove();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }

    public static void TNTRing(Player p) {
        if (Mana.spendMana(p, 3)) {
            for (int i = 0; i < 8; i++) {
                TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.PRIMED_TNT);
                Location location = p.getLocation();
                location.setYaw(location.getYaw() + (45 * i));
                location.setPitch(0);
                tnt.setFuseTicks(120);
                tnt.setSource(p);
                tnt.setVelocity(location.getDirection().add(new Vector(0, 1, 0)).multiply(.22));
            }
        }
    }

    public static void BeeBolt(Player p, Plugin plugin) {
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

            Snowball bolt = p.launchProjectile(Snowball.class, p.getEyeLocation().getDirection().multiply(.05));
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
                                    if (velocity.containsKey(entity)) {
                                        velocity.replace(entity, suck);
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
                        } else {
                            bolt.getWorld().spawnParticle(Particle.SQUID_INK, bolt.getLocation(), 10, .2, .2, .2, 0);
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
                    effectMap.put(bolt, effect);
                    active[0] = true;
                }
            }, 70);


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

    public static void EndWarp(Player p, Plugin plugin) {
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

    public static void ChildBomber(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 5)) {
            String TeamName = TeamsInit.getTeamName(p);
            ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta meta = (LeatherArmorMeta) helm.getItemMeta();

            Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);

            if (TeamName.equals("blue")) {
                meta.setColor(Color.fromRGB(60, 68, 170));
            } else if (TeamName.equals("red")) {
                meta.setColor(Color.fromRGB(176, 46, 38));
            } else if (TeamName.equals("green")) {
                meta.setColor(Color.fromRGB(128, 199, 31));
            } else if (TeamName.equals("yellow")) {
                meta.setColor(Color.fromRGB(120, 120, 2));
            }

            TeamsInit.addToTeam(zombie, TeamName);

            helm.setItemMeta(meta);
            ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
            chest.setItemMeta(meta);
            ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
            legs.setItemMeta(meta);
            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
            boots.setItemMeta(meta);

            ItemStack[] armor = {boots, legs, chest, new ItemStack(Material.TNT)};

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
                    zombie.getWorld().spawnParticle(Particle.SMOKE_NORMAL, zombie.getEyeLocation(), 1, .1, .1, .1, .1);
                    if (age[0] >= 140) {
                        zombie.getWorld().createExplosion(zombie.getEyeLocation(), 3);
                        this.cancel();
                        zombie.remove();
                    }
                    if (zombie.isDead()) {
                        this.cancel();
                    }

                    age[0]++;
                }
            }.runTaskTimer(plugin, 1, 1);


        }
    }

    public static void ZombieSpawn(Player p) {
        if (Mana.spendMana(p, 5)) {
            String TeamName = TeamsInit.getTeamName(p);
            ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta meta = (LeatherArmorMeta) helm.getItemMeta();

            if (TeamName.equals("blue")) {
                meta.setColor(Color.fromRGB(60, 68, 170));
            } else if (TeamName.equals("red")) {
                meta.setColor(Color.fromRGB(176, 46, 38));
            } else if (TeamName.equals("green")) {
                meta.setColor(Color.fromRGB(128, 199, 31));
            } else if (TeamName.equals("yellow")) {
                meta.setColor(Color.fromRGB(120, 120, 2));
            }


            for (int i = 0; i < 3; i++) {

                Zombie zombie = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);

                TeamsInit.addToTeam(zombie, TeamName);


                helm.setItemMeta(meta);
                ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
                chest.setItemMeta(meta);
                ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
                legs.setItemMeta(meta);
                ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                boots.setItemMeta(meta);

                ItemStack[] armor = {boots, legs, chest, helm};

                zombie.getEquipment().setArmorContents(armor);
                zombie.setAdult();

                zombie.getEquipment().setHelmetDropChance(0);
                zombie.getEquipment().setChestplateDropChance(0);
                zombie.getEquipment().setLeggingsDropChance(0);
                zombie.getEquipment().setBootsDropChance(0);
            }
        }
    }

    public static void SlimeBomb(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 2)) {
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            grenade.setItem(new ItemStack(Material.SLIME_BALL));
            ID.put(grenade, "SlimeBomb");

        }
    }

    public static void VoltStep(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 2)) {
            Vector direction = p.getEyeLocation().getDirection();
            Location current = p.getLocation().add(direction).add(0,.3,0);
            ArrayList<LivingEntity> damage = new ArrayList<>();
            int range = 0;
            boolean hasHit = false;
            while (!hasHit) {
                if (current.isWorldLoaded()) {
                    RayTraceResult ray = p.getWorld().rayTraceBlocks(current, direction, 1, FluidCollisionMode.NEVER, true);
                    Location hitLocation = null;
                    if (ray != null) {
                        try {
                            hitLocation = ray.getHitPosition().toLocation(p.getWorld());
                        } catch (Exception ignore) {
                        }
                        if (hitLocation != null) {
                            hasHit = true;
                        }
                    }
                    if (!hasHit) {
                        current = current.add(direction);
                        Particle.DustOptions dust = new Particle.DustOptions(Color.YELLOW,2);
                        p.getWorld().spawnParticle(Particle.REDSTONE, current, 4, .2, .4, .2, 0,dust);
                        range++;
                        if (range >= 6) hasHit = true;
                        for (Entity e : current.getWorld().getNearbyEntities(current,1,2,1)) {
                            if (e instanceof LivingEntity){
                                if (!(e instanceof ArmorStand)){
                                    if (!(TeamsInit.getTeamName(p).equals(TeamsInit.getTeamName(e)))){
                                        damage.add((LivingEntity) e);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    hasHit = true;
                }
            }
            Vector v = p.getVelocity();
            p.teleport(current.subtract(direction));
            p.setVelocity(v);
            Particle.DustOptions dust = new Particle.DustOptions(Color.YELLOW,2);
            p.getWorld().spawnParticle(Particle.REDSTONE, current, 12, .2, .4, .2, 0,dust);
            final int[] hits = {0};
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    hits[0]++;
                    for (LivingEntity e:damage) {
                        e.setNoDamageTicks(0);
                        e.damage(1, p);
                        e.setVelocity(new Vector(0,0,0));
                    }
                    if (hits[0]>=3) this.cancel();

                }
            }.runTaskTimer(plugin,0,3);

        }
    }
}

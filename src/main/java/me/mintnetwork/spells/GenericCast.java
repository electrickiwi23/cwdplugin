package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import me.mintnetwork.Objects.BlackHole;
import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.Objects.Shield;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.repeaters.BlockDecay;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Map;

public class GenericCast {





    public static void FireworkBolt(Player p) {
        if (Mana.spendMana(p, 2)) {
            Firework firework = (Firework) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREWORK);
            FireworkEffect.Builder effect = FireworkEffect.builder();
            FireworkMeta meta = firework.getFireworkMeta();
            effect.with(FireworkEffect.Type.BURST);

            if (TeamsInit.getTeamName(p).equals("")) effect.withColor(Color.WHITE);
            switch (TeamsInit.getTeamName(p)){
                case ("red"):
                    effect.withColor(Color.RED);
                    break;
                case ("blue"):
                    effect.withColor(Color.BLUE);
                    break;
                case ("yellow"):
                    effect.withColor(Color.YELLOW);
                    break;
                case ("green"):
                    effect.withColor(Color.fromBGR(0, 255, 0));
                    break;
            }

            meta.addEffect(effect.build());
            meta.setPower(1);
            firework.setShooter(p);
            firework.setShotAtAngle(true);
            firework.setVelocity(p.getEyeLocation().getDirection());
            firework.setFireworkMeta(meta);
        }
    }

    public static void SpeedBlink(Player p) {
        if (Mana.spendMana(p, 3)) p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 60, true, false));
    }

    public static void JumpBoost(Player p) {
        if (Mana.spendMana(p, 1)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 3, true, true));
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
            bat.setGlowing(true);
            Vector v = p.getEyeLocation().getDirection().multiply(.4);
            final Location[] set = {null};
            final int[] age = {0};
            String TeamName = TeamsInit.getTeamName(p);
            TeamsInit.addToTeam(bat,TeamName);
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
                        for (Entity e : bat.getWorld().getNearbyEntities(bat.getLocation(), 13, 13, 13)) {
                            if (bat.getLocation().distance(e.getLocation())<=12) {
                                if (e instanceof LivingEntity) {
                                    if (!(e instanceof ArmorStand)) {
                                        if (!TeamsInit.getTeamName(e).matches(TeamName)) {
                                            ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1));
                                        }
                                    }
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
        if (Mana.spendMana(p, 4)) {
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

            Snowball bolt = p.launchProjectile(Snowball.class, p.getEyeLocation().getDirection());
            velocity.put(bolt, p.getEyeLocation().getDirection());
            bolt.setItem(new ItemStack(Material.BLACK_DYE));
            bolt.setGravity(false);
            ID.put(bolt, "Black Hole");
            final Boolean[] active = {false};
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    bolt.setVelocity(velocity.get(bolt));
                    bolt.getWorld().spawnParticle(Particle.SQUID_INK, bolt.getLocation(), 5, .05, .05, .05, 0);

                }
            }, 1, 1));

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!bolt.isDead()){
                        tick.get(bolt).cancel();
                        new BlackHole(bolt.getLocation(),em,plugin);
                        bolt.remove();
                    }
                }
            }, 12);


//            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
//                @Override
//                public void run() {
//                    if (!bolt.isDead()) {
//                        bolt.remove();
//                        tick.get(bolt).cancel();
//                    }
//                }
//            }, 460);
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
        if (Mana.spendMana(p, 4)) {
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
        if (Mana.spendMana(p, 4)) {
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
            Location current = p.getLocation().add(direction).add(0, .3, 0);

            p.getWorld().spawnParticle(Particle.FLASH,p.getLocation().add(0,1,0),1);

            RayTraceResult ray = p.getWorld().rayTraceBlocks(current, direction, 6, FluidCollisionMode.NEVER, true);
            Block hitBlock = null;
            current = current.add(direction.multiply(6));
            direction.multiply(1.0/6.0);
            if (ray != null) {
                try {
                    hitBlock = ray.getHitBlock();
                } catch (Exception ignore) {
                }
                if (hitBlock != null) {
                    current = ray.getHitPosition().toLocation(p.getWorld()).setDirection(direction);
                }
            }

            Vector v = p.getVelocity();
            p.teleport(current.subtract(direction));
            p.setVelocity(v);

        }
    }

    public static void BoostSlam(Player p, Plugin plugin){
        if (Mana.spendMana(p, 3)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20,3));
            p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
//            StatusEffects.UsingMove.add(p);

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
                    final Vector[] v = {p.getEyeLocation().getDirection().setY(0).normalize().multiply(1.3)};


                    final int[] count = {0};
                    final boolean[] hasHit = {false};
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            count[0]++;


                            System.out.println(Math.toDegrees(v[0].angle(p.getEyeLocation().getDirection())));

                            p.teleport(p.getLocation().setDirection(v[0]));
                            p.setVelocity(v[0]);
                            p.getWorld().spawnParticle(Particle.FLAME,p.getLocation().add(0,1,0),10, .2,.2,.2,.2,null,false);


                            for (Entity e : p.getNearbyEntities(2, 2, 2)) {
                                if (e instanceof LivingEntity) {
                                    if (!(e instanceof ArmorStand)) {
                                        if (p.getBoundingBox().expand(.2,.2,.2,.2,.2,.2).overlaps(e.getBoundingBox())) {
                                            e.setVelocity(v[0].multiply(1.3).add(new Vector(0,.4,0)).multiply(1-((LivingEntity) e).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getValue()));
                                            p.setVelocity(v[0].multiply(-.2).add(new Vector(0,.4,0)));

//                                            StatusEffects.UsingMove.remove(p);

                                            ((LivingEntity) e).damage(6);
                                            p.damage(2);
                                            p.getWorld().playSound(p.getBoundingBox().intersection(e.getBoundingBox()).getCenter().toLocation(p.getWorld()),Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) .2,1);
                                            hasHit[0] = true;
                                            this.cancel();
                                            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, p.getBoundingBox().intersection(e.getBoundingBox()).getCenter().toLocation(p.getWorld()), 1);
                                        }
                                    }
                                } else  if (e instanceof TNTPrimed){
                                    p.getWorld().playSound(p.getBoundingBox().intersection(e.getBoundingBox()).getCenter().toLocation(p.getWorld()),Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) .2,1);
                                    p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, p.getBoundingBox().intersection(e.getBoundingBox()).getCenter().toLocation(p.getWorld()), 1);

//                                    StatusEffects.UsingMove.remove(p);

                                    e.setVelocity(v[0].multiply(1).add(new Vector(0,.4,0)));
                                    p.setVelocity(v[0].multiply(-.2).add(new Vector(0,.4,0)));

                                    hasHit[0] = true;
                                    this.cancel();
                                }
                            }

                            RayTraceResult ray = p.getWorld().rayTraceBlocks(p.getLocation().add(0,.5,0), v[0], 1, FluidCollisionMode.NEVER, true);
                            Location hitLocation = null;
                            Block hitBlock = null;
                            if (ray != null) {
                                try {
                                    hitLocation = ray.getHitPosition().toLocation(p.getWorld());
                                } catch (Exception ignore) {
                                }
                                try {
                                    hitBlock = ray.getHitBlock();
                                } catch (Exception ignore) {
                                }
                                if (hitBlock != null) {
                                    this.cancel();
//                                    StatusEffects.UsingMove.remove(p);
                                    p.damage(2);
                                    p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, hitLocation, 1);
                                    p.setVelocity(v[0].multiply(-.2).add(new Vector(0,.4,0)));
                                    p.getWorld().playSound(hitLocation,Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) .2,1);
                                }
                            }

                            if (count[0] >= 7) {
                                this.cancel();
//                                StatusEffects.UsingMove.remove(p);
                                p.setVelocity(v[0].multiply(.3));
                            }

                        }
                    }.runTaskTimer(plugin, 1, 1);
                }
            },13);
        }
    }

    public static void AnvilLaunch(Player p, Plugin plugin){
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
        if (wizard.thrownAnvil==null) {
            if (Mana.spendMana(p, 3)) {
                Location start = p.getEyeLocation().add(0, 1, 0);

                FallingBlock anvil = p.getWorld().spawnFallingBlock(start, new MaterialData(Material.ANVIL));
                anvil.setHurtEntities(true);
                anvil.setDropItem(false);


                wizard.thrownAnvil = anvil;


                double angle = 70;

                start.setPitch((float) angle * -1);
                anvil.setVelocity(start.getDirection().normalize().multiply(2.5));

                final Location[] current = {start};
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        current[0] = anvil.getLocation();
                        if (anvil.isDead()){
                            Location hit = current[0];
                            hit = hit.add(0,-1,0);
                            final int[] count = {0};
                            Location finalHit = hit;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    count[0]++;
                                    for (int j = count[0] * -1; j < count[0] + 1; j++) {
                                        for (int k = count[0] * -1; k < count[0] + 1; k++) {
                                            if (!(Math.abs(j) == 3 &&(Math.abs(k) == 3))) {
                                                if ((Math.abs(j) == count[0] || (Math.abs(k) ==  count[0]))) {
                                                    Location block = new Location(finalHit.getWorld(), finalHit.getX() + j, finalHit.getY(), finalHit.getZ() + k);
                                                    boolean surface;
                                                    surface = (!block.getBlock().isPassable() && block.clone().add(0, 1, 0).getBlock().isPassable());
                                                    if (!surface) {
                                                        for (int i = 0; i < 5; i++) {
                                                            if (!surface) {
                                                                if (block.getBlock().isPassable()) {
                                                                    block.add(0, -1, 0);
                                                                } else {
                                                                    surface = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (!surface) {
                                                        block = new Location(finalHit.getWorld(), finalHit.getX() + j, finalHit.getY(), finalHit.getZ() + k);
                                                        for (int i = 0; i < 5; i++) {
                                                            if (!surface) {
                                                                if (!(!block.getBlock().isPassable() && block.clone().add(0, 1, 0).getBlock().isPassable())) {
                                                                    if (!(block.getBlock().isPassable() && !block.clone().add(0, 1, 0).getBlock().isPassable())) {
                                                                        block.add(0, 1, 0);
                                                                    }
                                                                } else {
                                                                    surface = true;
                                                                }

                                                            }
                                                        }
                                                    }
                                                    if (surface) {
                                                        if (block.getBlock().getType().getBlastResistance() < 500) {
                                                            BlockData data = block.getBlock().getBlockData();
                                                            block.getBlock().setType(Material.AIR);
                                                            FallingBlock falling = block.getWorld().spawnFallingBlock(block.getBlock().getLocation().add(.5, .7, .5), data);
                                                            falling.setVelocity(new Vector(0, .3, 0));

                                                            for (Entity e : block.getWorld().getNearbyEntities(block.getBlock().getLocation().add(.5, 1.5, .5), .6, .5, .6)) {
                                                                if (e instanceof LivingEntity) {
                                                                    e.setVelocity(new Vector(0, 1.3, 0));
                                                                    ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (count[0]>=3)this.cancel();
                                }
                            }.runTaskTimer(plugin,2,2);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin,1,1);
            }
        } else {
            FallingBlock anvil = wizard.thrownAnvil;
            anvil.setVelocity(new Vector(0, -2, 0));
            wizard.thrownAnvil = null;

        }
    }

    public static void StormStrike(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            final boolean[] hasHit = {false};
            final Location[] current = {p.getEyeLocation().add(p.getEyeLocation().getDirection())};
            final Vector[] direction = {p.getEyeLocation().getDirection()};
            final int[] range = {0};
            new BukkitRunnable(){
                @Override
                public void run(){
                    for (int i = 0; i < 5; i++) {
                        if (!hasHit[0]) {
                            if (current[0].isWorldLoaded()) {
                                RayTraceResult ray = p.getWorld().rayTrace(current[0], direction[0], 1, FluidCollisionMode.NEVER, true, .1, null);
                                Location hitLocation = null;
                                if (ray != null) {
                                    try {
                                        hitLocation = ray.getHitPosition().toLocation(p.getWorld());
                                    } catch (Exception ignore) {
                                    }
                                    if (hitLocation != null) {
                                        hasHit[0] = true;

                                        final int[] count = {0};

                                        Location finalHitLocation = hitLocation;
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                count[0]++;
                                                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(50, 50, 255), 1);
                                                p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, current[0], 40, (20-count[0])/14.0, (20-count[0])/14.0, (20-count[0])/14.0, 0);

                                                if (count[0]>=20){
                                                    current[0].getWorld().strikeLightning(finalHitLocation);
                                                    this.cancel();
                                                }
                                            }
                                        }.runTaskTimer(plugin,1,1);
                                        this.cancel();
                                    }
                                }
                                if (!hasHit[0]) {
                                    current[0] = current[0].add(direction[0]);
                                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(50, 50, 255), 2);
                                    p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, current[0], 2, 0, 0, 0, 0);
                                    range[0]++;
                                    if (range[0] >= 50) this.cancel();
                                    Map<Entity, String> ID = ProjectileInfo.getProjectileID();

                                    //code for shield reflection--------------------------------------------------------
                                    for (Entity shield : Shield.shieldMap.keySet()) {
                                        if (shield.getLocation().distance(current[0])<Shield.shieldMap.get(shield).getRadius()+.5){
                                            direction[0] = Shield.shieldMap.get(shield).reflectVector(current[0],direction[0]);
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
            }.runTaskTimer(plugin,1,1);

        }
    }
    public static void ManaBullet(Player p, Plugin plugin){
        if (Mana.spendMana(p, 1)) {
            final boolean[] hasHit = {false};
            final Location[] current = {p.getEyeLocation().add(p.getEyeLocation().getDirection())};
            final Vector[] direction = {p.getEyeLocation().getDirection()};
            final int[] range = {0};
            new BukkitRunnable(){
                @Override
                public void run(){
                    for (int i = 0; i < 4; i++) {
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
                                        if (BlockDecay.decay.containsKey(ray.getHitBlock())){
                                            DecayBlock block = BlockDecay.decay.get(ray.getHitBlock());
                                            block.damage(30);
                                            block.setForceful(true);
                                        }
                                    }
                                    if (hitEntity != null && (!(hitEntity == p && range[0] <= 2))) {
                                        hasHit[0] = true;

                                        ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(),EntityType.ARMOR_STAND);
                                        stand.setInvisible(true);
                                        stand.setMarker(true);
                                        stand.setCustomNameVisible(false);
                                        stand.setCustomName(p.getDisplayName() + "'s Mana Bullet");
                                        TeamsInit.addToTeam(stand,TeamsInit.getTeamName(p));

                                        hitEntity.damage(2, stand);

                                        stand.remove();
                                    }
                                }
                                if (!hasHit[0]) {
                                    current[0] = current[0].add(direction[0]);
                                    p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, current[0], 2, 0, 0, 0, 0);
                                    range[0]++;
                                    if (range[0] >= 50) this.cancel();

                                    //code for shield reflection--------------------------------------------------------
                                    for (Entity shield : Shield.shieldMap.keySet()) {
                                        if (shield.getLocation().distance(current[0])<Shield.shieldMap.get(shield).getRadius()+.5){
                                            direction[0] = Shield.shieldMap.get(shield).reflectVector(current[0],direction[0]);
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
            }.runTaskTimer(plugin,1,1);

        }
    }
}

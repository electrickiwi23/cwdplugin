package me.mintnetwork.listeners;

import de.slikey.effectlib.Effect;
import me.mintnetwork.Main;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.util.*;
import java.util.function.Predicate;

public class ProjectileHitListener implements Listener {

    private final Main plugin;

    public ProjectileHitListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile e = event.getEntity();
        LivingEntity hit = null;
        BlockFace hitFace = null;
        Block hitBlock = null;
        if (event.getHitEntity()!=null) {
            if (event.getHitEntity() instanceof LivingEntity) {
                hit = (LivingEntity) event.getHitEntity();
            }
        }
        if (event.getHitBlockFace()!=null){
            hitFace = event.getHitBlockFace();
        }
        if (event.getHitBlock()!=null){
            hitBlock = event.getHitBlock();
        }
        if (event.getHitBlock()!=null){
            hitBlock = event.getHitBlock();
        }
        LivingEntity shooter = (LivingEntity) e.getShooter();
        Map<Entity,String> ID = ProjectileInfo.getProjectileID();
        Map<Entity, BukkitTask> task = ProjectileInfo.getTickCode();
        Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
        Map<Entity, List<Double>> offset = ProjectileInfo.getStickyOffset();
        Map<Entity, Effect> effectMap = ProjectileInfo.getVisualEffect();
        if ((e instanceof Arrow)) {
            if (ID.containsKey(e)) {
                 if (ID.get(e).equals("Wind Arrow")) {
                     if (hit != null) {
                         LivingEntity finalHit = hit;
                         Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
                             @Override
                             public void run() {
                                 finalHit.setCustomName(String.valueOf(finalHit.getHealth()));
                                 finalHit.setNoDamageTicks(0);
                             }
                         });
                     }
                     task.get(e).cancel();
                 }
                if (ID.get(e).equals("Tracker Arrow")) {
                    if (hit != null) {
                        Map<Player, Entity> tracked = ProjectileInfo.getStrikeTrackedEntity();
                        for (Player p :tracked.keySet()) {
                            if (tracked.get(p)==e){
                                tracked.replace(p, hit);
                            }
                        }

                    }
                    }
            }

        }
        if (e instanceof ThrownPotion){
            ThrownPotion potion = (ThrownPotion) e;
            if (ID.containsKey(potion)) {
                if (ID.get(potion).equals("Acid Potion")) {
                    AreaEffectCloud cloud = (AreaEffectCloud) potion.getWorld().spawnEntity(potion.getLocation(), EntityType.AREA_EFFECT_CLOUD);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 255, 0), 2);
                    cloud.setParticle(Particle.REDSTONE, dust);
                    cloud.setRadius(4);
                    cloud.setRadiusPerTick((float) -.01);
                    cloud.setDuration(200);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 1, 1, true, true), false);
                    cloud.setReapplicationDelay(10);
                    cloud.setSource(shooter);
                    cloud.setCustomName("Acid Pool");
                }
                if (ID.get(potion).equals("Heal Potion")) {
                    AreaEffectCloud cloud = (AreaEffectCloud) potion.getWorld().spawnEntity(potion.getLocation(), EntityType.AREA_EFFECT_CLOUD);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(240,40,128), 2);
                    cloud.setParticle(Particle.REDSTONE, dust);
                    cloud.setRadius(4);
                    cloud.setRadiusPerTick((float) -.005);
                    cloud.setDuration(150);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 3, false, false), false);
                    cloud.setReapplicationDelay(20);
                    cloud.setSource(shooter);
                }
                if (ID.get(potion).equals("Debuff Potion")) {
                    AreaEffectCloud cloud = (AreaEffectCloud) potion.getWorld().spawnEntity(potion.getLocation(), EntityType.AREA_EFFECT_CLOUD);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(30,0,0), 2);
                    cloud.setParticle(Particle.REDSTONE, dust);
                    cloud.setRadius((float) 4.5);
                    cloud.setRadiusPerTick((float) -.01);
                    cloud.setDuration(250);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 50, 2, false, true), false);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 50, 1, false, true), false);
                    cloud.setReapplicationDelay(5);
                    cloud.setSource(shooter);
                }
                if (ID.get(potion).equals("Immortal Potion")) {
                    AreaEffectCloud cloud = (AreaEffectCloud) potion.getWorld().spawnEntity(potion.getLocation(), EntityType.AREA_EFFECT_CLOUD);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255,215,0), 2);
                    cloud.setParticle(Particle.REDSTONE, dust);
                    cloud.setRadius(5);
                    cloud.setDuration(200);
                    cloud.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 10, false, false), false);
                    cloud.setReapplicationDelay(5);
                    cloud.setSource(shooter);
                }
            }
        }

        if (e instanceof EnderPearl){
            if (ID.containsKey(e)){
                if (ID.get(e).equals("Warp Bolt")) {
                    task.get(e).cancel();
                }
            }
        }

        if (e instanceof Snowball) {
            Snowball snow = (Snowball) e;
            if (ID.containsKey(snow)) {
                if (ID.get(snow).equals("FireBolt")) {
                    if (hit != null) {
                        hit.damage(2,snow);
                        hit.setFireTicks(60);
                    }
                    if (hitBlock != null) {
                        hitBlock.getLocation().add(hitFace.getDirection()).getBlock().setType(Material.FIRE);
                    }
                    task.get(snow).cancel();
                }

                if (ID.get(snow).equals("Grab Hook")) {
                    if (hit != null) {
                        hit.setFireTicks(100);
                    }
                }
                if (ID.get(snow).equals("Black Hole")){
                    snow.remove();
                    task.get(snow).cancel();
                    effectMap.get(snow).cancel();
                }

                if (ID.get(snow).equals("BloodBolt")) {
                    if (hit != null) {
                        hit.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 3));
                        shooter.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 3));
                    }
                }
                if (ID.get(snow).equals("BloodUlt")) {
                    if (hit != null) {
                        LivingEntity finalHit = hit;
                        BukkitTask bloodUltTask = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (finalHit.isDead()) {
                                    Firework firework = (Firework) finalHit.getWorld().spawnEntity(finalHit.getEyeLocation(), EntityType.FIREWORK);
                                    FireworkEffect.Builder effect = FireworkEffect.builder();
                                    FireworkMeta meta = firework.getFireworkMeta();
                                    effect.with(FireworkEffect.Type.BURST);
                                    effect.withColor(Color.RED);
                                    meta.addEffect(effect.build());
                                    meta.setPower(0);
                                    firework.setShooter(shooter);
                                    firework.setFireworkMeta(meta);
                                    firework.detonate();
                                    this.cancel();
                                } else {
                                    finalHit.setNoDamageTicks(0);
                                    finalHit.damage(2, shooter);
                                    if (shooter.getMaxHealth() - 2 >= Math.ceil(shooter.getHealth())) {
                                        shooter.setHealth(shooter.getHealth() + 2);
                                    }
                                    finalHit.setVelocity(new Vector(0, 0, 0));
                                }
                            }
                        }.runTaskTimer(plugin,0,1);
                    }
                    task.get(snow).cancel();
                }
                if (ID.get(snow).equals("HealBolt")) {
                    if (hit != null) {
                        hit.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
                    }
                    task.get(snow).cancel();
                }

                if (ID.get(snow).equals("BuildBolt")) {
                    task.get(snow).cancel();
                }

                if (ID.get(snow).equals("Element Blast")) {
                    snow.getWorld().createExplosion(snow.getLocation(),6,true);
                    task.get(snow).cancel();
                }

                if (ID.get(snow).equals("Grapple Bolt")){
                    task.get(snow).cancel();
                    LivingEntity stand = (LivingEntity) linked.get(snow);
                    BukkitTask pull = new BukkitRunnable(){
                        @Override
                        public void run() {
                            shooter.setVelocity(shooter.getVelocity().add(shooter.getLocation().toVector().subtract(stand.getEyeLocation().toVector()).normalize().multiply(-.15 )));
                            if (((Player)shooter).isSneaking()){
                                stand.remove();
                                this.cancel();
                            }
                            if (((Player)shooter).isDead()){
                                stand.remove();
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin,1,1);
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            stand.remove();
                            pull.cancel();
                        }
                    },100);
                }

                if (ID.get(snow).equals("BeeBolt")) {
                    for (int i = 0; i < 3; i++) {
                        Bee bee = (Bee) snow.getWorld().spawnEntity(snow.getLocation(),EntityType.BEE);
                        bee.setHealth(2);
                        bee.setAnger(4000);


                        task.get(snow).cancel();
                        BukkitTask agro = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Player nearest = null;
                                double distance = 10;
                                for (Entity entity : bee.getNearbyEntities(10, 10, 10)) {
                                    if (entity instanceof Player) {
                                        if (bee.getLocation().distance(entity.getLocation()) < distance) {
                                            nearest = (Player) entity;
                                            distance = bee.getLocation().distance(entity.getLocation());
                                        }
                                    }
                                }
                                bee.setTarget(nearest);
                            }
                        },40,20);
                        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                bee.remove();
                                agro.cancel();
                            }
                        },600);
                    }

                }

                if (ID.get(snow).equals("SnowBolt")) {
                    task.get(snow).cancel();
                    snow.getWorld().spawnParticle(Particle.SNOWBALL, snow.getLocation(), 20, 0, 0, 0, 100);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.WHITE, 1);
                    snow.getWorld().spawnParticle(Particle.REDSTONE, snow.getLocation(), 30, 1.5, 1.5, 1.5, dust);
                    for (Entity entity : snow.getNearbyEntities(4, 4, 4)) {
                        if (entity instanceof LivingEntity) {
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
                        }
                    }
                    for (int j = 0; j < 9; j++) {
                        for (int k = 0; k < 9; k++) {
                            if (!(Math.abs(j - 3) == 3 && Math.abs(k - 4) == 3)) {
                                for (int l = 0; l < 9; l++) {
                                    Location block = new Location(snow.getWorld(), snow.getLocation().getX() + j - 3, snow.getLocation().getY() + k - 3, snow.getLocation().getZ() + l - 3);
                                    if (block.getBlock().getType().isAir()) {
                                        if (block.add(0, -1, 0).getBlock().getType().isSolid()) {
                                            block.add(0, 1, 0).getBlock().setType(Material.SNOW);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (ID.get(snow).equals("StickyTNT")) {
                    if (hit != null) {
                        Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Entity hit = event.getHitEntity();
                                Entity stand = linked.get(snow);
                                linked.replace(stand,hit);
                                List<Double> sticky = new ArrayList<Double>();
                                sticky.add(0,snow.getLocation().getX()-hit.getLocation().getX());
                                sticky.add(1,snow.getLocation().getY()-hit.getLocation().getY());
                                sticky.add(2,snow.getLocation().getZ()-hit.getLocation().getZ());
                                offset.put(stand,sticky);
                            }
                        });
                    }else if(hitFace != null) {
                        Entity stand = linked.get(snow);
//                        stand.teleport(snow.getLocation().add(event.getHitBlockFace().getDirection().normalize().multiply(.2)));
                        stand.teleport(snow);
                        linked.replace(stand,null);

                    }
                }

                if (ID.get(snow).equals("PaintBomb")){
                    Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 3);
                    snow.getWorld().spawnParticle(Particle.REDSTONE, snow.getLocation(), 20, 2, 2, 2, 0, dust);
                    dust = new Particle.DustOptions(Color.ORANGE, 3);
                    snow.getWorld().spawnParticle(Particle.REDSTONE, snow.getLocation(), 20, 2, 2, 2, 0, dust);
                    dust = new Particle.DustOptions(Color.YELLOW, 3);
                    snow.getWorld().spawnParticle(Particle.REDSTONE, snow.getLocation(), 20, 2, 2, 2, 0, dust);
                    dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 3);
                    snow.getWorld().spawnParticle(Particle.REDSTONE, snow.getLocation(), 20, 2, 2, 2, 0, dust);
                    dust = new Particle.DustOptions(Color.BLUE, 3);
                    snow.getWorld().spawnParticle(Particle.REDSTONE, snow.getLocation(), 20, 2, 2, 2, 0, dust);
                    dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 3);
                    snow.getWorld().spawnParticle(Particle.REDSTONE, snow.getLocation(), 20, 2, 2, 2, 0, dust);
                    for (Entity entity :snow.getWorld().getNearbyEntities(snow.getLocation(),6,6,6)) {
                        if (entity instanceof LivingEntity){
                            if (!(entity instanceof ArmorStand)) {
                                LivingEntity live = (LivingEntity) entity;
                                if (entity.getLocation().distance(snow.getLocation()) <= 5) {
                                    live.damage(3, snow);
                                    live.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 2));
                                    live.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 160, 1));
                                    Map<LivingEntity, Integer> painted = StatusEffects.getPaintTimer();
                                    if (painted.containsKey(live)) {
                                        painted.replace(live, painted.get(live) + 300);
                                    } else {
                                        painted.put(live, 300);
                                    }
                                }
                            }
                        }
                    }
                    snow.getWorld().playSound(snow.getLocation(), Sound.ENTITY_CAT_HISS, 2, 1);
                    snow.getWorld().playSound(snow.getLocation(), Sound.ENTITY_CAT_HISS, 2, 1);
                    task.get(snow).cancel();
                }

                if (ID.get(snow).equals("Molotov")){
                    for (int i = 0; i < 4; i++) {
                        int finalI = i;
                        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                for (int j = 0; j < 2* finalI +1; j++) {
                                    for (int k = 0; k < 2* finalI +1; k++) {
                                        if(!(Math.abs(j-finalI)==3&&Math.abs(k-finalI)==3)) {
                                            for (int l = 0; l < 2 * finalI + 1; l++) {
                                                Location block = new Location(snow.getWorld(), snow.getLocation().getX() + j - finalI, snow.getLocation().getY() + k - finalI, snow.getLocation().getZ() + l - finalI);
                                                if (block.getBlock().getType().isAir()) {
                                                    block.getBlock().setType(Material.FIRE);
                                                }
                                            }
                                        }

                                    }

                                }

                            }
                        },i*2);

                    }
                    snow.remove();
                    Entity stand = linked.get(snow);
                    task.get(stand).cancel();
                    stand.remove();

                }

            }

            //code for projectiles that bounce when they hit walls such as tnt grenade
            if (e.doesBounce()) {
                Vector v = null;
                Vector n = null;
                Vector d = e.getVelocity();
                if (event.getHitBlockFace()!=null) {
                    n = event.getHitBlockFace().getDirection().normalize();
                    v = d.subtract(n.multiply(d.dot(n) * 2));
                } else if (event.getHitEntity()!=null){
                    n = e.getLocation().toVector().subtract(event.getHitEntity().getLocation().toVector()).normalize();
                    v = d.subtract(n.multiply(d.dot(n) * 2));
                }
                if (v != null) {
                    Snowball bounce = (Snowball) e.getWorld().spawnEntity(e.getLocation().add(n.multiply(-.4)), EntityType.SNOWBALL);
                    bounce.setVelocity(v.multiply(.3));
                    Entity stand = linked.get(e);
                    if (ID.get(stand).equals("TNTGrenade")) {
                        bounce.setItem(new ItemStack(Material.TNT));
                        bounce.setBounce(true);
                        linked.put(bounce, stand);
                        linked.replace(stand, bounce);

                    }
                    if (ID.get(stand).equals("DragonGrenade")) {
                        bounce.setItem(new ItemStack(Material.CRYING_OBSIDIAN));
                        bounce.setBounce(true);
                        linked.put(bounce, stand);
                        linked.replace(stand, bounce);

                    }
                }


            }
        }
    }
}

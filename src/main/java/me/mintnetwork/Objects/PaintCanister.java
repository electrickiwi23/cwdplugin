package me.mintnetwork.Objects;

import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;

public class PaintCanister {

    public static ArrayList<PaintCanister> canisterArrayList = new ArrayList<>();

    Location location;
    Rabbit entity;
    Player owner;
    BukkitTask task;
    BlockFace face;
    int age;
    int timer = 0;

    public PaintCanister(Location location, Player player, Plugin plugin,BlockFace blockFace,int projectileAge){
        this.location = location.add(blockFace.getDirection().multiply(.2));
        owner = player;
        face = blockFace;
        age = projectileAge;

        entity = (Rabbit) location.getWorld().spawnEntity(this.location.clone().add(0,-.25,0), EntityType.RABBIT);
        entity.setCustomNameVisible(false);
        entity.setCustomName(player.getDisplayName() + "'s Paint Canister");
        entity.setInvisible(true);
        entity.setAI(false);
        entity.setSilent(true);
        entity.setGravity(false);
        entity.setHealth(1);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                age++;
                if (age==600) explode(plugin);
                if (entity.isDead()) remove();

                if (age>=60||age%2==0) {

                    Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 1);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.ORANGE, 1);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.YELLOW, 1);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 1);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.BLUE, 1);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 1);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);
                }

            }
        }.runTaskTimer(plugin, 1, 1);

        canisterArrayList.add(this);
    }

    public int getAge(){
        return age;
    }

    public Player getOwner(){
        return owner;
    }

    public void explode(Plugin plugin){
        Location explodeLoc = location.add(face.getDirection());
        entity.teleport(entity.getLocation().add(face.getDirection()));
        new BukkitRunnable(){
            @Override
            public void run(){
                timer++;
                if (timer>=20) {
                    if (!entity.isDead()) {

                        Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 3);
                        explodeLoc.getWorld().spawnParticle(Particle.REDSTONE, explodeLoc, 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.ORANGE, 3);
                        explodeLoc.getWorld().spawnParticle(Particle.REDSTONE, explodeLoc, 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.YELLOW, 3);
                        explodeLoc.getWorld().spawnParticle(Particle.REDSTONE, explodeLoc, 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 3);
                        explodeLoc.getWorld().spawnParticle(Particle.REDSTONE, explodeLoc, 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.BLUE, 3);
                        explodeLoc.getWorld().spawnParticle(Particle.REDSTONE, explodeLoc, 20, 2, 2, 2, 0, dust);
                        dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 3);
                        explodeLoc.getWorld().spawnParticle(Particle.REDSTONE, explodeLoc, 20, 2, 2, 2, 0, dust);
                        for (Entity entity : explodeLoc.getWorld().getNearbyEntities(explodeLoc, 6, 6, 6)) {
                            if (entity instanceof LivingEntity) {
                                if (!(entity instanceof ArmorStand)&&!(entity instanceof Rabbit)) {

                                    RayTraceResult ray = explodeLoc.getWorld().rayTraceBlocks(explodeLoc, entity.getLocation().add(0, 1, 0).toVector().subtract(explodeLoc.toVector()), 5);

                                    if (ray==null||ray.getHitPosition().toLocation(explodeLoc.getWorld()).distance(explodeLoc)>entity.getLocation().distance(explodeLoc)) {
                                        LivingEntity live = (LivingEntity) entity;
                                        if (entity.getLocation().distance(explodeLoc) <= 5) {
                                            live.damage(3, entity);
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
                        }
                        explodeLoc.getWorld().playSound(explodeLoc, Sound.ENTITY_CAT_HISS, (float) .8, 1);
                        explodeLoc.getWorld().playSound(explodeLoc, Sound.ENTITY_CAT_HISS, (float) .8, 1);
                    }
                    remove();
                    this.cancel();
                } else {
                    location.getWorld().playSound(explodeLoc, Sound.ENTITY_CAT_HISS, (float) .1, 1);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.RED, 2);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.ORANGE, 2);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.YELLOW, 2);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.fromBGR(0, 255, 0), 2);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.BLUE, 2);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);

                    dust = new Particle.DustOptions(Color.fromBGR(255, 0, 255), 2);
                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, .1, .1, .1, 0, dust);
                }


            }
        }.runTaskTimer(plugin,1,1);

        canisterArrayList.remove(this);
    }

    public void remove(){
        entity.remove();
        task.cancel();
        if (canisterArrayList.contains(this)) canisterArrayList.remove(this);
    }


}

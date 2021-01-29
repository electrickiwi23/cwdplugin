//package me.mintnetwork.spells.projectiles;
//
//import org.bukkit.Color;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Particle;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.LivingEntity;
//
//import com.aim.coltonjgriswold.api.ParticleProjectile;
//
//public class BloodBolt extends ParticleProjectile {
//
//
//    public BloodBolt() {
//        super(Particle.REDSTONE, 0.3, 200, 128.0);
//        setColor(Color.fromRGB(255, 0, 0));
//        ignoreMaterial(Material.GRASS);
//
//        //Initialize a projectile with a color of red, a hitbox with size 0.1 cubed, Mass of 7.5 grams, speed of 20.0 meters/second, max distance of 128 meters
//    }
//
//    public void OnLaunch(LivingEntity who, Location start) {
//        who.teleport(who.getLocation().add(0, 10,0));
//    }
//
//    public void OnHit(LivingEntity who, Location end) {
//        //Do something when reach max distance
//    }
//
//    public void OnHitBlock(LivingEntity who, Location end, Block what) {
//        //Do something when hit block
//    }
//
//    public void OnHitEntity(LivingEntity who, Location end, Entity what) {
//        if (what instanceof LivingEntity){
//            System.out.println("hit entity: " + what.getType());
//            ((LivingEntity) what).damage(3,who);
//            who.setHealth(who.getHealth()+3);
//        }
//    }
//
//    public void OnPenetrateBlock(LivingEntity who, Location where, Block what) {
//        //Do something when passing through a block
//    }
//
//    public void OnPenetrateEntity(LivingEntity who, Location where, Entity what) {
//        //Do something when passing through an entity
//    }
//
//    public void OnMove(Location previous, Location current, double step) {
//        //Do something when the the particle moves
//    }
//}
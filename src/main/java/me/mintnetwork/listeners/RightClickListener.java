package me.mintnetwork.listeners;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CloudEffect;
import de.slikey.effectlib.effect.DiscoBallEffect;
import de.slikey.effectlib.effect.DragonEffect;
import me.mintnetwork.Main;
import me.mintnetwork.spells.Cast;
import org.bukkit.*;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import me.mintnetwork.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

public class RightClickListener implements Listener {

    private final Main plugin;

    public RightClickListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    //    ParticleProjectile bolt = new BloodBolt();
    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getPlayer() != null) {
            Player p = event.getPlayer();
            EffectManager em = new EffectManager(plugin);
            if (lastUsed.containsKey(p.getUniqueId())) {
                if (System.currentTimeMillis() - lastUsed.get(p.getUniqueId()) < 380) {
                    return;
                }
            }
            lastUsed.put(p.getUniqueId(), System.currentTimeMillis());
            if (!p.getInventory().getItemInMainHand().equals(new ItemStack(Material.AIR, 0))) {
                if (event.getHand() != null) {
                    if (event.getHand().equals(EquipmentSlot.HAND)) {
                        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Blood Bolt")) {
                                    Cast.ShadowInvis(p,plugin);
//                        ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
//                        stand.addScoreboardTag("BlackHole");
//                        stand.setVisible(false);
//                        stand.setMarker(true);
//                        stand.setVelocity(p.getEyeLocation().getDirection());
//                        AtomEffect effect = new AtomEffect(em);
//                        effect.setLocation(p.getLocation().add(0, 0, 0));
//                        effect.setEntity(stand);
//                        effect.particleNucleus = Particle.SQUID_INK;
//                        effect.particlesNucleus = 30;
//                        effect.radiusNucleus = (float).3;
//                        effect.particleOrbital = Particle.FLAME;
//                        effect.orbitals = 2;
//                        effect.duration = 15;
//                        em.start(effect);
//                        BukkitTask reflection = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
//                            @Override
//                            public void run() {
//                                for (Entity entity : stand.getNearbyEntities(12, 12, 12)) {
//                                    if (entity instanceof Projectile) {
//                                        if (entity.getLocation().distance(stand.getLocation()) <= 12) {
//                                            Vector n = stand.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(2-(stand.getLocation().distance(entity.getLocation()))/12);
//                                            entity.setVelocity(entity.getVelocity().multiply(.9));
//                                            entity.setVelocity(entity.getVelocity().add(n));
//                                        }
//
//                                    }
//                                    else if (entity instanceof Damageable){
//                                        if (entity.getLocation().distance(stand.getLocation()) <= 8) {
//                                            Vector n = stand.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.02*(1-(stand.getLocation().distance(entity.getLocation()))/12));
//                                            entity.setVelocity(entity.getVelocity().add(n));
//                                            if (entity.getLocation().distance(stand.getLocation()) <= 3) {
//                                                ((Damageable) entity).damage(3,stand);
//                                            }
//                                        }
//                                    }
//
//                                }
//                            }
//                        }, 0, 1);
//                        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
//                            @Override
//                            public void run() {
//                                stand.remove();
//                                reflection.cancel();
//                            }
//                        }, 400);
//                    }
//                        Cast.ShieldDome(p, em, plugin);
//                        bolt.launch(event.getPlayer(),false);
//                        TornadoEffect effect = new TornadoEffect(em);
//                        effect.tornadoParticle = Particle.SPELL;
//                        effect.tornadoHeight = 2;
//                        effect.particleOffsetX = 1;
//                        effect.particleOffsetZ = 1;
//                        effect.duration = 1;
//                        effect.setLocation(p.getLocation().add(0,0,0));
//                        em.start(effect);
                                    //TNT bolt
                                }
                                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("TNT Bolt")) {
                                    if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                                        Cast.BeeBolt(p,plugin);
//                                        if (event.getClickedBlock() != null) {
//                                            Cast.PopUpTower(p, plugin, event.getBlockFace(), event.getClickedBlock());
//                                        }

                                    }
//                            p.launchProjectile(SmallFireball.class);
//                            Cast.FireworkBolt(p);
                                }
                                //End Warp
                                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("End Warp")) {
                                    Cast.PaintBomb(p,plugin);
//                        Cast.BloodSacrifice(p);
                                }
                                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Sniper Bolt")) {
                                    if (event.getClickedBlock() != null) {
                                        Cast.BeamPillar(p,plugin, event.getClickedBlock(),event.getBlockFace(),em);
                                    }
                                }

//                        Cast.ShadowInvis(p,plugin);
//                        Cast.ShieldDome(p, em, plugin);
                                //Air Dash
                                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Air Dash")) {
                                    System.out.println("yo");

                                }
                            }
                        }
                    }
                } else {
                    System.out.println("your hand is empty");
                }
            }
        }
    }
}


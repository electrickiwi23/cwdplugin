package me.mintnetwork.listeners;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.*;
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
                                        Cast.BlackHole(p,plugin,em);

//
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
                                        Cast.BloodBolt(p,plugin);

//                                        if (event.getClickedBlock() != null) {
//                                            Cast.PopUpTower(p, plugin, event.getBlockFace(), event.getClickedBlock());
//                                        }

                                    }
//
//                            Cast.FireworkBolt(p);
                                }
                                //End Warp
                                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("End Warp")) {
                                    Cast.FireworkBolt(p);
//                        Cast.BloodSacrifice(p);
                                }
                                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Sniper Bolt")) {
                                    Cast.ShadowGrapple(p);
                                }

//                        Cast.ShadowInvis(p,plugin);
//                        Cast.ShieldDome(p, em, plugin);
                                //Air Dash
                                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Air Dash")) {
                                    Cast.AirDash(p,plugin);

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


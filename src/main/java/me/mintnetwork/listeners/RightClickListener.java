package me.mintnetwork.listeners;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.*;
import me.mintnetwork.Main;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.spells.Cast;
import me.mintnetwork.ultimates.UltCast;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
                                if (StatusEffects.CanCast(p)) {
                                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Blood Bolt")) {
                                        Cast.StunSong(p,plugin);

                                        //TNT bolt
                                    }
                                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("TNT Bolt")) {
                                        if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                                            Cast.SpeedSong(p,plugin);

//                                        if (event.getClickedBlock() != null) {
//                                            Cast.PopUpTower(p, plugin, event.getBlockFace(), event.getClickedBlock());
//                                        }

                                        }
//
//                            Cast.FireworkBolt(p);
                                    }
                                    //End Warp
                                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("End Warp")) {
                                        if (event.getClickedBlock() != null) {
                                            Cast.HealPillar(p,plugin,event.getBlockFace(),event.getClickedBlock());
                                        }
//                        Cast.BloodSacrifice(p);
                                    }
                                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Sniper Bolt")) {
                                        Cast.HealSong(p,plugin);
                                    }

//                        Cast.ShadowInvis(p,plugin);
//                        Cast.ShieldDome(p, em, plugin);
                                    //Air Dash
                                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Air Dash")) {
                                        if (event.getClickedBlock() != null) {
                                            UltCast.ArrowTurret(p, plugin, event.getClickedBlock(), event.getBlockFace(),em);
                                        }

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
}


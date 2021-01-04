package me.mintnetwork.listeners;

import jdk.tools.jlink.plugin.Plugin;
import me.mintnetwork.Main;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import me.mintnetwork.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class RightClickListener implements Listener {

    private final Main plugin;

    public RightClickListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player p = (Player) event.getPlayer();
        if (lastUsed.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - lastUsed.get(p.getUniqueId()) <= 50) {
                return;
            }
        }
        lastUsed.put(p.getUniqueId(), System.currentTimeMillis());
            Block targetBlock = p.getTargetBlock((Set<Material>) null, 100);
            if (p.getInventory().getItemInMainHand() != null) {
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            //TNT bolt
                            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("TNT Bolt")) {
                                if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                                    if (p.getTotalExperience() >= 0) {
                                        p.launchProjectile(Arrow.class).setPierceLevel(1);
                                        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                                            @Override
                                            public void run() {
                                                p.launchProjectile(Arrow.class).setPierceLevel(1);
                                            }
                                        }, 3);
                                        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                                            @Override
                                            public void run() {
                                                p.launchProjectile(Arrow.class).setPierceLevel(1);
                                            }
                                        }, 6);
                                    } else {
                                        p.sendMessage(Utils.chat("&4not enough mana"));
                                    }
                                }
                            }
                        //End Warp
                        if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("End Warp")) {
//                p.launchProjectile(EnderPearl.class);
                            p.setAllowFlight(true);
                            p.setFlySpeed(0);
                            p.sendMessage("flying");

                        }
                        //Air Dash
                        if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Air Dash")) {
                            p.setGravity(false);
                            p.setVelocity(p.getVelocity().add(p.getEyeLocation().getDirection().multiply(2)));
                            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    p.setGravity(true);
                                }
                            }, 10);


                        }

                    }

                }
            }
        }
    }


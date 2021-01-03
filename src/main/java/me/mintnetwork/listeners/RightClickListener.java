package me.mintnetwork.listeners;

import jdk.tools.jlink.plugin.Plugin;
import me.mintnetwork.Main;
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

import java.util.Set;
import java.util.function.Predicate;

public class RightClickListener implements Listener {

    private final Main plugin;

    public RightClickListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player p = (Player) event.getPlayer();
        Block targetBlock = p.getTargetBlock((Set<Material>) null, 100);
        if (p.getInventory().getItemInMainHand() != null) {
            //TNT bolt
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("TNT Bolt")) {
                if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                    if (p.getTotalExperience() >= 0) {
                        for (int i = 0; i < 3; i++) {
                            p.launchProjectile(Arrow.class).setPierceLevel(1);

                        }
                    }else{
                        p.sendMessage(Utils.chat("&4not enough mana"));
                    }
                }
            }
            //End Warp
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("End Warp")) {
                p.launchProjectile(EnderPearl.class);


            }
            //Air Dash
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Air Dash")) {
                p.getPlayer().setGravity(false);
//                p.setVelocity();
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    public void run() {

                    }
                },100);

            }

        }

    }
}


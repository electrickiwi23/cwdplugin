package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CancelTill implements Listener {

    private final Main plugin;

    public CancelTill(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().equals(new ItemStack(Material.DIAMOND_HOE, 1))) {
            e.setCancelled(true);
        }
    }
}

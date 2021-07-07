package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemListener implements Listener {
    public DropItemListener(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode()!= GameMode.CREATIVE){
            event.getPlayer().sendMessage("You can't drop your items!");
            event.setCancelled(true);
        }

    }
}

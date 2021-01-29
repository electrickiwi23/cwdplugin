package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerHoldsItemListener implements Listener {
    private final Main plugin;

    public PlayerHoldsItemListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static Map<Player, BukkitTask> ZoomCode = new HashMap<Player, BukkitTask>();

    @EventHandler
    public void HoldsItem(PlayerItemHeldEvent event) {
        List<Player> zoomed = PlayerSneakListener.getZoomed();
        Player p = event.getPlayer();
        Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (zoomed.contains(p)) {
                    if (!p.getInventory().getItemInMainHand().getType().isAir()) {
                        if (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Sniper Bolt")) {
                            Map<Player,Float> PreviousSpeed = PlayerSneakListener.getPreviousSpeed();
                            Map<Player,ItemStack> PreviousHelm = PlayerSneakListener.getPreviousHelm();
                            p.setWalkSpeed(PreviousSpeed.get(p));
                            p.getInventory().setHelmet(PreviousHelm.get(p));
                            zoomed.remove(p);
                        }
                    }
                }
            }
        });
    }

}

package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.DecayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockPlaceListener implements Listener {

    private final Main plugin;

    public BlockPlaceListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    static ArrayList<Material> mats = new ArrayList<Material>(Arrays.asList(Material.RED_WOOL, Material.BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,Material.GRAY_WOOL));

    @EventHandler
    public void onClick(BlockPlaceEvent event) {

        if (mats.contains(event.getItemInHand().getType())){
            new DecayBlock(70,0,event.getBlock());

        }
    }
}

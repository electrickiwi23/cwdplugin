package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.repeaters.BlockDecay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.HashMap;

public class BlockFallListener implements Listener{

    private final Main plugin;

    public BlockFallListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static HashMap<FallingBlock, DecayBlock> fallingDecays = new HashMap<>();

    @EventHandler
    public void onClick(EntityChangeBlockEvent event) {
        if (event.getEntity().getType()== EntityType.FALLING_BLOCK){
            FallingBlock falling = (FallingBlock) event.getEntity();
            if (event.getTo().equals(Material.AIR)) {
                if (BlockDecay.decay.containsKey(event.getBlock())) {
                    fallingDecays.put(falling, BlockDecay.decay.get(event.getBlock()));
                    BlockDecay.decay.get(event.getBlock()).falling = true;
                    BlockDecay.decay.remove(event.getBlock());
                }
            } else {
               if (fallingDecays.containsKey(falling)){
                   BlockDecay.decay.put(event.getBlock(),fallingDecays.get(falling));
                   fallingDecays.get(falling).block = event.getBlock();
                   fallingDecays.get(falling).falling =false;
                   fallingDecays.remove(falling);
               }
            }
        }
    }
}

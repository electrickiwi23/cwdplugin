package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.repeaters.BlockDecay;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FireworkExpolodeListener implements Listener {

    private final Main plugin;

    public FireworkExpolodeListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHit(FireworkExplodeEvent event) {
        Firework firework = event.getEntity();

        HashMap<Block, DecayBlock> decayList = BlockDecay.decay;
        for (Block b:decayList.keySet()) {
            if (b.getLocation().add(.5,.5,.5).distance(firework.getLocation())<=3){
                decayList.get(b).damage((int) (10 + (75.0/b.getLocation().add(.5,.5,.5).distanceSquared(firework.getLocation()))));
                decayList.get(b).setForceful(true);
            }
        }
    }

    }
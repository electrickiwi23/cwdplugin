package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockDecay {
    public static HashMap<Block,DecayBlock> decay = new HashMap<>();


    public static void decayRepeater(Main plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                List<Block> list = new ArrayList<>();
                for (DecayBlock block:decay.values()) {
                    block.tickBlock();

                    if (block.age>=block.decayTime){
                        block.block.breakNaturally();
                        list.add(block.block);
                    }
                }

                for (Block r : list) {
                    decay.remove(r);
                }

            }
        }, 0, 2);
    }


}

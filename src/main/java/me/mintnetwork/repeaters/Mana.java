package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.commands.GiveWand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Mana {

    private final Main plugin;

    public Mana(Main plugin) {
        this.plugin = plugin;
    };

    Map<String, Integer> playerMana = new HashMap<String, Integer>();
    Map<String, Integer> manaCounter = new HashMap<String, Integer>();


    public void mana(Main plugin) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String uuid = player.getUniqueId().toString();
            playerMana.put(uuid, null);
            manaCounter.put(uuid, null);
        }
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (String player : playerMana.keySet()) {
                    if (playerMana.get(player)<10){
                        manaCounter.replace(player, manaCounter.get(player)+1);
                        if (manaCounter.get(player)>=6) {
                            playerMana.replace(player, playerMana.get(player) + 1);
                            Bukkit.getPlayer(UUID.fromString(player)).setLevel(playerMana.get(player));
                            manaCounter.replace(player, 0);
                            }
                        }
                    }
                }
            },0,10);
        }
}

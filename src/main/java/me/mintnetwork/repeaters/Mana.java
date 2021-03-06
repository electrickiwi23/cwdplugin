package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.commands.GiveWand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class Mana{
    private final Main plugin;

    public Mana(Main plugin) {
        this.plugin = plugin;
    };

    private static Map<Player, Integer> playerMana = new HashMap<Player, Integer>();
    public static Map<Player, Integer> manaCounter = new HashMap<Player, Integer>();


    public static Map<Player, Integer> getPlayerMana() {return playerMana; }
    public static Map<Player, Integer> getManaCounter() {return manaCounter; }


    public static void tickMana(Player player){
        if (playerMana.get(player) < 10) {
            manaCounter.replace(player, manaCounter.get(player) + 1);
            //if player has a mana counter of 6 add a mana and set their xp to mana and resets their counter
            if (manaCounter.get(player) >= 6) {
                playerMana.replace(player, playerMana.get(player) + 1);
                player.setLevel(playerMana.get(player));
                manaCounter.replace(player, 0);;
            }
        }
    }

    public static boolean spendMana(Player p, int c) {
        boolean has = playerMana.get(p)>=c;
        if (has) {
            playerMana.replace(p, playerMana.get(p)-c);
            p.setLevel(playerMana.get(p));
        }
        return has;
    }

    public void mana(Main plugin) {
        System.out.println("generating mana");
        for (Player player : Bukkit.getOnlinePlayers()) {
            //on start up add player to the list
            playerMana.put(player, 0);
            manaCounter.put(player, 0);
        }
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : playerMana.keySet()) {
                    //if player has less than 10 mana add one to their mana counter
                    if (playerMana.get(player) < 10) {
                        manaCounter.replace(player, manaCounter.get(player) + 1);
                        //if player has a mana counter of 6 add a mana and set their xp to mana and resets their counter
                        if (manaCounter.get(player) >= 6) {
                            playerMana.replace(player, playerMana.get(player) + 1);
                            player.setLevel(playerMana.get(player));
                            manaCounter.replace(player, 0);
                        }
                    }
                }
            }
        }, 0, 10);
    }
}

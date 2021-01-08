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

    private static Map<String, Integer> playerMana = new HashMap<String, Integer>();
    Map<String, Integer> manaCounter = new HashMap<String, Integer>();

    public static boolean spendMana(Player p, int c) {
        String id = p.getUniqueId().toString();
        boolean has = playerMana.get(id)>=c;
        if (has) {
            playerMana.replace(id, playerMana.get(id)-c);
            p.setLevel(playerMana.get(id));
        }
        return has;
    }

    public Map<String, Integer>  getMana() {
        return playerMana;
    }

    public void mana(Main plugin) {
        System.out.println("generating mana");
        for (Player player : Bukkit.getOnlinePlayers()) {
            String uuid = player.getUniqueId().toString();
            playerMana.put(uuid, 0);
            manaCounter.put(uuid, 0);
        }
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (String player : playerMana.keySet()) {
                    if (playerMana.get(player) < 10) {
                        manaCounter.replace(player, manaCounter.get(player) + 1);
                        if (manaCounter.get(player) >= 6) {
                            playerMana.replace(player, playerMana.get(player) + 1);
                            Bukkit.getPlayer(UUID.fromString(player)).setLevel(playerMana.get(player));
                            manaCounter.replace(player, 0);
                        }
                    }
                }
            }
        }, 0, 10);
    }
}

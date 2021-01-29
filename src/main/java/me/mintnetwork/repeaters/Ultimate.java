package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Ultimate {

    private final Main plugin;

    public Ultimate(Main plugin) {
            this.plugin = plugin;
    }

    private static Map<String, Integer> playerUlt = new HashMap<String, Integer>();
    private static Map<String, Integer> playerClass = new HashMap<String, Integer>();

    public boolean spendUlt(Player p, int c) {
        String id = p.getUniqueId().toString();
        boolean has = playerUlt.get(id)>=c;
        if (has) {
            playerUlt.replace(id, 0);
        }
        return has;
    }

    public Map<String, Integer>  getPlayerUlt() {
        return playerUlt;
    }

    public void ult(Main plugin) {
        System.out.println("generating ult");
        for (Player player : Bukkit.getOnlinePlayers()) {
            String uuid = player.getUniqueId().toString();
            playerUlt.put(uuid, 0);
        }
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (String player : playerUlt.keySet()) {
                    switch (playerClass.get(player)) {
                        case 1:
                            if (playerUlt.get(player) < 120) {
                                playerUlt.replace(player, playerUlt.get(player) + 1);
                                break;
                            }
                            break;
                        case 2:
                            if (playerUlt.get(player) < 180) {
                                playerUlt.replace(player, playerUlt.get(player) + 1);
                                break;
                            }
                            break;
                        case 3:
                            if (playerUlt.get(player) < 240) {
                                playerUlt.replace(player, playerUlt.get(player) + 1);
                                break;
                            }
                            break;
                    }
                }
            }
            }, 0, 10);
        }
    }

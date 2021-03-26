package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
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

    private static Map<Player, Integer> playerUlt = new HashMap<>();

    public static boolean spendUlt(Player p, int c) {
        boolean has = playerUlt.get(p)>=c;
        if (has) {
            playerUlt.replace(p, 0);
        }
        return has;
    }

    public static void ult(Main plugin) {
        System.out.println("generating ult");
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerUlt.put(player, 0);
        }
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : playerUlt.keySet()) {
                    Wizard wizard = WizardInit.playersWizards.get(player);
                    switch (wizard.ClassID) {
                        case "spell slinger":
                        case "painter":
                            if (playerUlt.get(player) < 120) {
                                playerUlt.replace(player, playerUlt.get(player) + 1);
                                break;
                            }
                            break;
                        case "demolitionist":
                        case "sky flyer":
                        case "berserker":
                        case "alchemist":
                        case "bard":
                        case "blood mage":
                        case "builder":
                        case "cleric":
                        case "tactician":
                            if (playerUlt.get(player) < 180) {
                                playerUlt.replace(player, playerUlt.get(player) + 1);
                                break;
                            }
                            break;
                        case "shadow":
                        case "pillar man":
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

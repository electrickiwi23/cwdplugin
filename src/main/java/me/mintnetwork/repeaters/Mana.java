package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.commands.GiveWand;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
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

    public static void tickMana(Player player){
        Wizard wizard = WizardInit.playersWizards.get(player);
        //if player has less than 10 mana add one to their mana counter
        if (wizard.Mana < 10) {
            wizard.ManaTick++;
            //if player has a mana counter of 6 add a mana and set their xp to mana and resets their counter

            if (wizard.ManaTick >= 6||wizard.ClassID.equals("spell slinger")&&wizard.ManaTick >= 5) {
                wizard.Mana++;
                player.setLevel(wizard.Mana);
                wizard.ManaTick = 0;
            }
        }
    }

    public static boolean spendMana(Player p, int c) {
        Wizard wizard = WizardInit.playersWizards.get(p);
        boolean has = wizard.Mana>=c;
        if (has) {
            wizard.Mana = wizard.Mana-c;
            p.setLevel(wizard.Mana);
        }
        return has;
    }

    public static void mana(Main plugin) {
        System.out.println("generating mana");
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : WizardInit.playersWizards.keySet()) {
                    tickMana(player);
                }
            }
        }, 0, 10);
    }
}

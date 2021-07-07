package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Mana{

    public static void addMana(Player player,int amount){
        for (int i = 0; i < amount; i++) {
            tickMana(player);
        }
    }

    public static void tickMana(Player player){
        Wizard wizard = WizardInit.playersWizards.get(player.getUniqueId());
        //if player has less than 10 mana add one to their mana counter
        if (wizard.Mana < 10) {
            wizard.ManaTick++;
            //if player has a mana counter of 6 add a mana and set their xp to mana and resets their counter

            if (wizard.ManaTick >= 6||wizard.kitID.equals(Kit.SPELL_SLINGER)&&wizard.ManaTick >= 5) {
                wizard.Mana++;
                player.setLevel(wizard.Mana);
                wizard.ManaTick = 0;
            }
        }
    }

    public static boolean spendMana(Player p, int c) {
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
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
                if (GameStart.gameRunning) {
                    for (UUID uuid: WizardInit.playersWizards.keySet()) {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player!=null) {
                            if (player.isOnline()) {
                                player.setFoodLevel(8);
                                tickMana(player);
                            }
                        }
                    }
                }
            }
        }, 0, 10);
    }
}

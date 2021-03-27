package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Ultimate {

    private final Main plugin;

    public Ultimate(Main plugin) {
            this.plugin = plugin;
    }

    public static boolean spendUlt(Player p) {

        Wizard wizard = WizardInit.playersWizards.get(p);
        boolean has = false;

        switch (wizard.ClassID) {
            case "spell slinger":
            case "painter":
                if (wizard.Ult == 120) {
                    has = true;
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
                if (wizard.Ult == 180) {
                    has = true;
                }
                break;
            case "shadow":
            case "pillar man":
                if (wizard.Ult == 240) {
                    has = true;
                }
                break;
        }
        return has;
    }

    public static void ult(Main plugin) {
        System.out.println("generating ult");
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : WizardInit.playersWizards.keySet()) {
                    Wizard wizard = WizardInit.playersWizards.get(player);
                    switch (wizard.ClassID) {
                        case "spell slinger":
                        case "painter":
                            if (wizard.Ult < 120) {
                                wizard.Ult = wizard.Ult + 5;
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
                            if (wizard.Ult < 180) {
                                wizard.Ult = wizard.Ult + 5;
                                break;
                            }
                            break;
                        case "shadow":
                        case "pillar man":
                            if (wizard.Ult < 240) {
                                wizard.Ult = wizard.Ult + 5;
                                break;
                            }
                            break;
                    }
                }
            }
            }, 0, 5);
        }
    }

package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Ultimate {

    private final Main plugin;

    public Ultimate(Main plugin) {
            this.plugin = plugin;
    }

    public static boolean hasUlt(Player p) {

        Wizard wizard = WizardInit.playersWizards.get(p);
        boolean has = false;

        switch (wizard.ClassID) {
            case "spell slinger":
            case "painter":
                if (wizard.Ult >= 480) {
                    has = true;
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
            case "protector":
                if (wizard.Ult >= 720) {
                    has = true;
                }
                break;
            case "shadow":
            case "pillar man":
                if (wizard.Ult >= 960) {
                    has = true;
                }
                break;
        }
        return has;
    }

    public static boolean spendUlt(Player p) {

        Wizard wizard = WizardInit.playersWizards.get(p);
        boolean has = false;


        switch (wizard.ClassID) {
            case "spell slinger":
            case "painter":
                if (wizard.Ult >= 480) {
                    has = true;
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
            case "protector":
                if (wizard.Ult >= 720) {
                    has = true;
                }
                break;
            case "shadow":
            case "pillar man":
                if (wizard.Ult >= 960) {
                    has = true;
                }
                break;
        }
        if (has){
            wizard.Ult = 0;
        }

        return has;
    }

    public static double getUltPercentage(Player p) {

        Wizard wizard = WizardInit.playersWizards.get(p);
        double percentage = 0;

        switch (wizard.ClassID) {
            case "spell slinger":
            case "painter":
                percentage = wizard.Ult/480.0;
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
            case "protector":
                percentage = wizard.Ult/720.0;
                break;
            case "shadow":
            case "pillar man":
                percentage = wizard.Ult/960.0;
                break;
        }

        return percentage;
    }

    public static void ult(Main plugin) {

        System.out.println("generating ult");

        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (GameStart.gameRunning) {
                    for (Player player : WizardInit.playersWizards.keySet()) {
                        Wizard wizard = WizardInit.playersWizards.get(player);
                        switch (wizard.ClassID) {
                            case "spell slinger":
                            case "painter":
                                if (wizard.Ult < 480) {
                                    wizard.Ult++;
                                }
                                if (wizard.Ult == 479){
                                    player.sendMessage("ULTIMATE CHARGED");
                                }

                                if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                    for (ItemStack i : player.getInventory().getContents()) {
                                        if (i != null) {
                                            if (i.getType().equals(Material.DIAMOND_HOE)) {
                                                System.out.println(getUltPercentage(player));
                                                ItemMeta itemMeta = i.getItemMeta();
                                                ((Damageable) itemMeta).setDamage((int) ((1561) - 1561*getUltPercentage(player)));
                                                i.setItemMeta(itemMeta);
                                            }
                                        }
                                    }
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
                            case "protector":
                                if (wizard.Ult < 720) {
                                    wizard.Ult++;
                                }
                                if (wizard.Ult == 719){
                                    player.sendMessage("ULTIMATE CHARGED");
                                }
                                if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                    for (ItemStack i : player.getInventory().getContents()) {
                                        if (i != null) {
                                            if (i.getType().equals(Material.DIAMOND_HOE)) {
                                                ItemMeta itemMeta = i.getItemMeta();
                                                ((Damageable) itemMeta).setDamage((int) ((1561) - 1561*getUltPercentage(player)));
                                                i.setItemMeta(itemMeta);
                                            }
                                        }
                                    }
                                }
                                break;
                            case "shadow":
                            case "pillar man":
                                if (wizard.Ult < 960) {
                                    wizard.Ult++;
                                }
                                if (wizard.Ult == 959){
                                    player.sendMessage("ULTIMATE CHARGED");
                                }
                                if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                    for (ItemStack i : player.getInventory().getContents()) {
                                        if (i != null) {
                                            if (i.getType().equals(Material.DIAMOND_HOE)) {
                                                ItemMeta itemMeta = i.getItemMeta();
                                                ((Damageable) itemMeta).setDamage((int) ((1561) - 1561*getUltPercentage(player)));
                                                i.setItemMeta(itemMeta);
                                            }
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }
            }, 0, 5);
        }
    }

package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

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

    public static int getUltPercentage(Player p) {

        Wizard wizard = WizardInit.playersWizards.get(p);
        int percentage = 0;

        switch (wizard.ClassID) {
            case "spell slinger":
            case "painter":
                percentage = wizard.Ult/120;
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
                percentage = wizard.Ult/180;
                break;
            case "shadow":
            case "pillar man":
                percentage = wizard.Ult/240;
                break;
        }

        return percentage;
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
                            if (wizard.Ult < 480) {
                                wizard.Ult++;
                                break;
                            }
                            if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                for (ItemStack i : player.getInventory().getContents()) {
                                    if (i != null) {
                                        if (i.getType().equals(Material.DIAMOND_HOE)) {
                                            ItemMeta itemMeta = i.getItemMeta();
                                            if (((Damageable) itemMeta).getDamage() < 1560) {
                                                ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage()+16);
                                            } else {
                                                ((Damageable) itemMeta).setDamage(1561);
                                            }
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
                            if (wizard.Ult < 720) {
                                wizard.Ult++;
                                break;
                            }
                            if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                for (ItemStack i : player.getInventory().getContents()) {
                                    if (i != null) {
                                        if (i.getType().equals(Material.DIAMOND_HOE)) {
                                            ItemMeta itemMeta = i.getItemMeta();
                                            if (((Damageable) itemMeta).getDamage() < 1551) {
                                                ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage()+11);
                                            } else {
                                                ((Damageable) itemMeta).setDamage(1561);
                                            }
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
                                break;
                            }
                            if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                for (ItemStack i : player.getInventory().getContents()) {
                                    if (i != null) {
                                        if (i.getType().equals(Material.DIAMOND_HOE)) {
                                            ItemMeta itemMeta = i.getItemMeta();
                                            if (((Damageable) itemMeta).getDamage() < 1536) {
                                                ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage()+8);
                                            } else {
                                                ((Damageable) itemMeta).setDamage(1561);
                                            }
                                            i.setItemMeta(itemMeta);
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
            }
            }, 0, 5);
        }
    }

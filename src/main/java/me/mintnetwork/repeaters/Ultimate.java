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

import java.util.UUID;

public class Ultimate {

    private final Main plugin;

    public Ultimate(Main plugin) {
            this.plugin = plugin;
    }

    public static boolean hasUlt(Player p) {

        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
        boolean has = false;

        switch (wizard.kitID) {
            case SPELL_SLINGER:
            case PAINTER:
            case CLERIC:
                if (wizard.Ult >= 480) {
                    has = true;
                }
                break;
            case DEMOLITIONIST:
            case AVIATOR:
            case BERSERKER:
            case ALCHEMIST:
            case BARD:
            case BLOOD_MAGE:
            case BUILDER:
            case TACTICIAN:
            case PROTECTOR:
                if (wizard.Ult >= 720) {
                    has = true;
                }
                break;
            case SHADOW:
            case PILLAR_MAN:
                if (wizard.Ult >= 960) {
                    has = true;
                }
                break;
        }
        return has;
    }

    public static boolean spendUlt(Player p) {

        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
        boolean has = false;


        switch (wizard.kitID) {
            case SPELL_SLINGER:
            case PAINTER:
            case CLERIC:
                if (wizard.Ult >= 480) {
                    has = true;
                }
                break;
            case DEMOLITIONIST:
            case AVIATOR:
            case BERSERKER:
            case ALCHEMIST:
            case BARD:
            case BLOOD_MAGE:
            case BUILDER:
            case TACTICIAN:
            case PROTECTOR:
                if (wizard.Ult >= 720) {
                    has = true;
                }
                break;
            case SHADOW:
            case PILLAR_MAN:
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

        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
        double percentage = 0;

        switch (wizard.kitID) {
            case SPELL_SLINGER:
            case PAINTER:
            case CLERIC:
                percentage = wizard.Ult/480.0;
                break;
            case DEMOLITIONIST:
            case AVIATOR:
            case BERSERKER:
            case ALCHEMIST:
            case BARD:
            case BLOOD_MAGE:
            case BUILDER:
            case TACTICIAN:
            case PROTECTOR:
                percentage = wizard.Ult/720.0;
                break;
            case SHADOW:
            case PILLAR_MAN:
                percentage = wizard.Ult/960.0;
                break;
        }

        return percentage;
    }

    public static void FullCharge(Player p){
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());

        switch (wizard.kitID) {
            case SPELL_SLINGER:
            case PAINTER:
            case CLERIC:
                wizard.Ult = 480;
                break;
            case DEMOLITIONIST:
            case AVIATOR:
            case BERSERKER:
            case ALCHEMIST:
            case BARD:
            case BLOOD_MAGE:
            case BUILDER:
            case TACTICIAN:
            case PROTECTOR:
                wizard.Ult = 720;
                break;
            case SHADOW:
            case PILLAR_MAN:
                wizard.Ult = 960;
                break;
        }
    }


    public static void ult(Main plugin) {

        System.out.println("generating ult");

        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (GameStart.gameRunning) {
                    for (UUID uuid: WizardInit.playersWizards.keySet()) {
                        Player player = Bukkit.getPlayer(uuid);
                        Wizard wizard = WizardInit.playersWizards.get(uuid);
                        if (player != null) {
                            if (wizard.kitID!=null) {
                                switch (wizard.kitID) {
                                    case SPELL_SLINGER:
                                    case PAINTER:
                                    case CLERIC:
                                        if (wizard.Ult < 480) {
                                            wizard.Ult++;
                                        }
                                        if (wizard.Ult == 479) {
                                            player.sendMessage("ULTIMATE CHARGED");
                                        }

                                        if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                            for (ItemStack i : player.getInventory().getContents()) {
                                                if (i != null) {
                                                    if (i.getType().equals(Material.DIAMOND_HOE)) {
                                                        ItemMeta itemMeta = i.getItemMeta();
                                                        ((Damageable) itemMeta).setDamage((int) ((1561) - 1561 * getUltPercentage(player)));
                                                        i.setItemMeta(itemMeta);
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case DEMOLITIONIST:
                                    case AVIATOR:
                                    case BERSERKER:
                                    case ALCHEMIST:
                                    case BARD:
                                    case BLOOD_MAGE:
                                    case BUILDER:
                                    case TACTICIAN:
                                    case PROTECTOR:
                                        if (wizard.Ult < 720) {
                                            wizard.Ult++;
                                        }
                                        if (wizard.Ult == 719) {
                                            player.sendMessage("ULTIMATE CHARGED");
                                        }
                                        if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                            for (ItemStack i : player.getInventory().getContents()) {
                                                if (i != null) {
                                                    if (i.getType().equals(Material.DIAMOND_HOE)) {
                                                        ItemMeta itemMeta = i.getItemMeta();
                                                        ((Damageable) itemMeta).setDamage((int) ((1561) - 1561 * getUltPercentage(player)));
                                                        i.setItemMeta(itemMeta);
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case SHADOW:
                                    case PILLAR_MAN:
                                        if (wizard.Ult < 960) {
                                            wizard.Ult++;
                                        }
                                        if (wizard.Ult == 959) {
                                            player.sendMessage("ULTIMATE CHARGED");
                                        }
                                        if (player.getInventory().contains(Material.DIAMOND_HOE)) {
                                            for (ItemStack i : player.getInventory().getContents()) {
                                                if (i != null) {
                                                    if (i.getType().equals(Material.DIAMOND_HOE)) {
                                                        ItemMeta itemMeta = i.getItemMeta();
                                                        ((Damageable) itemMeta).setDamage((int) ((1561) - 1561 * getUltPercentage(player)));
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
                }
            }
            }, 0, 5);
        }
    }

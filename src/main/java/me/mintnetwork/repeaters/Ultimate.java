package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
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

        if (wizard.kitID!= Kit.NONE){
            has = wizard.Ult >= wizard.kitID.KitItems.ultTime;
        }
        return has;
    }

    public static boolean spendUlt(Player p) {
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
        boolean has = hasUlt(p);
        if (has){
            wizard.Ult = 0;
        }
        return has;
    }

    public static double getUltPercentage(Player p) {
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
        double percentage = 0;

        if (wizard.kitID!= Kit.NONE){
            percentage = wizard.Ult /(double) wizard.kitID.KitItems.ultTime;
        }

        return percentage;
    }

    public static void FullCharge(Player p){
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());

        if (wizard.kitID!= Kit.NONE){
            wizard.Ult = wizard.kitID.KitItems.ultTime;
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
                            if (wizard.kitID!=Kit.NONE) {
                                if (wizard.Ult < wizard.kitID.KitItems.ultTime) {
                                    wizard.Ult++;
                                }
                                if (wizard.Ult == wizard.kitID.KitItems.ultTime-1) {
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
                            }
                        }
                    }
                }
            }
            }, 0, 5);
        }
    }

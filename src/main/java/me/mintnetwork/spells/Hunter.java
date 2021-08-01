package me.mintnetwork.spells;

import me.mintnetwork.Objects.ArrowType;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class Hunter extends KitItems{

    public Hunter(){
        ultTime = Utils.HUNTER_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();


        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.BOMB_ARROW_COST);
        lore.add(ChatColor.GRAY + "Attach explosives to your next bow shot");
        lore.add(ChatColor.GRAY + "that explodes soon after contact.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Bomb Arrow"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SCAN_ARROW_COST);
        lore.add(ChatColor.GRAY + "Imbues your arrow with light ");
        lore.add(ChatColor.GRAY + "that reveals those around the ");
        lore.add(ChatColor.GRAY + "arrow after a short period.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Scan Arrow"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.DECAY_ARROW_COST);
        lore.add(ChatColor.GRAY + "Attaches a tracker to your next ");
        lore.add(ChatColor.GRAY + "arrow that reveals the targets");
        lore.add(ChatColor.GRAY + "position in short bursts.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Tracking Arrow"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY + "converts your arrow to light and ");
        lore.add(ChatColor.GRAY + "shoots out dealing damage to anyone");
        lore.add(ChatColor.GRAY + "in the ray(goes through walls).");
        meta.setDisplayName(ChatColor.GOLD+("Starlight Arrow"));
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Gain a bow to suppress enemies");
        lore.add(ChatColor.GRAY + "from a long distance away.");
        meta.setDisplayName(ChatColor.WHITE + "Ranged Tactics");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Outplay your enemies by gathering ");
        lore.add(ChatColor.GRAY + "information and out-ranging the enemies.");

        menuItem.setType(Material.BOW);
        meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN+"Hunter");
        meta.addItemFlags(ItemFlag.values());
        meta.setLore(lore);
        menuItem.setItemMeta(meta);

        //create itemstacks for each wand of the class
    }

    public static void deselectArrow(Player p){
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("Bomb Arrow","Scan Arrow","Tracking Arrow","Starlight Arrow"));
        for (ItemStack item:p.getInventory()){
            if (item!=null) {
                if (strings.contains(ChatColor.stripColor(item.getItemMeta().getDisplayName()))) {
                    item.removeEnchantment(Enchantment.DURABILITY);
                }
            }
        }
        WizardInit.playersWizards.get(p.getUniqueId()).nextArrow = ArrowType.REGULAR;
    }

    public static void SelectArrow(Player p,ArrowType type){
        deselectArrow(p);
        Wizard w = WizardInit.playersWizards.get(p.getUniqueId());
        p.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        p.getInventory().getItemInMainHand().setItemMeta(meta);
        w.nextArrow = type;
    }

    public static void castBombArrow(Player p) {
        Wizard w = WizardInit.playersWizards.get(p.getUniqueId());
        if (w.nextArrow!=ArrowType.BOMB) {
            if (w.Mana >= Utils.BOMB_ARROW_COST) {
                SelectArrow(p,ArrowType.BOMB);
            }
        } else {
            deselectArrow(p);
        }
    }

    public static void castScanArrow(Player p) {
        Wizard w = WizardInit.playersWizards.get(p.getUniqueId());
        if (w.nextArrow!=ArrowType.SCAN) {
            if (w.Mana >= Utils.SCAN_ARROW_COST) {
                SelectArrow(p,ArrowType.SCAN);
            }
        } else {
            deselectArrow(p);
        }
    }

    public static void castDecayArrow(Player p) {
        Wizard w = WizardInit.playersWizards.get(p.getUniqueId());
        if (w.nextArrow!=ArrowType.DECAY) {
            if (w.Mana >= Utils.DECAY_ARROW_COST) {
                SelectArrow(p,ArrowType.DECAY);
            }
        } else {
            deselectArrow(p);
        }
    }
    public static void castLightArrow(Player p) {
        Wizard w = WizardInit.playersWizards.get(p.getUniqueId());
        if (w.nextArrow!=ArrowType.LIGHT) {
            if (Ultimate.hasUlt(p)) {
                SelectArrow(p,ArrowType.LIGHT);
            }
        } else {
            deselectArrow(p);
        }
    }
}

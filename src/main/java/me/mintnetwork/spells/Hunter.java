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


        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.HEAL_SONG_COST);
        lore.add(ChatColor.GRAY + "Your singing heals your teammates");
        lore.add(ChatColor.GRAY + "who listen for a large amount.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Bomb Arrow"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SPEED_SONG_COST);
        lore.add(ChatColor.GRAY + "Your singing allows your teammates");
        lore.add(ChatColor.GRAY + "who listen to run faster.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Scan Arrow"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.STUN_SONG_COST);
        lore.add(ChatColor.GRAY + "Throws a bouncy jukebox that ");
        lore.add(ChatColor.GRAY + "explodes after a short amount");
        lore.add(ChatColor.GRAY + "of time stunning enemies.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Tracking Arrow"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY + "Your song entrances your enemies and ");
        lore.add(ChatColor.GRAY + "forces them to look your direction.");
        meta.setDisplayName(ChatColor.GOLD+("Starlight Arrow"));
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Inspire teammates around you, giving");
        lore.add(ChatColor.GRAY + "them points that buff their next attack.");
        meta.setDisplayName(ChatColor.WHITE + "Bardic Inspiration");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Use the power of music to heal");
        lore.add(ChatColor.GRAY + "your allies and ward off enemies.");

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

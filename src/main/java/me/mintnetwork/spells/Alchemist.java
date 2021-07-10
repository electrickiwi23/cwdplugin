package me.mintnetwork.spells;

import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Map;

public class Alchemist extends KitItems {


    public Alchemist(){
        ultTime = Utils.ALCHEMIST_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();

        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.ACID_POTION_COST);
        lore.add(ChatColor.GRAY + "Throw a potion which deals massive");
        lore.add(ChatColor.GRAY + "damage within its radius over time.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET + "Acid Vial");
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.HEAL_POTION_COST);
        lore.add(ChatColor.GRAY + "Throw a potion which heals");
        lore.add(ChatColor.GRAY + "allies within its radius over time.");
        meta.setDisplayName(ChatColor.RESET+("Healing Potion"));
        meta.setLore(lore);
        lore.clear();
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.DEBUFF_POTION_COST);
        lore.add(ChatColor.GRAY + "Throws a potion which slows and ");
        lore.add(ChatColor.GRAY + "weakens anyone within its radius.");
        meta.setDisplayName(ChatColor.RESET+("Plague Potion"));
        meta.setLore(lore);
        lore.clear();
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY +"Throws a potion which shields ");
        lore.add(ChatColor.GRAY +"allies within its radius");
        meta.setDisplayName(ChatColor.GOLD+("Elixir of Immortality"));
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Occasionally gets splash potions ");
        lore.add(ChatColor.GRAY + "to use on your allys and enemies.");
        meta.setDisplayName(ChatColor.WHITE + "Mobile Brewery");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Use powerful area of effect");
        lore.add(ChatColor.GRAY + "spells to control the battlefield");
        lore.add(ChatColor.GRAY + "and help your teammates.");

        menuItem.setType(Material.SPLASH_POTION);
        meta = menuItem.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setDisplayName(ChatColor.DARK_GREEN+"Alchemist");
        meta.setLore(lore);

        PotionMeta potionMeta = (PotionMeta) meta;
        potionMeta.setColor(Color.LIME);
        menuItem.setItemMeta(potionMeta);

        //create itemstacks for each wand of the class
    }

    public static void AcidPotion(Player p){
        if (Mana.spendMana(p, Utils.ACID_POTION_COST)) {
            ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
            ItemStack item = (new ItemStack(Material.SPLASH_POTION));
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.fromRGB(0, 255, 0));
            item.setItemMeta(potionMeta);
            potion.setItem(item);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(potion, "Acid Potion");
        }
    }

    public static void HealPotion(Player p){
        if (Mana.spendMana(p, Utils.HEAL_POTION_COST)) {
            ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
            ItemStack item = (new ItemStack(Material.SPLASH_POTION));
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.fromRGB(240, 40, 128));
            item.setItemMeta(potionMeta);
            potion.setItem(item);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(potion, "Heal Potion");
        }
    }

    public static void DebuffPotion(Player p){
        if (Mana.spendMana(p, Utils.DEBUFF_POTION_COST)) {
            ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
            ItemStack item = (new ItemStack(Material.SPLASH_POTION));
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.fromRGB(30, 0, 0));
            item.setItemMeta(potionMeta);
            potion.setItem(item);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(potion, "Debuff Potion");
        }
    }

    public static void ImmortalPotionUlt(Player p) {
        if (Ultimate.spendUlt(p)) {
            ThrownPotion potion = p.launchProjectile(ThrownPotion.class);
            ItemStack item = (new ItemStack(Material.SPLASH_POTION));
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.fromRGB(255, 215, 0));
            item.setItemMeta(potionMeta);
            potion.setItem(item);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(potion, "Immortal Potion");
        }
    }
}

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Alchemist {
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

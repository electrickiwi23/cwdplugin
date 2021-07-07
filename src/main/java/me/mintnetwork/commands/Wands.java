package me.mintnetwork.commands;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wands implements CommandExecutor {

    public static Inventory wandsInv;

    public Wands(Main plugin) {
        plugin.getCommand("wands").setExecutor(this);
    }


    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot do this!");
            return true;
        }
        Player player = (Player) sender;

        player.openInventory(wandsEditInventory(player, wandsInv));

        return true;
    }

    public static void createWandsMenu(){
        wandsInv = Bukkit.createInventory(null, 27, ChatColor.BOLD+"Select Wands");

        ItemStack item;
        item = new ItemStack(Material.);
        ItemMeta meta;
        List<String> lore = new ArrayList<String>();
        String select = ChatColor.GRAY + "Click to select wand";

//        list.add("fireworkbolt");
        lore.clear();
        lore.add(select);
        lore.add("");


        item.setType(Material.FIREWORK_ROCKET);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Firework Bolt");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(0, item);
//        list.add("jumpboost");
        lore.clear();
        lore.add(select);
        lore.add("Enhances your jump and slows your fall");

        item.setType(Material.RABBIT_FOOT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Jump Boost");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(1, item);


//        list.add("engineblast");
        lore.clear();
        lore.add(select);
        lore.add("A blast of smoke propels you and your enemies away");

        item.setType(Material.FIRE_CHARGE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Engine Blast");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(2, item);


//        list.add("dragonorb");
        lore.clear();
        lore.add(select);
        lore.add("Shoot a timed grenade that releases deadly gasses");

        item.setType(Material.DRAGON_BREATH);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Dragon Orb");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(3, item);


//        list.add("batsonar");
        lore.clear();
        lore.add(select);
        lore.add("Spawn a bat which locates and marks your enemies");

        item.setType(Material.SCULK_SENSOR);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Bat Sonar");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(4, item);


        //        list.add("tntring");
        lore.clear();
        lore.add(select);
        lore.add("Create a ring of explosives that detonate after a short time");

        item.setType(Material.TNT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"TNT Ring");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(5, item);


        //        list.add("hivebolt");
        lore.clear();
        lore.add(select);
        lore.add("Shoots out angry bees which attack anyone near. BEEware!");

        item.setType(Material.HONEY_BOTTLE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Hive Bolt");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(6, item);


//        list.add("blackhole");
//        list.add("endwarp");
//        list.add("babyboomer");
//        list.add("zombiesummon");
//        list.add("slimeball");
//        list.add("flashstep");
//        list.add("shoulderblitz");
//        list.add("anviltoss");
//        list.add("stormstrike");
//        list.add("manabullet");

        //


        // CLOSE 17
        item.setType(Material.BARRIER);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Close Menu");
        lore.clear();
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(26, item);

    }

    public static Inventory wandsEditInventory(Player player, Inventory invet){
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BOLD+"Select Wands");
        inv.setContents(invet.getContents());

        Wizard wizard = WizardInit.playersWizards.get(player.getUniqueId());

        String[] slots = {
                "Firework Bolt", "Jump Boost", "Engine Blast", "Dragon Orb", "Bat Sonar", "TNT Ring", "Hive Bolt", "Black Hole", "End Warp",
                "Baby Boomer", "Zombie Summon", "Slime Ball", "Flash Step", "Shoulder Blitz", "Anvil Toss", "Storm Strike", "Mana Bullet"
        };

        for (int i = 0; i < slots.length; i++) {
            for (ItemStack item: wizard.wands) {
                if (item.getItemMeta().getDisplayName().equals(slots[i])){
                    ItemStack pane = inv.getItem(i);
                    pane.setType(Material.LIME_STAINED_GLASS_PANE);
                    ItemMeta meta = pane.getItemMeta();
                    meta.setLore(Arrays.asList("You have already selected this wand"));
                    pane.setItemMeta(meta);
                    inv.setItem(i, pane);
                }
            }
        }


        return inv;

    }
}

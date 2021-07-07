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
import java.util.List;

public class Wands implements CommandExecutor {

    public static Inventory wandsInv;

    public Wands(Main plugin) {
        plugin.getCommand("wands").setExecutor(this);
    }


    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }


        Player p = (Player) sender;
        return false;
    }

    public static void createClassMenu(){
        wandsInv = Bukkit.createInventory(null, 18, ChatColor.BOLD+"Select Wand");

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
//        list.add("engineblast");
//        list.add("dragonorb");
//        list.add("batsonar");
//        list.add("tntring");
//        list.add("hivebolt");
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
        classInv.setItem(17, item);

    }
}

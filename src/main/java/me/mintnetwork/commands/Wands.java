package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
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
        item = new ItemStack(Material.BONE_MEAL);
        ItemMeta meta;
        List<String> lore = new ArrayList<String>();
        String select = ChatColor.GRAY + "Click to select wand";

//      FIREWORK BOLT
        lore.clear();
        lore.add(select);
        lore.add("Shoots an explosive rocket towards your enemies");


        item.setType(Material.FIREWORK_ROCKET);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Firework Bolt");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(0, item);


//      JUMP BOOST
        lore.clear();
        lore.add(select);
        lore.add("Enhances your jump and slows your fall");

        item.setType(Material.RABBIT_FOOT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Jump Boost");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(1, item);


//      ENGINE BLAST
        lore.clear();
        lore.add(select);
        lore.add("A blast of smoke propels you and your enemies away");

        item.setType(Material.FIRE_CHARGE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Engine Blast");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(2, item);


//      DRAGON ORB
        lore.clear();
        lore.add(select);
        lore.add("Shoot a timed grenade that releases deadly gasses");

        item.setType(Material.DRAGON_BREATH);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Dragon Orb");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(3, item);


//      BAT SONAR
        lore.clear();
        lore.add(select);
        lore.add("Spawn a bat which locates and marks your enemies");

        item.setType(Material.SCULK_SENSOR);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Bat Sonar");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(4, item);


//      TNT RING
        lore.clear();
        lore.add(select);
        lore.add("Create a ring of explosives that detonate after a short time");

        item.setType(Material.TNT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"TNT Ring");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(5, item);


//      HIVE BOLT
        lore.clear();
        lore.add(select);
        lore.add("Shoots out angry bees which attack anyone near. BEEware!");

        item.setType(Material.HONEY_BOTTLE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Hive Bolt");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(6, item);


//      BLACK HOLE
        lore.clear();
        lore.add(select);
        lore.add("Create a black hole which after charging sucks in everyone around it");

        item.setType(Material.NETHER_STAR);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Black Hole");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(7, item);


//      END WARP
        lore.clear();
        lore.add(select);
        lore.add("Shoots out orb which teleports you to location of contact");

        item.setType(Material.ENDER_PEARL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"End Warp");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(8, item);
        
        
//      BABY BOOMER
        lore.clear();
        lore.add(select);
        lore.add("Child Suicide Squad Member"); //todo do something better me

        item.setType(Material.GUNPOWDER);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Baby Boomer");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(9, item);
        
        
//      ZOMBIE SUMMON
        lore.clear();
        lore.add(select);
        lore.add("Summons a trio of zombie servants");

        item.setType(Material.ROTTEN_FLESH);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Zombie Summon");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(10, item);


//      SLIME BALL
        lore.clear();
        lore.add(select);
        lore.add("Shoots a ray that turns blocks into slime and causes knockback in the area");

        item.setType(Material.SLIME_BALL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Slime Ball");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(11, item);


//      FLASH STEP
        lore.clear();
        lore.add(select);
        lore.add("Teleports you in the direction you are facing");

        item.setType(Material.GLOWSTONE_DUST);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Flash Step");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(12, item);


//      SHOULDER BLITZ
        lore.clear();
        lore.add(select);
        lore.add("After charging up shoots you forward dealing damage to those in your path");

        item.setType(Material.IRON_CHESTPLATE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Shoulder Blitz");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(13, item);


//      ANVIL TOSS
        lore.clear();
        lore.add(select);
        lore.add("Teleports you in the direction you are facing");

        item.setType(Material.ANVIL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Shoulder Blitz");
        meta.setLore(lore);
        item.setItemMeta(meta);
        wandsInv.setItem(14, item);


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

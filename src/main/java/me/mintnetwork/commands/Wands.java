package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wands implements CommandExecutor {

    public static Inventory wandsInv;
    public static ArrayList<ItemMeta> genericWands = new ArrayList<>();

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
        String select = ChatColor.WHITE + "Click to select wand";

//      FIREWORK BOLT
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.FIREWORK_BOLT_COST);
        lore.add(ChatColor.GRAY + "Shoots an explosive ");
        lore.add(ChatColor.GRAY + "rocket towards your enemies");


        item.setType(Material.FIREWORK_ROCKET);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Firework Bolt");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(0, item);


//      JUMP BOOST
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.JUMP_BOOST_COST);
        lore.add(ChatColor.GRAY + "Enhances your jump and ");
        lore.add(ChatColor.GRAY + "slows your fall.");
        item.setType(Material.RABBIT_FOOT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Jump Boost");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(1, item);


//      ENGINE BLAST
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.ENGINE_BLAST_COST);
        lore.add(ChatColor.GRAY + "A blast of smoke propels");
        lore.add(ChatColor.GRAY + "you and your enemies away.");

        item.setType(Material.FIRE_CHARGE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Engine Blast");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(2, item);


//      DRAGON ORB
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.DRAGON_ORB_COST);
        lore.add(ChatColor.GRAY + "Shoot a timed grenade charged");
        lore.add(ChatColor.GRAY + "with a ender dragon's breath.");

        item.setType(Material.DRAGON_BREATH);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Dragon Orb");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(3, item);


//      BAT SONAR
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.BAT_SONAR_COST);
        lore.add(ChatColor.GRAY + "Sends out a bat which locates");
        lore.add(ChatColor.GRAY + "and marks your enemies.");

        item.setType(Material.SCULK_SENSOR);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Bat Sonar");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(4, item);


//      TNT RING
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.TNT_RING_COST);
        lore.add(ChatColor.GRAY + "Create a ring of TNT that");
        lore.add(ChatColor.GRAY + "detonate after a short time.");

        item.setType(Material.TNT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"TNT Ring");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(5, item);


//      HIVE BOLT
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.HIVE_BOLT_COST);
        lore.add(ChatColor.GRAY + "Shoots out angry bees which ");
        lore.add(ChatColor.GRAY + "attack anyone near. BEEware!");

        item.setType(Material.HONEYCOMB);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Hive Bolt");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(6, item);


        //      BLACK HOLE
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.BLACK_HOLE_COST);
        lore.add(ChatColor.GRAY + "Create a black hole which after ");
        lore.add(ChatColor.GRAY + "charging sucks in everything around it.");

        item.setType(Material.NETHER_STAR);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Black Hole");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(7, item);


//      END WARP
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.END_WARP_COST);
        lore.add(ChatColor.GRAY +"Shoots out orb which teleports ");
        lore.add(ChatColor.GRAY +"you to location of contact.");
        item.setType(Material.ENDER_PEARL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"End Warp");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(8, item);


//      BABY BOOMER
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.BABY_BOOMER_COST);
        lore.add(ChatColor.GRAY +"Summons a baby zombie with TNT "); //todo do something better me
        lore.add(ChatColor.GRAY +"that explodes after a few seconds.");

        item.setType(Material.GUNPOWDER);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Baby Boomer");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(9, item);


//      ZOMBIE SUMMON
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.ZOMBIE_SUMMON_COST);
        lore.add(ChatColor.GRAY +"Summons a trio of zombie");
        lore.add(ChatColor.GRAY +"servants to fight for you.");
        item.setType(Material.ROTTEN_FLESH);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Zombie Summon");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(10, item);


//      SLIME BALL
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SLIME_BALL_COST);
        lore.add(ChatColor.GRAY +"Shoots a slimeball that turns ");
        lore.add(ChatColor.GRAY +"blocks into slime and causes ");
        lore.add(ChatColor.GRAY + "knockback in the area");
        item.setType(Material.SLIME_BALL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Slime Ball");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(11, item);


//      FLASH STEP
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.FLASH_STEP_COST);
        lore.add(ChatColor.GRAY + "Teleports you in the ");
        lore.add(ChatColor.GRAY + "direction you are facing");

        item.setType(Material.GLOWSTONE_DUST);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Flash Step");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(12, item);


//      SHOULDER BLITZ
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SHOULDER_BLITZ_COST);
        lore.add(ChatColor.GRAY + "After preping for a moment ");
        lore.add(ChatColor.GRAY + "you charge forward.");
        item.setType(Material.IRON_CHESTPLATE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Shoulder Blitz");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(13, item);


//      ANVIL TOSS
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.ANVIL_TOSS_COST);
        lore.add(ChatColor.GRAY + "Throws an anvil that launches");
        lore.add(ChatColor.GRAY + "enemies around where it lands");


        item.setType(Material.ANVIL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Anvil Toss");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(14, item);


//      STORM STRIKE
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.STORM_STRIKE_COST);
        lore.add(ChatColor.GRAY + "Causes a lightning strike");
        lore.add(ChatColor.GRAY + "after a short charge.");
        item.setType(Material.LIGHTNING_ROD);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Storm Strike");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(15, item);


//      MANA BULLET
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.MANA_BULLET_COST);
        lore.add(ChatColor.GRAY + "Shoots a bolt which deals");
        lore.add(ChatColor.GRAY + "a small amount of damage.");

        item.setType(Material.EMERALD);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Mana Bullet");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(16, item);

        //      MANA BULLET
        lore.clear();
        lore.add(select);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.VOID_PILLAR_COST);
        lore.add(ChatColor.GRAY + "Creates a rift that sucks in");
        lore.add(ChatColor.GRAY +"harmful projectiles around it.");
        item.setType(Material.BLACK_CONCRETE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Void Rift");
        meta.setLore(lore);
        item.setItemMeta(meta);
        genericWands.add(meta.clone());
        wandsInv.setItem(17, item);



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

        for (int i = 0; i < genericWands.size(); i++) {
            for (ItemStack item: wizard.wands) {
                if (item.getItemMeta().getDisplayName().equals(genericWands.get(i).getDisplayName())){
                    ItemStack pane = inv.getItem(i);
                    pane.setType(Material.LIME_STAINED_GLASS_PANE);
                    ItemMeta meta = pane.getItemMeta();
                    meta.setLore(Arrays.asList(ChatColor.RED + "You have already selected this wand",ChatColor.WHITE + "Click to deselect"));
                    pane.setItemMeta(meta);
                    inv.setItem(i, pane);
                }
            }
        }


        return inv;

    }
}

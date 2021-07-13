package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.scoreboard.Team;

import java.util.*;


public class ClassSelect implements CommandExecutor {

    private final Main plugin;
    public static Inventory classInv;


    public ClassSelect(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("class").setExecutor(this);
    }

    @EventHandler()
    public boolean onClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(classInv))
            return false;
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot do this!");
            return true;
        }
        Player player = (Player) sender;

        player.openInventory(classEditInventory(player, classInv));

        return true;

    }

    public static Inventory createInfoMenu(Kit kit) {
        Inventory Inv = Bukkit.createInventory(null, 18, ChatColor.BOLD + ChatColor.stripColor(kit.KitItems.menuItem.getItemMeta().getDisplayName()));
        ItemStack tempItem = kit.KitItems.menuItem.clone();
        ItemMeta tempMeta = tempItem.getItemMeta();
        ArrayList<String> lore = new ArrayList<>(Collections.singletonList(ChatColor.WHITE + "Left Click to select class"));
        lore.addAll(tempMeta.getLore());
        tempMeta.setLore(lore);
        tempItem.setItemMeta(tempMeta);


        Inv.setItem(4,tempItem);
        Inv.setItem(11,kit.KitItems.ult);
        Inv.setItem(12,kit.KitItems.wands.get(0));
        Inv.setItem(13,kit.KitItems.wands.get(1));
        Inv.setItem(14,kit.KitItems.wands.get(2));
        Inv.setItem(15,kit.KitItems.passive);

        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Close Menu");
        item.setItemMeta(meta);
        Inv.setItem(8, item);

        return Inv;
    }


    public static void createClassMenu(){
        classInv = Bukkit.createInventory(null, 18, ChatColor.BOLD+"Select Class");

        for (int i = 0; i < Kit.values().length-1; i++) {
            Kit kit = Kit.values()[i];
            ItemStack tempItem = kit.KitItems.menuItem.clone();
            ItemMeta tempMeta = tempItem.getItemMeta();
            ArrayList<String> lore = new ArrayList<>(Arrays.asList(ChatColor.WHITE + "Left Click to select class", ChatColor.WHITE + "Right Click for more info"));
            lore.addAll(tempMeta.getLore());
            tempMeta.setLore(lore);
            tempItem.setItemMeta(tempMeta);
            classInv.setItem(i,tempItem);

        }

        // CLOSE 17
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Close Menu");
        item.setItemMeta(meta);
        classInv.setItem(17, item);

    }

    public static Inventory classEditInventory(Player player, Inventory invet){
        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.BOLD+"Select Class");
        inv.setContents(invet.getContents());

        Team team = TeamsInit.getTeam(player);

        //red pane
        ArrayList<Kit> takenKits = new ArrayList<Kit>();
        if(team != null) {
            Set<String> playerNames = team.getEntries();
            ArrayList<Player> players = new ArrayList<Player>();
            for (String playerName : playerNames) {
                if (Bukkit.getPlayer(playerName) != null) {
                    players.add(Bukkit.getPlayer(playerName));
                }
            }

            for (Player ply : players){
                takenKits.add(WizardInit.playersWizards.get(ply.getUniqueId()).kitID);
            }

        }

        //green pane
        Kit playerKit = WizardInit.playersWizards.get(player.getUniqueId()).kitID;


        ArrayList<Kit> kits = new ArrayList<>(Arrays.asList(Kit.values()));
        for (int i = 0; i < kits.size(); i++) {
            if (kits.get(i) != Kit.NONE) {
                if (playerKit == kits.get(i)) {
                    //green pane
                    ItemStack pane = inv.getItem(i);
                    pane.setType(Material.LIME_STAINED_GLASS_PANE);
                    ItemMeta meta = pane.getItemMeta();
                    meta.setLore(Collections.singletonList(ChatColor.RED + "You have already selected this class."));
                    pane.setItemMeta(meta);
                    inv.setItem(i, pane);
                } else if (takenKits.contains(kits.get(i))) {
                    //red pane
                    ItemStack pane = inv.getItem(i);
                    pane.setType(Material.RED_STAINED_GLASS_PANE);
                    ItemMeta meta = pane.getItemMeta();
                    meta.setLore(Collections.singletonList(ChatColor.RED + "A player on your team has already selected this class."));
                    pane.setItemMeta(meta);
                    inv.setItem(i, pane);
                }
            }
        }


        return inv;
    }
}

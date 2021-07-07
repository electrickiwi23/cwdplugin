package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static me.mintnetwork.commands.ChangeClass.classInv;

public class InventoryClickListener implements Listener {

    private final Main plugin;

    public InventoryClickListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler()
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BOLD + "Select Class")){
            event.setCancelled(true);
            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getItemMeta() == null) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            Player player = (Player) event.getWhoClicked();

            if (event.getSlot()!=8){

                ArrayList<ItemMeta> wandList = new ArrayList<>();

                ItemStack baseWand = new ItemStack(Material.STICK);
                ItemMeta meta = baseWand.getItemMeta();

                ItemStack ultWand = new ItemStack(Material.DIAMOND_HOE);
                ItemMeta ultMeta = ultWand.getItemMeta();

            }

            String[] slots ={
                    "alchemist", "bard", "berserker", "bloodmage", "builder", "cleric", "aviator", "demolitionist", "painter",
                    "pillarman", "shadow", "spellslinger", "tactician", "protector"
            };

            if(event.getSlot()<slots.length){
                player.performCommand("changeclass "+slots[event.getSlot()]);
                player.sendMessage(ChatColor.GOLD + "Class Selected");
            }
            player.closeInventory();
            return;
        }
    }
}
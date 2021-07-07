package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

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


            if (event.getCurrentItem().getType()==Material.LIME_STAINED_GLASS_PANE || event.getCurrentItem().getType()==Material.RED_STAINED_GLASS_PANE) {
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND,.5F,1);
                return;
            }

            String[] slots = {
                    "alchemist", "bard", "berserker", "bloodmage", "builder", "cleric", "aviator", "demolitionist", "painter",
                    "pillarman", "shadow", "spellslinger", "tactician", "protector"
            };

            if (event.getSlot() < slots.length) {
                player.performCommand("changeclass " + slots[event.getSlot()]);
                player.sendMessage(ChatColor.GOLD + "Class Selected");
            }
            player.closeInventory();
            return;
        }
        else if (event.getView().getTitle().equals(ChatColor.BOLD + "Select Wands")){
            event.setCancelled(true);
            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getItemMeta() == null) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            Player player = (Player) event.getWhoClicked();


            if (event.getCurrentItem().getType()==Material.LIME_STAINED_GLASS_PANE) {

                for (ItemStack item:WizardInit.playersWizards.get(player.getUniqueId()).wands) {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())){
                        player.getInventory().removeItem(item);
                        WizardInit.playersWizards.get(player.getUniqueId()).wands.remove(item);
                    }
                }

                event.getClickedInventory().setContents(Wands.wandsEditInventory(player,Wands.wandsInv).getContents());
                return;
            }

            String[] slots = {
                    "fireworkbolt", "jumpboost", "engineblast", "dragonorb", "batsonar", "tntring", "hivebolt", "blackhole", "endwarp",
                    "babyboomer", "zombiesummon", "slimeball", "flashstep", "shoulderblitz", "anviltoss", "stormstrike", "manabullet"
            };

            if (event.getSlot() < slots.length) {
                player.performCommand("givewands " + slots[event.getSlot()]);
                player.sendMessage(ChatColor.GOLD + "Wand Selected");
                event.getClickedInventory().setContents(Wands.wandsEditInventory(player,Wands.wandsInv).getContents());
            }else{
                player.closeInventory();
                return;
            }

        }
    }
}
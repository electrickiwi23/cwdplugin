package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.commands.ClassSelect;
import me.mintnetwork.commands.Wands;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
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
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Set;

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

                Team team = TeamsInit.getTeam(player);

                //red pane
                if(team != null) {
                    Set<String> playerNames = team.getEntries();
                    ArrayList<Player> players = new ArrayList<Player>();
                    for (String playerName : playerNames) {
                        if (Bukkit.getPlayer(playerName) != null) {
                            players.add(Bukkit.getPlayer(playerName));
                        }
                    }

                    for (Player ply : players){
                        if (ply.getOpenInventory().getTitle().equals(ChatColor.BOLD + "Select Class")){
                            ply.getOpenInventory().getTopInventory().setContents(ClassSelect.classEditInventory(ply,ClassSelect.classInv).getContents());
                        }
                    }

                }
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
                System.out.println("pane");

                for (ItemStack item:WizardInit.playersWizards.get(player.getUniqueId()).wands) {
                    System.out.println(item.getItemMeta().getDisplayName());
                    if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals(ChatColor.stripColor(item.getItemMeta().getDisplayName()))){
                        System.out.println("ooga");
                        player.getInventory().removeItem(item);
                        WizardInit.playersWizards.get(player.getUniqueId()).wands.remove(item);
                        event.getClickedInventory().setContents(Wands.wandsEditInventory(player,Wands.wandsInv).getContents());
                        return;
                    }
                }
                return;
            }

            String[] slots = {
                    "fireworkbolt", "jumpboost", "engineblast", "dragonorb", "batsonar", "tntring", "hivebolt", "blackhole", "endwarp",
                    "babyboomer", "zombiesummon", "slimeball", "flashstep", "shoulderblitz", "anviltoss", "stormstrike", "manabullet"
            };

            if (event.getSlot() < slots.length) {
                player.performCommand("givewands " + slots[event.getSlot()]);
                event.getClickedInventory().setContents(Wands.wandsEditInventory(player,Wands.wandsInv).getContents());
            }else{
                player.closeInventory();
                return;
            }

        }
    }
}
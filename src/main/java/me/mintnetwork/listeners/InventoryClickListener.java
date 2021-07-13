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
        Player player = (Player) event.getWhoClicked();

        String[] classSlots = {
                "alchemist", "bard", "berserker", "bloodmage", "builder", "cleric", "aviator", "demolitionist", "painter",
                "pillarman", "shadow", "spellslinger", "tactician", "protector"
        };

        if (event.getView().getTitle().equals(ChatColor.BOLD + "Select Class")) {
            event.setCancelled(true);
            if (event.getClickedInventory() == event.getView().getTopInventory()) {
                if (event.getCurrentItem() == null) return;
                if (event.getCurrentItem().getItemMeta() == null) return;
                if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;


                if (event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, .5F, 1);
                    return;
                }


                if (event.getSlot() < classSlots.length) {
                    if (event.getClick().isLeftClick()) {

                        player.performCommand("changeclass " + classSlots[event.getSlot()]);
                        player.sendMessage(ChatColor.GOLD + "Class Selected");

                        Team team = TeamsInit.getTeam(player);

                        //red pane
                        if (team != null) {
                            Set<String> playerNames = team.getEntries();
                            ArrayList<Player> players = new ArrayList<Player>();
                            for (String playerName : playerNames) {
                                if (Bukkit.getPlayer(playerName) != null) {
                                    players.add(Bukkit.getPlayer(playerName));
                                }
                            }

                            for (Player ply : players) {
                                if (event.getClickedInventory().equals(ChatColor.BOLD + "Select Class")) {
                                    event.getClickedInventory().setContents(ClassSelect.classEditInventory(ply, ClassSelect.classInv).getContents());
                                }
                            }

                        }

                    } else if (event.isRightClick()){
                        player.openInventory(ClassSelect.createInfoMenu(Kit.values()[event.getSlot()]));

                    }
                }
                if (!event.isRightClick()) player.closeInventory();
                return;
            }
        } else if (event.getView().getTitle().equals(ChatColor.BOLD + "Select Wands")) {
            event.setCancelled(true);
            if (event.getClickedInventory() == event.getView().getTopInventory()) {
                if (event.getCurrentItem() == null) return;
                if (event.getCurrentItem().getItemMeta() == null) return;
                if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;




                if (event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                    System.out.println("pane");

                    for (ItemStack item : WizardInit.playersWizards.get(player.getUniqueId()).wands) {
                        System.out.println(item.getItemMeta().getDisplayName());
                        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals(ChatColor.stripColor(item.getItemMeta().getDisplayName()))) {
                            System.out.println("ooga");
                            player.getInventory().removeItem(item);
                            WizardInit.playersWizards.get(player.getUniqueId()).wands.remove(item);
                            event.getClickedInventory().setContents(Wands.wandsEditInventory(player, Wands.wandsInv).getContents());
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
                    event.getClickedInventory().setContents(Wands.wandsEditInventory(player, Wands.wandsInv).getContents());
                } else {
                    player.closeInventory();
                    return;
                }

            }
        } else {
            for (int i = 0; i < Kit.values().length; i++) {
                Kit kit = Kit.values()[i];
                if (kit==Kit.NONE) return;
                if (kit.KitItems.menuItem.getType()==Material.FIRE) return;
                if (event.getView().getTitle().equals(ChatColor.BOLD + ChatColor.stripColor(kit.KitItems.menuItem.getItemMeta().getDisplayName()))){
                    event.setCancelled(true);
                    if (event.getClickedInventory() == event.getView().getTopInventory()) {
                        if (event.getCurrentItem() == null) return;
                        if (event.getCurrentItem().getItemMeta() == null) return;
                        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                        if (event.getSlot()==4){
                             player.performCommand("changeclass " + classSlots[i]);
                             player.sendMessage(ChatColor.GOLD + "Class Selected");

                             Team team = TeamsInit.getTeam(player);

                             //red pane
                             if (team != null) {
                                 Set<String> playerNames = team.getEntries();
                                 ArrayList<Player> players = new ArrayList<Player>();
                                 for (String playerName : playerNames) {
                                     if (Bukkit.getPlayer(playerName) != null) {
                                         players.add(Bukkit.getPlayer(playerName));
                                     }
                                 }

                                 for (Player ply : players) {
                                     if (event.getClickedInventory().equals(ChatColor.BOLD + "Select Class")) {
                                         event.getClickedInventory().setContents(ClassSelect.classEditInventory(ply, ClassSelect.classInv).getContents());
                                     }
                                 }

                             }
                            player.closeInventory();
                             break;
                         }
                        if (event.getSlot()==8){
                            player.openInventory(ClassSelect.classEditInventory(player, ClassSelect.classInv));
                            break;

                        }
                    }
                }
            }

        }
    }
}
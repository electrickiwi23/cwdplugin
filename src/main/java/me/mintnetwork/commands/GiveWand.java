package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.utils.Utils;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GiveWand implements CommandExecutor {

    private final Main plugin;

    public GiveWand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("givewands").setExecutor(this);
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
//        if (p.hasPermission("cwd.cangivewands")) {
            if (wizard.wands.size()>=3){
                p.sendMessage("You have already chosen 3 spells");
                return false;
            }

            if (args.length==1) {

                    ItemStack baseWand = new ItemStack(Material.STICK);
                    ItemMeta meta = baseWand.getItemMeta();

                        switch (args[0].toLowerCase()) {
                            case ("fireworkbolt"):
                                meta.setDisplayName(ChatColor.RESET + ("Firework Bolt"));
                                break;
                            case ("jumpboost"):
                                meta.setDisplayName(ChatColor.RESET + ("Jump Boost"));
                                break;
                            case ("engineblast"):
                                meta.setDisplayName(ChatColor.RESET + ("Engine Blast"));
                                break;
                            case ("dragonorb"):
                                meta.setDisplayName(ChatColor.RESET + ("Dragon Orb"));
                                break;
                            case ("batsonar"):
                                meta.setDisplayName(ChatColor.RESET + ("Bat Sonar"));;
                                break;
                            case ("tntring"):
                                meta.setDisplayName(ChatColor.RESET + ("TNT Ring"));
                                break;
                            case ("hivebolt"):
                                meta.setDisplayName(ChatColor.RESET + ("Hive Bolt"));
                                break;
                            case ("blackhole"):
                                meta.setDisplayName(ChatColor.RESET + ("Black Hole"));
                                break;
                            case ("endwarp"):
                                meta.setDisplayName(ChatColor.RESET + ("End Warp"));
                                break;
                            case ("babyboomer"):
                                meta.setDisplayName(ChatColor.RESET + ("Baby Boomer"));
                                break;
                            case ("zombiesummon"):
                                meta.setDisplayName(ChatColor.RESET + ("Zombie Summon"));
                                break;
                            case ("slimeball"):
                                meta.setDisplayName(ChatColor.RESET + ("Slime Ball"));
                                break;
                            case ("flashstep"):
                                meta.setDisplayName(ChatColor.RESET + ("Flash Step"));
                                break;
                            case ("shoulderblitz"):
                                meta.setDisplayName(ChatColor.RESET + ("Shoulder Blitz"));
                                break;
                            case ("anviltoss"):
                                meta.setDisplayName(ChatColor.RESET + ("Anvil Toss"));
                                break;
                            case ("stormstrike"):
                                meta.setDisplayName(ChatColor.RESET + ("Storm Strike"));
                                break;
                            case("manabullet"):
                                meta.setDisplayName(ChatColor.RESET + ("Mana Bullet"));
                                break;


                        }
                        baseWand.setItemMeta(meta);
                        p.getInventory().addItem(baseWand);

                        wizard.wands.add(baseWand);
                } else{
                    p.sendMessage(ChatColor.RED+("You must select a spell."));
                }
//        }
        return false;
    }

}

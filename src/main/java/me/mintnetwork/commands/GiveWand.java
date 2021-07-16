package me.mintnetwork.commands;

import me.mintnetwork.Main;
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
                                meta = Wands.genericWands.get(0);
                                break;
                            case ("jumpboost"):
                                meta = Wands.genericWands.get(1);
                                break;
                            case ("engineblast"):
                                meta = Wands.genericWands.get(2);
                            break;
                            case ("dragonorb"):
                                meta = Wands.genericWands.get(3);
                            break;
                            case ("batsonar"):
                                meta = Wands.genericWands.get(4);
                            break;
                            case ("tntring"):
                                meta = Wands.genericWands.get(5);
                            break;
                            case ("hivebolt"):
                                meta = Wands.genericWands.get(6);
                            break;
                            case ("blackhole"):
                                meta = Wands.genericWands.get(7);
                            break;
                            case ("endwarp"):
                                meta = Wands.genericWands.get(8);
                            break;
                            case ("babyboomer"):
                                meta = Wands.genericWands.get(9);
                            break;
                            case ("zombiesummon"):
                                meta = Wands.genericWands.get(10);
                            break;
                            case ("slimeball"):
                                meta = Wands.genericWands.get(11);
                            break;
                            case ("flashstep"):
                                meta = Wands.genericWands.get(12);
                            break;
                            case ("shoulderblitz"):
                                meta = Wands.genericWands.get(13);
                            break;
                            case ("anviltoss"):
                                meta = Wands.genericWands.get(14);
                            break;
                            case ("stormstrike"):
                                meta = Wands.genericWands.get(15);
                            break;
                            case("manabullet"):
                                meta = Wands.genericWands.get(16);
                            break;
                            case("voidpillar"):
                                meta = Wands.genericWands.get(17);
                            break;


                        }
                        baseWand.setItemMeta(meta);
                        p.getInventory().addItem(baseWand);

                        wizard.wands.add(baseWand);
                } else {
                p.sendMessage(ChatColor.RED + ("You must select a spell."));
            }
//        }
        return false;
    }

}

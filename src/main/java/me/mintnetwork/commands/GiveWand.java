package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.utils.Utils;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
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
            if (wizard.wands.size()>3){
                p.sendMessage("You have already chosen 3 spells");
                return false;
            }

            if (args.length==1) {

                    ItemStack baseWand = new ItemStack(Material.STICK);
                    ItemMeta meta = baseWand.getItemMeta();

                        switch (args[0].toLowerCase()) {
                            case ("fireworkbolt"):
                                meta.setDisplayName(Utils.chat("&rFirework Bolt"));
                                break;
                            case ("jumpboost"):
                                meta.setDisplayName(Utils.chat("&rJump Boost"));
                                break;
                            case ("engineblast"):
                                meta.setDisplayName(Utils.chat("&rEngine Blast"));
                                break;
                            case ("dragonorb"):
                                meta.setDisplayName(Utils.chat("&rDragon Orb"));
                                break;
                            case ("batsonar"):
                                meta.setDisplayName(Utils.chat("&rBat Sonar"));;
                                break;
                            case ("tntring"):
                                meta.setDisplayName(Utils.chat("&rTNT Ring"));
                                break;
                            case ("hivebolt"):
                                meta.setDisplayName(Utils.chat("&rHive Bolt"));
                                break;
                            case ("blackhole"):
                                meta.setDisplayName(Utils.chat("&rBlack Hole"));
                                break;
                            case ("endwarp"):
                                meta.setDisplayName(Utils.chat("&rEnd Warp"));
                                break;
                            case ("babyboomer"):
                                meta.setDisplayName(Utils.chat("&rBaby Boomer"));
                                break;
                            case ("zombiesummon"):
                                meta.setDisplayName(Utils.chat("&rZombie Summon"));
                                break;
                            case ("slimeball"):
                                meta.setDisplayName(Utils.chat("&rSlime Ball"));
                                break;
                            case ("flashstep"):
                                meta.setDisplayName(Utils.chat("&rFlash Step"));
                                break;
                            case ("shoulderblitz"):
                                meta.setDisplayName(Utils.chat("&rShoulder Blitz"));
                                break;
                            case ("anviltoss"):
                                meta.setDisplayName(Utils.chat("&rAnvil Toss"));
                                break;
                            case ("stormstrike"):
                                meta.setDisplayName(Utils.chat("&rStorm Strike"));
                                break;
                            case("manabullet"):
                                meta.setDisplayName(Utils.chat("&rMana Bullet"));
                                break;


                        }
                        baseWand.setItemMeta(meta);
                        p.getInventory().addItem(baseWand);

                        wizard.wands.add(baseWand);
                } else{
                    p.sendMessage(Utils.chat("&cYou must select a spell."));
                }
//        }
        return false;
    }

}

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
//        if (p.hasPermission("cwd.cangivewands")) {
            if (args.length==3) {
                if (!(args[0].equals(args[1])||args[1].equals(args[2])||args[0].equals(args[2]))) {


                    ArrayList<ItemMeta> wandList = new ArrayList<>();

                    ItemStack baseWand = new ItemStack(Material.STICK);
                    ItemMeta meta = baseWand.getItemMeta();

                    for (int i = 0; i < 3; i++) {
                        switch (args[i].toLowerCase()) {
                            case ("fireworkbolt"):
                                meta.setDisplayName(Utils.chat("&rFirework Bolt"));
                                wandList.add(meta.clone());
                                break;
                            case ("jumpboost"):
                                meta.setDisplayName(Utils.chat("&rJump Boost"));
                                wandList.add(meta.clone());
                                break;
                            case ("engineblast"):
                                meta.setDisplayName(Utils.chat("&rEngine Blast"));
                                wandList.add(meta.clone());
                                break;
                            case ("dragonorb"):
                                meta.setDisplayName(Utils.chat("&rDragon Orb"));
                                wandList.add(meta.clone());
                                break;
                            case ("batsonar"):
                                meta.setDisplayName(Utils.chat("&rBat Sonar"));
                                wandList.add(meta.clone());
                                break;
                            case ("tntring"):
                                meta.setDisplayName(Utils.chat("&rTNT Ring"));
                                wandList.add(meta.clone());
                                break;
                            case ("hivebolt"):
                                meta.setDisplayName(Utils.chat("&rHive Bolt"));
                                wandList.add(meta.clone());
                                break;
                            case ("blackhole"):
                                meta.setDisplayName(Utils.chat("&rBlack Hole"));
                                wandList.add(meta.clone());
                                break;
                            case ("endwarp"):
                                meta.setDisplayName(Utils.chat("&rEnd Warp"));
                                wandList.add(meta.clone());
                                break;
                            case ("babyboomer"):
                                meta.setDisplayName(Utils.chat("&rBaby Boomer"));
                                wandList.add(meta.clone());
                                break;
                            case ("zombiesummon"):
                                meta.setDisplayName(Utils.chat("&rZombie Summon"));
                                wandList.add(meta.clone());
                                break;
                            case ("slimeball"):
                                meta.setDisplayName(Utils.chat("&rSlime Ball"));
                                wandList.add(meta.clone());
                                break;
                            case ("flashstep"):
                                meta.setDisplayName(Utils.chat("&rFlash Step"));
                                wandList.add(meta.clone());
                                break;
                            case ("shoulderblitz"):
                                meta.setDisplayName(Utils.chat("&rShoulder Blitz"));
                                wandList.add(meta.clone());
                                break;
                            case ("anviltoss"):
                                meta.setDisplayName(Utils.chat("&rAnvil Toss"));
                                wandList.add(meta.clone());
                                break;
                            case ("stormstrike"):
                                meta.setDisplayName(Utils.chat("&rStorm Strike"));
                                wandList.add(meta.clone());
                                break;
                            case("manabullet"):
                                meta.setDisplayName(Utils.chat("&rMana Bullet"));
                                wandList.add(meta.clone());
                                break;


                        }
                    }
                    if (wandList.size()==3){
                        for (ItemMeta m:wandList) {
                            baseWand.setItemMeta(m);
                            p.getInventory().addItem(baseWand);

                        }
                    } else{
                        p.sendMessage(Utils.chat("&cOne or more of the selected spells are not valid."));
                    }
                } else{
                    p.sendMessage(Utils.chat("&cThe spells you select must be unique."));
                }
            } else{
                p.sendMessage(Utils.chat("&cYou must select 3 spells."));
            }
//        }
        return false;
    }

}

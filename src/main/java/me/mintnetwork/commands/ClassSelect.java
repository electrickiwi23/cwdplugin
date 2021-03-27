package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.utils.Utils;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ClassSelect implements CommandExecutor {

    private final Main plugin;

    public ClassSelect(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("class").setExecutor(this);
    }

    public static void AutoFill(Main plugin){
        plugin.getCommand("class").setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                return null;
            }
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }

        Player p = (Player) sender;

        ArrayList<ItemMeta> wandList = new ArrayList<>();

        ItemStack baseWand = new ItemStack(Material.STICK);
        ItemMeta meta = baseWand.getItemMeta();

        ItemStack ultWand = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta ultMeta = ultWand.getItemMeta();


        Wizard wizard = WizardInit.playersWizards.get(sender);
        if (args.length>0) {
            switch (args[0].toLowerCase()) {
                case ("alchemist"):
                    meta.setDisplayName(Utils.chat("&rAcid Vial"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rHealing Potion"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPlague Potion"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rElixir of Immortality"));
                    wizard.ClassID = "alchemist";
                    break;
                case ("bard"):
                    meta.setDisplayName(Utils.chat("&rSong of Healing"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rSong of Haste"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rConcussion Ring"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rSiren Song"));
                    wizard.ClassID = "bard";
                    break;
                case ("berserker"):
                    meta.setDisplayName(Utils.chat("&rSpeed Rush"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rForce Pull"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rUnleash Rage"));
                    wizard.ClassID = "berserker";
                    break;
                case ("bloodmage"):
                    meta.setDisplayName(Utils.chat("&rBlood Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rLife Sacrifice"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rBlood Tracker"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rLife Drain"));
                    wizard.ClassID = "blood mage";
                    break;
                case ("builder"):
                    meta.setDisplayName(Utils.chat("&rQuick Build"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPop up Tower"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rBridge Bolt"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rNetherite Wall"));
                    wizard.ClassID = "builder";
                    break;
                case ("cleric"):
                    meta.setDisplayName(Utils.chat("&rHeal Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rHealing Pillar"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPurification Wave"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rDivine Intervention"));
                    wizard.ClassID = "cleric";
                    break;
                case ("skyflyer"):
                    meta.setDisplayName(Utils.chat("&rCloud Burst"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rAir Dash"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rAir Needles"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rTornado Blast"));
                    wizard.ClassID = "sky flyer";
                    break;
                case ("demolitionist"):
                    meta.setDisplayName(Utils.chat("&rTNT Line"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rTNT Grenade"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rSticky Grenade"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rCluster Bomb"));
                    wizard.ClassID = "demolitionist";
                    break;
                case ("painter"):
                    meta.setDisplayName(Utils.chat("&rSpray Paint"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPaint Canister"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rReveal Paint"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rPaint Activate"));
                    wizard.ClassID = "painter";
                    break;
                case ("pillarman"):
                    meta.setDisplayName(Utils.chat("&rVoid Rift"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rBeam Pillar"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rMana Pillar"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rSkeleton Spirit"));
                    wizard.ClassID = "pillar man";
                    break;
                case ("shadow"):
                    meta.setDisplayName(Utils.chat("&rDarkness Camouflage"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rShadow Escape"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPray Abduction"));
                    wandList.add(meta.clone());
                    p.setCustomNameVisible(false);
                    ultMeta.setDisplayName(Utils.chat("&rConsuming Midnight"));
                    wizard.ClassID = "shadow";
                    break;
                case ("spellslinger"):
                    meta.setDisplayName(Utils.chat("&rLightning Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rFire Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rIce Bolt"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rElemental Blast"));
                    wizard.ClassID = "spell slinger";
                    break;
                case ("tactician"):
                    meta.setDisplayName(Utils.chat("&rSniper Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rMolotov Cocktail"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rGrapple Hook"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rAir Strike"));
                    wizard.ClassID = "tactician";
                    break;
            }
            if (wandList.isEmpty()) {
                sender.sendMessage(args[0] + " is not a valid Class");
            } else {
                if (!wizard.ClassID.equals("shadow")) p.setCustomNameVisible(true);

                ItemStack sword = new ItemStack(Material.STONE_SWORD);
                ItemMeta swordMeta = sword.getItemMeta();
                if (wizard.ClassID.equals("berserker")){
                    sword.setType(Material.IRON_SWORD);
                    swordMeta.setDisplayName((Utils.chat("&rBerserker Blade")));
                }
                swordMeta.setUnbreakable(true);
                sword.setItemMeta(swordMeta);
                p.getInventory().addItem(sword);

                for (ItemMeta i : wandList) {
                    ItemStack wand = new ItemStack(Material.STICK);
                    wand.setItemMeta(i);
                    p.getInventory().addItem(wand);
                }

                ultWand.setItemMeta(ultMeta);
                p.getInventory().addItem(ultWand);

                ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
                LeatherArmorMeta armorMeta = (LeatherArmorMeta) helm.getItemMeta();

                String TeamName = TeamsInit.getTeamName(p);

                    switch (TeamName) {
                        case "blue":
                            armorMeta.setColor(Color.fromRGB(60, 68, 170));
                            break;
                        case "red":
                            armorMeta.setColor(Color.fromRGB(176, 46, 38));
                            break;
                        case "green":
                            armorMeta.setColor(Color.fromRGB(128, 199, 31));
                            break;
                        case "yellow":
                            armorMeta.setColor(Color.fromRGB(120, 120, 2));
                            break;
                    }

                if (wizard.ClassID.equals("demolitionist")){
                    armorMeta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS,1,true);
                }

                armorMeta.setUnbreakable(true);

                helm.setItemMeta(armorMeta);
                ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
                chest.setItemMeta(armorMeta);
                ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
                legs.setItemMeta(armorMeta);
                ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                boots.setItemMeta(armorMeta);

                ItemStack[] armor = {boots,legs,chest,helm};

                p.getInventory().setArmorContents(armor);

            }
        }else{
            sender.sendMessage("You must input a class.");
        }

        return false;
    }
}


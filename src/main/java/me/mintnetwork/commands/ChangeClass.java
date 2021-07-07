package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.utils.Utils;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChangeClass implements CommandExecutor {

    private final Main plugin;

    public ChangeClass(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("changeclass").setExecutor(this);
    }

    public static void AutoFill(Main plugin){
        plugin.getCommand("changeclass").setTabCompleter(new TabCompleter() {
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


        Wizard wizard = WizardInit.playersWizards.get(((Player) sender).getUniqueId());
        if (args.length>0) {
            switch (args[0].toLowerCase()) {
                case "alchemist":
                    meta.setDisplayName(Utils.chat("&rAcid Vial"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rHealing Potion"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPlague Potion"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rElixir of Immortality"));
                    wizard.kitID = Kit.ALCHEMIST;
                    break;
                case ("bard"):
                    meta.setDisplayName(Utils.chat("&rSong of Healing"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rSong of Haste"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rConcussion Ring"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rSiren Song"));
                    wizard.kitID = Kit.BARD;
                    break;
                case ("berserker"):
                    meta.setDisplayName(Utils.chat("&rSpeed Rush"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rForce Pull"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rUnleash Rage"));
                    wizard.kitID = Kit.BERSERKER;
                    break;
                case ("bloodmage"):
                    meta.setDisplayName(Utils.chat("&rBlood Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rLife Sacrifice"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rBlood Tracker"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rBlood Link Ritual"));
                    wizard.kitID = Kit.BLOOD_MAGE;
                    StatusEffects.bloodLink.put(p,new HashMap<>());
                    break;
                case ("builder"):
                    meta.setDisplayName(Utils.chat("&rQuick Build"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPop up Tower"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rBridge Bolt"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rNetherite Wall"));
                    wizard.kitID = Kit.BUILDER;
                    break;
                case ("cleric"):
                    meta.setDisplayName(Utils.chat("&rHeal Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rHealing Pillar"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPurification Wave"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rDivine Intervention"));
                    wizard.kitID = Kit.CLERIC;
                    break;
                case ("aviator"):
                    meta.setDisplayName(Utils.chat("&rCloud Burst"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rAir Dash"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rAir Needles"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rTornado Blast"));
                    wizard.kitID = Kit.AVIATOR;
                    break;
                case ("demolitionist"):
                    meta.setDisplayName(Utils.chat("&rTNT Line"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rTNT Grenade"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rSticky Grenade"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rCluster Bomb"));
                    wizard.kitID = Kit.DEMOLITIONIST;
                    break;
                case ("painter"):
                    meta.setDisplayName(Utils.chat("&rSpray Paint"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPaint Canister"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rBrush Stroke"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rPaint Activate"));
                    wizard.kitID = Kit.PAINTER;
                    break;
                case ("pillarman"):
                    meta.setDisplayName(Utils.chat("&rVoid Rift"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rBeam Pillar"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rMana Pillar"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rSkeleton Spirit"));
                    wizard.kitID = Kit.PILLAR_MAN;
                    break;
                case ("shadow"):
                    meta.setDisplayName(Utils.chat("&rDarkness Camouflage"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rShadow Dash"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rPray Abduction"));
                    wandList.add(meta.clone());
                    p.setCustomNameVisible(false);
                    ultMeta.setDisplayName(Utils.chat("&rConsuming Midnight"));
                    wizard.kitID = Kit.SHADOW;
                    break;
                case ("spellslinger"):
                    meta.setDisplayName(Utils.chat("&rLightning Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rFire Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rIce Bolt"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rElemental Blast"));
                    wizard.kitID = Kit.SPELL_SLINGER;
                    break;
                case ("tactician"):
                    meta.setDisplayName(Utils.chat("&rSniper Bolt"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rMolotov Cocktail"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rGrapple Hook"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rAir Strike"));
                    wizard.kitID = Kit.TACTICIAN;
                    break;
                case ("protector"):
                    meta.setDisplayName(Utils.chat("&rArial Slam"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rDome of Safety"));
                    wandList.add(meta.clone());
                    meta.setDisplayName(Utils.chat("&rArmor Aura"));
                    wandList.add(meta.clone());
                    ultMeta.setDisplayName(Utils.chat("&rAura of Protection"));
                    wizard.kitID = Kit.PROTECTOR;
                    break;
            }
            if (wandList.isEmpty()) {
                sender.sendMessage(args[0] + " is not a valid Class");
            } else {
                boolean uniqueClass = true;
                for (UUID uuid: WizardInit.playersWizards.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    Wizard tempWizard = WizardInit.playersWizards.get(uuid);
                    if (tempWizard.kitID!=null) {
                        if (tempWizard.kitID.equals(wizard.kitID) && TeamsInit.getTeamName(p).matches(TeamsInit.getTeamName(player)) && player != p) {
                            uniqueClass = false;
                        }
                    }
                }

                if (uniqueClass) {
                    p.getInventory().clear();

                    wizard.wands.clear();

                    p.setCustomNameVisible(!wizard.kitID.equals(Kit.SHADOW));

                    if (wizard.kitID.equals(Kit.PROTECTOR)){
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24);
                        p.setHealth(24);
                        p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(new AttributeModifier("ProtectorSlow",p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue()*-.15,AttributeModifier.Operation.ADD_NUMBER));
                    } else {
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                        p.setHealth(20);
                        for (AttributeModifier modifier : p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getModifiers()) {
                            if (modifier.getName().equals("ProtectorSlow")) {

                                p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
                            }
                        }
                    }

                    ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
                    ItemMeta swordMeta = sword.getItemMeta();
                    if (wizard.kitID.equals(Kit.BERSERKER)) {
                        sword.setType(Material.STONE_SWORD);
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

                    if (wizard.kitID.equals(Kit.DEMOLITIONIST)) {
                        armorMeta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true);
                    }

                    armorMeta.setUnbreakable(true);

                    helm.setItemMeta(armorMeta);
                    ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
                    chest.setItemMeta(armorMeta);
                    ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
                    legs.setItemMeta(armorMeta);
                    ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                    boots.setItemMeta(armorMeta);

                    ItemStack[] armor = {boots, legs, chest, helm};

                    p.getInventory().setArmorContents(armor);

                }else{
                    sender.sendMessage("A player on your team already has chosen that class");
                }
            }
        }else{
            sender.sendMessage("You must input a class.");
        }

        return false;
    }
}


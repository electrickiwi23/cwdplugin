package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.utils.Utils;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.potion.PotionEffectType;

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


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }

        Player p = (Player) sender;
        if (args.length>0) {
            Kit kit = Kit.NONE;
            switch (args[0].toLowerCase()) {
                case "alchemist":
                    kit = Kit.ALCHEMIST;
                    break;
                case ("bard"):
                    kit = Kit.BARD;
                    break;
                case ("berserker"):
                    kit = Kit.BERSERKER;
                    break;
                case ("bloodmage"):
                    kit = Kit.BLOOD_MAGE;
                    break;
                case ("builder"):
                    kit = Kit.BUILDER;
                    break;
                case ("cleric"):
                    kit = Kit.CLERIC;
                    break;
                case ("aviator"):
                    kit = Kit.AVIATOR;
                    break;
                case ("demolitionist"):
                    kit = Kit.DEMOLITIONIST;
                    break;
                case ("painter"):
                    kit = Kit.PAINTER;
                    break;
                case ("pillarman"):
                    kit = Kit.PILLAR_MAN;
                    break;
                case ("shadow"):
                    kit = Kit.SHADOW;
                    break;
                case ("spellslinger"):
                    kit = Kit.SPELL_SLINGER;
                    break;
                case ("tactician"):
                    kit = Kit.TACTICIAN;
                    break;
                case ("protector"):
                    kit = Kit.PROTECTOR;
                    break;
            }
            if (kit == Kit.NONE) {
                sender.sendMessage(args[0] + " is not a valid Class");
            } else {
                ChangeKit(kit,p);
            }
        }else{
            sender.sendMessage("You must input a class.");
        }
        return false;
    }

    public static void ChangeKit(Kit kit, Player p){
        boolean uniqueClass = true;
        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());

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

        wizard.kitID = kit;

        wizard.wands.clear();

        p.setCustomNameVisible(kit!=Kit.SHADOW);
        StatusEffects.cloudFloating.remove(p);
        p.removePotionEffect(PotionEffectType.SLOW_FALLING);

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
            swordMeta.setDisplayName((ChatColor.RESET+("Berserker Blade")));
        }
        swordMeta.setUnbreakable(true);
        sword.setItemMeta(swordMeta);
        p.getInventory().addItem(sword);

        for (ItemStack item:kit.KitItems.wands) {
            if (item.getType()==Material.STICK)  p.getInventory().addItem(item);
        }

        p.getInventory().addItem(kit.KitItems.ult);

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
            p.sendMessage(ChatColor.RED + "A player on your team already has chosen that class");
        }
    }
}


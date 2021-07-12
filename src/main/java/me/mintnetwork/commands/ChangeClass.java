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

        ArrayList<ItemMeta> wandList = new ArrayList<>();

        ItemStack baseWand = new ItemStack(Material.STICK);
        ItemMeta meta = baseWand.getItemMeta();

        ItemStack ultWand = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta ultMeta = ultWand.getItemMeta();

        ArrayList<ItemStack> give = new ArrayList<ItemStack>();

        List<String> lore = new ArrayList<String>();
        lore.clear();

        Wizard wizard = WizardInit.playersWizards.get(((Player) sender).getUniqueId());
        if (args.length>0) {
            boolean selected = false;
            switch (args[0].toLowerCase()) {
                case "alchemist":
                    wizard.kitID = Kit.ALCHEMIST;
                    give.addAll(Kit.ALCHEMIST.KitItems.wands);
                    give.add(Kit.ALCHEMIST.KitItems.ult);
                    selected = true;
                    break;
                case ("bard"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Song of Healing"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Song of Haste"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Concussion Ring"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Siren Song"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.BARD;
                    selected = true;
                    break;
                case ("berserker"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Speed Rush"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Force Pull"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Unleash Rage"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.BERSERKER;
                    selected = true;
                    break;
                case ("bloodmage"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Blood Bolt"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Life Sacrifice"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Blood Tracker"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET + ("Blood Link Ritual"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.BLOOD_MAGE;
                    StatusEffects.bloodLink.put(p,new HashMap<>());
                    selected = true;
                    break;
                case ("builder"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Quick Build"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Pop up Tower"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Bridge Bolt"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Netherite Wall"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.BUILDER;
                    selected = true;
                    break;
                case ("cleric"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Heal Bolt"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Healing Pillar"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Purification Wave"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Divine Intervention"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.CLERIC;
                    selected = true;
                    break;
                case ("aviator"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Cloud Burst"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Air Dash"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Air Needles"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Tornado Blast"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.AVIATOR;
                    selected = true;
                    break;
                case ("demolitionist"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("TNT Line"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("TNT Grenade"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Sticky Grenade"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Cluster Bomb"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.DEMOLITIONIST;
                    selected = true;
                    break;
                case ("painter"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Spray Paint"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Paint Canister"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Brush Stroke"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Paint Activate"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.PAINTER;
                    selected = true;
                    break;
                case ("pillarman"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Void Rift"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Beam Pillar"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Mana Pillar"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Skeleton Spirit"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.PILLAR_MAN;
                    selected = true;
                    break;
                case ("shadow"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Darkness Camouflage"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Shadow Dash"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Pray Abduction"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    p.setCustomNameVisible(false);
                    ultMeta.setDisplayName(ChatColor.RESET+("Consuming Midnight"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.SHADOW;
                    selected = true;
                    break;
                case ("spellslinger"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Lightning Bolt"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Fire Bolt"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Ice Bolt"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Elemental Blast"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.SPELL_SLINGER;
                    selected = true;
                    break;
                case ("tactician"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Sniper Bolt"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Molotov Cocktail"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Grapple Hook"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Air Strike"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.TACTICIAN;
                    selected = true;
                    break;
                case ("protector"):
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Arial Slam"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Dome of Safety"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add(ChatColor.GREEN + "Mana Cost: " + "#");
                    lore.add("TEXT");
                    meta.setDisplayName(ChatColor.RESET+("Armor Aura"));
                    meta.setLore(lore);
                    lore.clear();
                    wandList.add(meta.clone());
                    lore.add("TEXT");
                    ultMeta.setDisplayName(ChatColor.RESET+("Aura of Protection"));
                    meta.setLore(lore);
                    lore.clear();
                    wizard.kitID = Kit.PROTECTOR;
                    selected = true;
                    break;
            }
            if (!selected) {
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
                        swordMeta.setDisplayName((ChatColor.RESET+("Berserker Blade")));
                    }
                    swordMeta.setUnbreakable(true);
                    sword.setItemMeta(swordMeta);
                    p.getInventory().addItem(sword);

                    for (ItemStack stack:give) {
                        p.getInventory().addItem(stack);
                    }

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


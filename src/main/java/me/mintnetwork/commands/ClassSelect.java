package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.scoreboard.Team;

import java.util.*;


public class ClassSelect implements CommandExecutor {

    private final Main plugin;
    public static Inventory classInv;


    public ClassSelect(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("class").setExecutor(this);
    }

    @EventHandler()
    public boolean onClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(classInv))
            return false;
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot do this!");
            return true;
        }
        Player player = (Player) sender;

        player.openInventory(classEditInventory(player, classInv));

        return true;

    }

    public static void createClassMenu(){
        classInv = Bukkit.createInventory(null, 18, ChatColor.BOLD+"Select Class");

        ItemStack item;
        ItemMeta meta;
        List<String> lore = new ArrayList<String>();
        String select = ChatColor.GRAY + "Click to select class";


        //  alchemist 0
        lore.clear();
        lore.add(select);
        lore.add("Use powerful area of effect");
        lore.add("spells to control the battlefield");
        lore.add("and help your teammates.");

        item = new ItemStack(Material.SPLASH_POTION);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setDisplayName(ChatColor.DARK_GREEN+"Alchemist");
        meta.setLore(lore);

        PotionMeta potionMeta = (PotionMeta) meta;
        potionMeta.setColor(Color.GREEN);
        item.setItemMeta(potionMeta);
        classInv.setItem(0, item);


        //  bard 1
        lore.clear();
        lore.add(select);
        lore.add("Use the power of music to heal");
        lore.add("your allies and ward off enemies.");

        item.setType(Material.MUSIC_DISC_CAT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA+"Bard");
        meta.addItemFlags(ItemFlag.values());
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(1, item);


        //  berserker 2
        lore.clear();
        lore.add(select);
        lore.add("Chase down and stab your enemies");
        lore.add("with your increased close range capabilities.");

        item.setType(Material.STONE_SWORD);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED+"Berserker");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(2, item);


        //  bloodmage 3
        lore.clear();
        lore.add(select);
        lore.add("Excel in 1v1 and team fights with lifesteal");
        lore.add("and abilities focused on survivability.");

        item.setType(Material.REDSTONE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED+"Blood Mage");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(3, item);


        //  builder 4
        lore.clear();
        lore.add(select);
        lore.add("Dominate the terrain by shaping the");
        lore.add("environment to your teams advantage.");

        item.setType(Material.WHITE_WOOL);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW+"Builder");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(4, item);


        //  cleric 5
        lore.clear();
        lore.add(select);
        lore.add("Be the ultimate support by providing");
        lore.add("healing and cleansing your teammates.");

        item.setType(Material.GOLDEN_APPLE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE+"Cleric");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(5, item);


        //  skyflyer 6
        lore.clear();
        lore.add(select);
        lore.add("Dominate the skies with enhanced");
        lore.add("movement and the power of flight.");

        item.setType(Material.FEATHER);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA+"Aviator");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(6, item);


        //  demolitionist 7
        lore.clear();
        lore.add(select);
        lore.add("Exterminate your enemies with large");
        lore.add("area of effect explosions");

        item.setType(Material.TNT);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED+"Demolitionist");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(7, item);


        //  painter 8
        lore.clear();
        lore.add(select);
        lore.add("Smother your enemies with toxic");
        lore.add("paint to paint over your competition");

        item.setType(Material.PURPLE_DYE);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Painter");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(8, item);


        //  pillarman 9
        lore.clear();
        lore.add(select);
        lore.add("Pillarz sounds like the next hit rock band");
        lore.add("-me");

        item.setType(Material.QUARTZ_PILLAR);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE+"Pillar Man");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(9, item);


        //  shadow 10
        lore.clear();
        lore.add(select);
        lore.add("Escape into the night to");
        lore.add("bewilder and confuse your foes.");

        item.setType(Material.INK_SAC);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY +"The Shadow");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(10, item);


        //  spellslinger 11
        lore.clear();
        lore.add(select);
        lore.add("Command the elements to deal");
        lore.add("with enemies at range");

        item.setType(Material.ARROW);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE +"Spell Slinger");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(11, item);


        //  tactician 12
        lore.clear();
        lore.add(select);
        lore.add("Win engagements by setting up");
        lore.add("teamfights in favorable situations");

        item.setType(Material.SPYGLASS);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE +"Tactician");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(12, item);


        //  protector 13
        lore.clear();
        lore.add(select);
        lore.add("Support and protect your teammates");
        lore.add("with a variety of magic");

        item.setType(Material.SHIELD);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD +"Protector");
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(13, item);

        // CLOSE 17
        item.setType(Material.BARRIER);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Close Menu");
        lore.clear();
        meta.setLore(lore);
        item.setItemMeta(meta);
        classInv.setItem(17, item);

    }

    public Inventory classEditInventory(Player player, Inventory invet){
        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.BOLD+"Select Class");
        inv.setContents(invet.getContents());

        Team team = TeamsInit.getTeam(player);

        //red pane
        ArrayList<Kit> takenKits = new ArrayList<Kit>();
        if(team != null) {
            Set<String> playerNames = team.getEntries();
            ArrayList<Player> players = new ArrayList<Player>();
            for (String playerName : playerNames) {
                if (Bukkit.getPlayer(playerName) != null) {
                    players.add(Bukkit.getPlayer(playerName));
                }
            }

            for (Player ply : players){
                takenKits.add(WizardInit.playersWizards.get(ply.getUniqueId()).kitID);
            }

        }

        //green pane
        Kit playerKit = WizardInit.playersWizards.get(player.getUniqueId()).kitID;

        String[] slots ={
                "alchemist", "bard", "berserker", "bloodmage", "builder", "cleric", "aviator", "demolitionist", "painter",
                "pillarman", "shadow", "spellslinger", "tactician", "protector"
        };

        ArrayList<Kit> kits = new ArrayList<>(Arrays.asList(Kit.values()));
        for (int i = 0; i < kits.size(); i++) {
            if (playerKit==kits.get(i)){
                //green pane
                ItemStack pane = inv.getItem(i);
                pane.setType(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta meta = pane.getItemMeta();
                meta.setLore(Arrays.asList("You have already selected this class."));
                pane.setItemMeta(meta);
                inv.setItem(i, pane);
            } else if (takenKits.contains(kits.get(i))){
                //red pane
                ItemStack pane = inv.getItem(i);
                pane.setType(Material.RED_STAINED_GLASS_PANE);
                ItemMeta meta = pane.getItemMeta();
                meta.setLore(Arrays.asList("A player on your team has already selected this class."));
                pane.setItemMeta(meta);
                inv.setItem(i,pane);
            }
        }


        return inv;
    }
}

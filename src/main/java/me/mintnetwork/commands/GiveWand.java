package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GiveWand implements CommandExecutor {

    private final Main plugin;

    public GiveWand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("givewands").setExecutor(this);
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (p.hasPermission("cwd.cangivewands")) {
            if (label.equalsIgnoreCase("givewands")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Utils.chat("&4Only players may execute this command"));
                    return true;
                }

                p.getInventory().addItem(getItem());
                return true;
            }
        }
        return false;
    }

    public ItemStack getItem(){

        ItemStack wandone = new ItemStack(Material.STICK);
        ItemMeta meta = wandone.getItemMeta();
        meta.setDisplayName(Utils.chat("&cTNT Bolt"));
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.chat("&a1mana"));
        lore.add(Utils.chat("&7Offense"));
        lore.add(Utils.chat("&7Shoots a bolt of energy that"));
        lore.add(Utils.chat("&7makes a small explosion on contac"));
        lore.add(Utils.chat("&7with a player or a block"));
        meta.setLore(lore);
        wandone.setItemMeta(meta);
        return wandone;
    }

}

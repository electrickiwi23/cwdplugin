package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class DebugCommand implements CommandExecutor {

    private final Main plugin;

    public DebugCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("test").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }

        System.out.println(((Player) sender).getInventory().getItemInMainHand());

        return false;
    }
}

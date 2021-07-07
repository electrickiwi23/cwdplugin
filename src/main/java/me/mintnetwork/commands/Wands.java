package me.mintnetwork.commands;

import me.mintnetwork.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class Wands implements CommandExecutor {

    public Wands(Main plugin) {
        plugin.getCommand("wands").setExecutor(this);
    }


    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }


        Player p = (Player) sender;
        return false;
    }
}

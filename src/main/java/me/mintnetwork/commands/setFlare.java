package me.mintnetwork.commands;

import me.mintnetwork.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class setFlare implements CommandExecutor {

    private final Main plugin;

    public setFlare(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("setflare").setExecutor(this);
    }


    @Override
    public boolean onCommand(@Nonnull CommandSender sender,@Nonnull Command command,@Nonnull String label,@Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }
        Player p = (Player) sender;
        if (p.hasPermission("cwd.cansetflares")) {
            if (args.length == 1) {

                char site = args[0].charAt(0);
                boolean success = false;
                switch (site) {
                    case ('a'):
                        plugin.getConfig().set("FlareAx", p.getLocation().getBlockX() + .5);
                        plugin.getConfig().set("FlareAy", p.getLocation().getBlockY());
                        plugin.getConfig().set("FlareAz", p.getLocation().getBlockZ() + .5);
                        p.sendMessage("set flare a at "  + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() +  ", " + p.getLocation().getBlockZ());
                        success = true;
                        break;
                    case ('b'):
                        plugin.getConfig().set("FlareBx", p.getLocation().getBlockX() + .5);
                        plugin.getConfig().set("FlareBy", p.getLocation().getBlockY());
                        plugin.getConfig().set("FlareBz", p.getLocation().getBlockZ() + .5);
                        p.sendMessage("set flare b at " + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() +  ", " + p.getLocation().getBlockZ());
                        success = true;
                        break;
                    case ('c'):
                        plugin.getConfig().set("FlareCx", p.getLocation().getBlockX() + .5);
                        plugin.getConfig().set("FlareCy", p.getLocation().getBlockY());
                        plugin.getConfig().set("FlareCz", p.getLocation().getBlockZ() + .5);
                        p.sendMessage("set flare c at "  + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() +  ", " + p.getLocation().getBlockZ());
                        success = true;
                        break;

                }
                if (success) {
                    plugin.getConfig().options().copyDefaults(true);
                    plugin.saveConfig();
                } else {
                    p.sendMessage("it needs to be a b or c");
                }


            } else p.sendMessage("you need exactly one argument");


        } else {
            p.sendMessage("you need permission to do this");
        }
        return false;
    }
}
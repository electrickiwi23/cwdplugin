package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.initialization.GameStart;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class endGame implements CommandExecutor {

    private final Main plugin;

    public endGame(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("endgame").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }
        GameStart.endGame(null);


        return false;
    }
}

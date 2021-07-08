package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Start implements CommandExecutor {

    private final Main plugin;

    public Start(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("start").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }

        if (sender.hasPermission("cwd.cangivewands")) {
            if (args.length==0) {
                GameStart.startGeneric(plugin);
            }else{
                switch (args[0].toLowerCase()) {
                    case ("skirmish"):
                    case ("test"):
                        GameStart.startGeneric(plugin);
                        break;
                    case ("elimination"):
                    case ("elim"):
                        if (args.length>=2){
                            if(args[1].matches("[0-9]+") && args[1].length() > 0) {
                                GameStart.startElimination(plugin,Math.abs(Integer.parseInt(args[1])));
                            }else{
                                sender.sendMessage(ChatColor.RED+("You must enter a number."));
                            }

                        } else{
                            sender.sendMessage(ChatColor.RED+("You must enter an amount of lives."));
                        }
                        break;
                    case("br"):
                    case("battleroyale"):
                        GameStart.startBR(plugin,((Player) sender).getWorld());
                        break;
                    case("flares"):
                    case("flare"):
                        if (args.length>=2) {
                            if(args[1].matches("[0-9]+") && args[1].length() > 0) {
                                GameStart.startFlares(plugin,((Player) sender).getWorld(),Integer.parseInt(args[1]));
                            }else{
                                sender.sendMessage(ChatColor.RED+("You must enter a number."));
                            }
                        } else{
                            sender.sendMessage(ChatColor.RED+("You must enter a amount of time."));
                        }
                        break;
                    case("koth"):
                    case("kingofthehill"):
                    case("hill"):

                        if (args.length>=2) {
                            if(args[1].matches("[0-9]+") && args[1].length() > 0) {
                                GameStart.startKoth(plugin,((Player) sender).getWorld(),Integer.parseInt(args[1]));
                            }else{
                                sender.sendMessage(ChatColor.RED+("You must enter a number."));
                            }
                        } else{
                            sender.sendMessage(ChatColor.RED+("You must enter a amount of time."));
                        }
                        break;
                }
            }
        }
        return false;
    }
}

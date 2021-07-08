package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.utils.Utils;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AssignTeams implements CommandExecutor {

    private final Main plugin;

    public AssignTeams(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("assignteams").setExecutor(this);
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (p.hasPermission("cwd.canassignteams")) {
            if (args.length>0){
                int teams = Integer.parseInt(args[0]);
                ArrayList<Character> possibles = new ArrayList<Character>();
                possibles.add('b');
                possibles.add('r');
                possibles.add('g');
                possibles.add('y');

                char team1 = 'b';
                if (args.length>1) {
                    if (possibles.contains(args[1].charAt(0))) team1 = args[1].charAt(0);
                }
                char team2 = 'r';
                if (args.length>2) {
                    if (possibles.contains(args[2].charAt(0))) team2 = args[2].charAt(0);
                }
                if (team1==team2){
                    if (team1=='b'){
                        team2 = 'r';
                    } else {
                        team2 = 'b';
                    }
                }

                if (teams==2||teams==4){
                    TeamsInit.initializeTeams(teams,team1,team2);
                    return true;
                }  p.sendMessage(ChatColor.RED+"There must be either 2 or 4 teams.");

            } else {
                p.sendMessage(ChatColor.RED+"&cYou must input an amount of teams.");
            }



        }

        return false;
    }
}
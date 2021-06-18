package me.mintnetwork.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class StartAutoCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cde, String arg, String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("skirmish");
        list.add("elimination");
        list.add("battleroyale");


        return list;
    }
}

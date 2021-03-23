package me.mintnetwork.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cde, String arg, String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("alchemist");
        list.add("bard");
        list.add("berserker");
        list.add("bloodmage");
        list.add("builder");
        list.add("cleric");
        list.add("skyflyer");
        list.add("demolitionist");
        list.add("painter");
        list.add("pillarman");
        list.add("shadow");
        list.add("spellslinger");
        list.add("tactician");

        return list;
    }
}

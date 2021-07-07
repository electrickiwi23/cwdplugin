package me.mintnetwork.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ClassAutoCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cde, String arg, String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("alchemist");
        list.add("bard");
        list.add("berserker");
        list.add("bloodmage");
        list.add("builder");
        list.add("cleric");
        list.add("aviator");
        list.add("demolitionist");
        list.add("painter");
        list.add("pillarman");
        list.add("shadow");
        list.add("spellslinger");
        list.add("tactician");
        list.add("protector");

        return list;
    }
}

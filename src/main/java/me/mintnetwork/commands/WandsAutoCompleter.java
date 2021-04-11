package me.mintnetwork.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class WandsAutoCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cde, String arg, String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("fireworkbolt");
        list.add("blinkstep");
        list.add("jumpboost");
        list.add("engineblast");
        list.add("dragonorb");
        list.add("batsonar");
        list.add("tntring");
        list.add("hivebolt");
        list.add("blackhole");
        list.add("endwarp");
        list.add("babyboomer");
        list.add("zombiesummon");
        list.add("slimeball");
        list.add("voltstep");

        return list;
    }
}

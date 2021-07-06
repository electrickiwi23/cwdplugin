package me.mintnetwork.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WandsAutoCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender,@Nonnull Command cde,@Nonnull String arg,@Nonnull String[] args) {
        List<String> list = new ArrayList<>();
        list.add("fireworkbolt");
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
        list.add("flashstep");
        list.add("shoulderblitz");
        list.add("anviltoss");
        list.add("stormstrike");
        list.add("manabullet");

        return list;
    }
}

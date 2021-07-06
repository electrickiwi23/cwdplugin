package me.mintnetwork.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class StartAutoCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender,@Nonnull Command cde,@Nonnull String arg,@Nonnull String[] args) {
        List<String> list = new ArrayList<>();
        list.add("skirmish");
        list.add("elimination");
        list.add("battleroyale");
        list.add("flare");
        list.add("kingofthehill");


        return list;
    }
}

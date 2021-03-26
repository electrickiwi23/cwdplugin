package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Passives {

    private final Main plugin;

    public Passives(Main plugin) {
        this.plugin = plugin;
    };

    public static void PassivesStart(Main plugin){
        BuilderUlt(plugin);
    }


    public static void BuilderUlt(Main plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player p:Bukkit.getOnlinePlayers()) {
                    Wizard wizard = WizardInit.playersWizards.get(p);
                    if (wizard.ClassID.equals("builder")) {
                        String TeamName = TeamsInit.getTeamName(p);
                        ItemStack wool = new ItemStack(Material.GRAY_WOOL);
                        switch (TeamName){
                            case ("red"):
                                wool.setType(Material.RED_WOOL);
                            case ("blue"):
                                wool.setType(Material.BLUE_WOOL);
                            case ("yellow"):
                                wool.setType(Material.YELLOW_WOOL);
                            case ("green"):
                                wool.setType(Material.LIME_WOOL);
                        }

                        if (p.getInventory().containsAtLeast(wool,64)){
                            p.getInventory().addItem(wool);
                        }


                    }



                }

            }

    },1,30);
    }
}

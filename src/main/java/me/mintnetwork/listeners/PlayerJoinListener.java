package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.CloneNPC;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Main plugin;

    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (CloneNPC.getNPCS() != null){
            if (!CloneNPC.getNPCS().isEmpty()) {
                CloneNPC.addJoinPacket(p);
            }
        }
        WizardInit.playersWizards.put(p, new Wizard(p));
        System.out.println(p.getName() + " created Wizard");
    }
}
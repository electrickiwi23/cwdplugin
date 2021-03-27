package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class EntityDieListener implements Listener {

    private final Main plugin;

    public EntityDieListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void EntityDeath(EntityDeathEvent event) {
        LivingEntity e = event.getEntity();
        Map<Entity, String> id = ProjectileInfo.getProjectileID();
        Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();

        if (e instanceof Player){
            Player p = (Player) e;
            Wizard wizard = WizardInit.playersWizards.get(p);
            String gameMode = GameStart.gameMode;
            if (GameStart.gameRunning) {
                if (gameMode.equals("elimination")) {
                    wizard.Mana = 3;
                    p.setLevel(3);
                    if (wizard.ElimLives > 0) {
                        wizard.ElimLives--;
                    } else {
                        p.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }

        }

        if (id.containsKey(e)) {
            if (id.get(e).equals("VoidPillar")) {
                tick.get(e).cancel();
            }
        }
    }
}

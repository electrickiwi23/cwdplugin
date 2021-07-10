package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
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
            Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
            me.mintnetwork.initialization.GameMode gameMode = GameStart.gameMode;
            if (GameStart.gameRunning) {
                if (gameMode.equals(me.mintnetwork.initialization.GameMode.ELIMINATION)||gameMode.equals(me.mintnetwork.initialization.GameMode.BATTLE_ROYAL)) {
                    wizard.Mana = 3;
                    p.setLevel(3);
                    if (wizard.ElimLives > 0) {
                        wizard.ElimLives--;
                    }
                    if (wizard.ElimLives <= 0){
                        p.setGameMode(GameMode.SPECTATOR);
                    }
                    Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

                    if (board.getObjective("lives")!=null) board.getObjective("lives").getScore(p.getDisplayName()).setScore(wizard.ElimLives);

                    ArrayList<Team> teams = new ArrayList<>();
                    for (Player player:Bukkit.getOnlinePlayers()) {
                        if (WizardInit.playersWizards.get(player.getUniqueId()).ElimLives>0){
                            if (TeamsInit.getTeam(player)!=null){
                                if (!teams.contains(TeamsInit.getTeam(player))){
                                    teams.add(TeamsInit.getTeam(player));
                                }
                            }
                        }
                    }
                    if (teams.size()==1){
                        GameStart.endGame(teams.get(0));
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

package me.mintnetwork.repeaters;

import jdk.vm.ci.aarch64.AArch64;
import me.mintnetwork.Main;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Passives {

    private final Main plugin;

    public Passives(Main plugin) {
        this.plugin = plugin;
    }

    ;

    public static void PassivesStart(Main plugin) {
        SlowLoopPassives(plugin);
    }


    public static void SlowLoopPassives(Main plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    String teamName = TeamsInit.getTeamName(p);
                    Wizard wizard = WizardInit.playersWizards.get(p);
                    switch (wizard.ClassID) {
                        case "builder":
                            wizard.PassiveTick++;
                            if (wizard.PassiveTick >= 2) {
                                wizard.PassiveTick = 0;
                                String TeamName = TeamsInit.getTeamName(p);
                                ItemStack wool = new ItemStack(Material.GRAY_WOOL);
                                switch (TeamName) {
                                    case ("red"):
                                        wool.setType(Material.RED_WOOL);
                                        break;
                                    case ("blue"):
                                        wool.setType(Material.BLUE_WOOL);
                                        break;
                                    case ("yellow"):
                                        wool.setType(Material.YELLOW_WOOL);
                                        break;
                                    case ("green"):
                                        wool.setType(Material.LIME_WOOL);
                                        break;
                                }

                                if (!p.getInventory().containsAtLeast(wool, 64)) {
                                    p.getInventory().addItem(wool);
                                }
                            }
                            break;
                        case "cleric":
                            if (p.getMaxHealth() - .20 >= Math.ceil(p.getHealth())) {
                                p.setHealth(p.getHealth() + .20);
                            }
                            break;
                        case "bard":
                            wizard.PassiveTick++;
                            if (wizard.PassiveTick >= 5) {
                                wizard.PassiveTick = 0;
                                List<Player> inRange = new ArrayList<>();
                                int listLevel = 2;
                                for (Player e : Bukkit.getOnlinePlayers()) {
                                    String hitTeamName = TeamsInit.getTeamName(e);
                                    if (hitTeamName.equals(teamName)) {
                                        if (e.getLocation().distance(p.getLocation()) <= 6) {
                                            int currentLevel = 0;
                                            if (StatusEffects.bardInspiration.containsKey(e)) {
                                                currentLevel = (int) Math.ceil(StatusEffects.bardInspiration.get(e));
                                            }

                                            if (currentLevel < listLevel) {
                                                inRange.clear();
                                                listLevel = currentLevel;
                                                inRange.add(e);
                                            } else if (currentLevel == listLevel) {
                                                inRange.add(e);
                                            }
                                        }
                                    }
                                }
                                if (inRange.size() > 0) {

                                    int rnd = new Random().nextInt(inRange.size());
                                    Player e = inRange.get(rnd);
                                    if (StatusEffects.bardInspiration.containsKey(e)) {
                                        StatusEffects.bardInspiration.replace(e, StatusEffects.bardInspiration.get(e) + 1);
                                    } else {
                                        StatusEffects.bardInspiration.put(e, 1.0);
                                    }
                                    e.getWorld().spawnParticle(Particle.NOTE, e.getEyeLocation().add(0, 1, 0), 0, .1, .2, .92, 1, null);
                                    e.getWorld().playSound(e.getEyeLocation().add(0, 1, 0), Sound.BLOCK_NOTE_BLOCK_PLING, (float) .5, (float) (7 + Math.ceil(StatusEffects.bardInspiration.get(e))));
                                }
                            }
                            break;


                    }
                }
            }
        }, 1, 20);
    }
}

package me.mintnetwork.repeaters;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Kit;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Passives {

    public static void PassivesStart(Main plugin) {
        StartPassives(plugin);
    }


    public static void StartPassives(Main plugin) {
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (GameStart.gameRunning) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
                        if (wizard.kitID==Kit.COSMONAUT){
                            if (Utils.isGrounded(p)){
                                wizard.PassiveTick = 50;
                            } else{
                                if (p.isFlying()){
                                    wizard.PassiveTick--;
                                    if (p.getGameMode()== GameMode.ADVENTURE||p.getGameMode()== GameMode.SURVIVAL) p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION,p.getLocation().add(0,-.5,0),1,.2,.2,.2,0,new Particle.DustTransition(Color.BLACK,Color.FUCHSIA,2));
                                }
                            }
                            if (p.getGameMode()==GameMode.CREATIVE||p.getGameMode()==GameMode.SPECTATOR){
                                p.setAllowFlight(true);
                                p.setFlySpeed(1f);
                            } else {
                                p.setAllowFlight(wizard.PassiveTick > 0 && StatusEffects.CanCast(p));
                                p.setFlySpeed(.025f);
                            }
                        }
                    }
                }
            }
        },1,2);

        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (GameStart.gameRunning) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        String teamName = TeamsInit.getTeamName(p);
                        Wizard wizard = WizardInit.playersWizards.get(p.getUniqueId());
                        if (wizard.combatTick < 10) {
                            wizard.combatTick++;
                        }
                        switch (wizard.kitID) {
                            case ALCHEMIST:
                                if (!p.getInventory().contains(Material.SPLASH_POTION)) {
                                    wizard.PassiveTick++;
                                    if (wizard.PassiveTick >= 20) {
                                        wizard.PassiveTick = 0;
                                        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
                                        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

                                        switch ((int) Math.ceil(Math.random() * 4)) {
                                            case (1):
                                                potionMeta.setDisplayName(ChatColor.RESET + ("Splash Potion of Swiftness"));
                                                potionMeta.setColor(Color.fromRGB(124, 175, 198));
                                                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1), false);
                                                break;
                                            case (2):
                                                potionMeta.setDisplayName(ChatColor.RESET + ("Splash Potion of Regeneration"));
                                                potionMeta.setColor(Color.fromRGB(205, 92, 171));
                                                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), false);
                                                break;
                                            case (3):
                                                potionMeta.setDisplayName(ChatColor.RESET + ("Splash Potion of Poison"));
                                                potionMeta.setColor(Color.fromRGB(78, 147, 49));
                                                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 200, 1), false);
                                                break;
                                            case (4):
                                                potionMeta.setDisplayName(ChatColor.RESET + ("Splash Potion of Slowness"));
                                                potionMeta.setColor(Color.fromRGB(90, 108, 129));
                                                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1), false);
                                                break;

                                        }


                                        potion.setItemMeta(potionMeta);
                                        p.getInventory().addItem(potion);
                                    }
                                }
                                break;
                            case BUILDER:
                                wizard.PassiveTick++;
                                if (wizard.PassiveTick >= 2) {
                                    wizard.PassiveTick = 0;
                                    ItemStack wool = new ItemStack(Material.GRAY_WOOL);
                                    switch (teamName) {
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
                            case CLERIC:
                                if (!p.isDead()) {
                                        p.setHealth(Math.min(p.getHealth() + .20,p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
                                }
                                break;
                            case TACTICIAN:
                                if (wizard.PassiveTick < 10) {
                                    wizard.PassiveTick++;
                                }
                                break;
                            case HUNTER:
                                if (!p.getInventory().contains(Material.ARROW,3)){
                                    wizard.PassiveTick++;
                                    if (wizard.PassiveTick>=3){
                                        wizard.PassiveTick=0;
                                        p.getInventory().addItem(new ItemStack(Material.ARROW));
                                    }
                                }
                                break;
                            case BARD:
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
            }
        }, 1, 20);
    }
}

package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Map;

public class  EntityDamageListener implements Listener {

    private final Main plugin;

    public EntityDamageListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        Entity victim = event.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) entity;
            Wizard wizard = WizardInit.playersWizards.get(entity);
            Collection<Player> shadowList = StatusEffects.ShadowInvis;

            if (shadowList.contains(p)) {
                Map<Player, Runnable> CancelMap = StatusEffects.getShadowCancel();
                System.out.println("invis damage");
                CancelMap.get(p).run();
            }

            if (StatusEffects.RageUlt.containsKey(p)) {
                event.setDamage(event.getDamage()+2);
            }

            if (StatusEffects.bardInspiration.containsKey(p)){
                p.getWorld().spawnParticle(Particle.NOTE, p.getEyeLocation().add(0, 1, 0), 0, .1, .2, .92, 1, null);
                p.getWorld().playSound(p.getEyeLocation().add(0, 1, 0), Sound.BLOCK_NOTE_BLOCK_PLING, (float) .5, (float) (7 + Math.ceil(StatusEffects.bardInspiration.get(p))));
                StatusEffects.bardInspiration.replace(p,StatusEffects.bardInspiration.get(p)-1);
                event.setDamage(event.getDamage()+1);


            }

            if (victim instanceof Player) {
                Map<LivingEntity, Integer> speedMap = StatusEffects.speedTimer;
                if (speedMap.containsKey(entity)) {
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, false, true));
                    speedMap.put((Player) entity, 30);
                }
                Player live = (Player) entity;
                switch (wizard.ClassID) {
                    case "blood mage":
                        if (StatusEffects.BloodWeak.containsKey(victim)) {
                            for (int i = 0; i < 3; i++) {
                                if (live.getMaxHealth() - 1 >= Math.ceil(live.getHealth())) {
                                    live.setHealth(live.getHealth() + 1);
                                }
                            }
                        } else {
                            if (live.getMaxHealth() - 1 >= Math.ceil(live.getHealth())) {
                                live.setHealth(live.getHealth() + 1);
                            }
                        }
                        break;
                    case "berserker":
                        for (int i = 0; i < 2; i++) {
                            Mana.tickMana((Player) entity);
                        }
                        break;
                    case "tactician":
                        if (wizard.PassiveTick>=10){
                            wizard.PassiveTick = 0;
                            Wizard victimWiz = WizardInit.playersWizards.get(victim);
                            live.sendMessage(victim.getName() + ":");
                            live.sendMessage("Health: " + Math.ceil(((Player) victim).getHealth()));
                            live.sendMessage("Mana: " + victimWiz.Mana);
                            live.sendMessage("Ultimate: ");


                        }
                        break;
                }
            }
        }

    }
}

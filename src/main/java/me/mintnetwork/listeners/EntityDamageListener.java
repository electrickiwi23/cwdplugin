package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.spells.BloodMage;
import me.mintnetwork.utils.Utils;
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
            Wizard wizard = WizardInit.playersWizards.get(entity.getUniqueId());
            Collection<Player> shadowList = StatusEffects.ShadowInvis;

            if (shadowList.contains(p)) {
                Map<Player, Runnable> CancelMap = StatusEffects.getShadowCancel();
                System.out.println("invis damage");
                CancelMap.get(p).run();
            }

            if (StatusEffects.RageUlt.containsKey(p)) {
                event.setDamage(event.getDamage()+3);
            }
            Map<LivingEntity, Integer> painted = StatusEffects.paintTimer;
            if (victim instanceof LivingEntity) {
                LivingEntity liveVictim = (LivingEntity) victim;
                if (wizard.ClassID.equals("painter")) {
                    if (painted.containsKey(liveVictim)) {
                        painted.replace(liveVictim, painted.get(liveVictim) + 80);
                    } else {
                        painted.put(liveVictim, 80);
                    }
                }
            }

            if (StatusEffects.bardInspiration.containsKey(p)){
                p.getWorld().spawnParticle(Particle.NOTE, p.getEyeLocation().add(0, 1, 0), 0, .1, .2, .92, 1, null);
                p.getWorld().playSound(p.getEyeLocation().add(0, 1, 0), Sound.BLOCK_NOTE_BLOCK_PLING, (float) .5, (float) (7 + Math.ceil(StatusEffects.bardInspiration.get(p))));
                StatusEffects.bardInspiration.replace(p,StatusEffects.bardInspiration.get(p)-1);
                event.setDamage(event.getDamage()+1);


            }

            if (victim instanceof Player) {
                Wizard victimWizard = WizardInit.playersWizards.get(victim.getUniqueId());

                Map<LivingEntity, Integer> speedMap = StatusEffects.speedTimer;
                if (speedMap.containsKey(entity)) {
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, false, true));
                    speedMap.put((Player) entity, 30);
                }

                if (victimWizard.ClassID.equals("berserker")){
                    Mana.addMana((Player) victim,(int)Math.round(event.getDamage()));
                }



                Player live = (Player) entity;

                Mana.addMana(live,(int)Math.round(event.getDamage()));

                switch (wizard.ClassID) {
                    case "blood mage":
                        BloodMage.BloodLink(p, (Player) victim);
                        if (StatusEffects.BloodWeak.containsKey(victim)) {
                            for (int i = 0; i < 3; i++) {
                                if (!live.isDead()) {
                                    if (live.getMaxHealth() - 1 >= Math.ceil(live.getHealth())) {
                                        live.setHealth(live.getHealth() + 2);
                                    }
                                }
                            }
                        } else {
                            if (!live.isDead()) {
                                if (live.getMaxHealth() - 1 >= Math.ceil(live.getHealth())) {
                                    live.setHealth(live.getHealth() + 1);
                                }
                            }
                        }
                        break;
                    case "tactician":
                        if (wizard.PassiveTick>=10){
                            wizard.PassiveTick = 0;
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    Wizard victimWiz = WizardInit.playersWizards.get(victim.getUniqueId());
                                    live.sendMessage(Utils.chat("&n&l" + victim.getName() + ":"));
                                    live.sendMessage(Utils.chat("&cHealth: " + Math.ceil(((Player) victim).getHealth())));
                                    live.sendMessage(Utils.chat("&aMana: " + victimWiz.Mana));
                                    live.sendMessage(Utils.chat("&6Ultimate: " + (int) (Ultimate.getUltPercentage((Player) victim ) * 100) + "%"));
                                }
                            });
                        }
                        break;
                }
            }
        }

    }
}

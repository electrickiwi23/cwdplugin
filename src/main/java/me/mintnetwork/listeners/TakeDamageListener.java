package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.ShadowGrapple;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TakeDamageListener implements Listener {

    private final Main plugin;

    public TakeDamageListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onHit(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player){
            Player p = (Player) entity;
            for (Player victim:StatusEffects.protectionAura.keySet()) {
                if (victim.getLocation().distance(p.getLocation()) < 15 && TeamsInit.getTeamName(p).equals(TeamsInit.getTeamName(victim))&&!StatusEffects.protectionAura.containsKey(p)) {
                    victim.damage(event.getDamage());
                    event.setCancelled(true);
                }
            }
            Collection<Player> shadowList = StatusEffects.ShadowInvis;
            if (StatusEffects.EnergyShield.contains(p)){
                Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (p.getAbsorptionAmount()<=0){
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK,.3F,1);
                            for (AttributeModifier attributeModifier:p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getModifiers()){
                                if (attributeModifier.getName().equals("EnergyShield")){
                                    p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).removeModifier(attributeModifier);
                                }
                            }
                            StatusEffects.EnergyShield.remove(p);
                        }
                    }
                }
                );

            }


            if (shadowList.contains(p)) {
                Map<Player, Runnable> CancelMap = StatusEffects.getShadowCancel();
                System.out.println("invis damage");
                CancelMap.get(p).run();
            }
        }
        if (entity instanceof LivingEntity) {
            if (StatusEffects.BloodWeak.containsKey(entity)) {
                event.setDamage(event.getDamage() + 1);
            }


            for (int i = 0; i < ShadowGrapple.allGrapples.size(); i++) {
                ShadowGrapple grapple = ShadowGrapple.allGrapples.get(i);
                if (grapple.getVictim() == entity) {
                    grapple.disband();
                    i--;
                }
            }
        }

    }
}

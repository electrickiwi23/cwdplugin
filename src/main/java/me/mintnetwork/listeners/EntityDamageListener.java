package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityDamageListener implements Listener {

    private final Main plugin;

    public EntityDamageListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        Entity victim = event.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) entity;
            Collection<Player> shadowList = StatusEffects.getShadowInvis();
            if (shadowList.contains(p)) {
                Map<Player, Runnable> CancelMap = StatusEffects.getShadowCancel();
                System.out.println("invis damage");
                CancelMap.get(p).run();
            }
        }
        if ((entity instanceof Arrow)) {

            if (victim instanceof LivingEntity) {

                LivingEntity living = (LivingEntity) victim;
                Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                living.setCustomName(String.valueOf(living.getHealth()));
                                living.setNoDamageTicks(0);
                            }
                        });

            }
        }

    }
}

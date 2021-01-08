package me.mintnetwork.listeners;

//import com.aim.coltonjgriswold.api.ParticleProjectile;
//import me.mintnetwork.spells.projectiles.BloodBolt;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.TraceEffect;
import me.mintnetwork.Main;
import me.mintnetwork.spells.Cast;
import org.bukkit.*;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import me.mintnetwork.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class RightClickListener implements Listener {

    private final Main plugin;

    public RightClickListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    //    ParticleProjectile bolt = new BloodBolt();
    Map<UUID, Long> lastUsed = new HashMap<UUID, Long>();


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player p = (Player) event.getPlayer();
        EffectManager em = new EffectManager(plugin);


        if (lastUsed.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - lastUsed.get(p.getUniqueId()) < 580) {

                return;
            }
        }
        lastUsed.put(p.getUniqueId(), System.currentTimeMillis());
        Block targetBlock = p.getTargetBlock((Set<Material>) null, 100);
        if (p.getInventory().getItemInMainHand() != null) {
            if (event.getHand().equals(EquipmentSlot.HAND)) {
                if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).getDisplayName().contains("Blood Bolt")) {
                        Cast.ShieldDome(p, em, plugin);
//                        bolt.launch(event.getPlayer(),false);
//                        TornadoEffect effect = new TornadoEffect(em);
//                        effect.tornadoParticle = Particle.SPELL;
//                        effect.tornadoHeight = 2;
//                        effect.particleOffsetX = 1;
//                        effect.particleOffsetZ = 1;
//                        effect.duration = 1;
//                        effect.setLocation(p.getLocation().add(0,0,0));
//                        em.start(effect);
                    }
                    //TNT bolt
                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("TNT Bolt")) {
                        if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {

                        }
                    }
                    //End Warp
                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("End Warp")) {
//
                    }
                    //Air Dash
                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Air Dash")) {

                    }


                }

            }

        }
    }

}

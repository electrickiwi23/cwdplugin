package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.wizard.Wizard;
import me.mintnetwork.wizard.WizardInit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerSneakListener implements Listener {
    private final Main plugin;

    public PlayerSneakListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static List<Player> Zoomed = new ArrayList<Player>();

    public static Map<Player, ItemStack> PreviousHelm = new HashMap<Player, ItemStack>();

    public static Map<Player, Float> PreviousSpeed = new HashMap<Player, Float>();

    public static List<Player> getZoomed() {return Zoomed;}

    public static Map<Player, ItemStack> getPreviousHelm() {return PreviousHelm;}

    public static Map<Player, Float> getPreviousSpeed() {return PreviousSpeed;}



    @EventHandler
    public void PlayerSneaks(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        Wizard wizard = WizardInit.playersWizards.get(p);
        if (p.isSneaking()){
            if (Zoomed.contains(p)) {
                p.setWalkSpeed(PreviousSpeed.get(p));
                p.getInventory().setHelmet(PreviousHelm.get(p));
                Zoomed.remove(p);
            }


        }else{
            if (wizard.ClassID.equals("sky flyer")){
                if (StatusEffects.cloudFloating.contains(p)){
                    p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    StatusEffects.cloudFloating.remove(p);
                }else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100000, 3, false, false));
                    StatusEffects.cloudFloating.add(p);
                }
            }

            if (!p.getInventory().getItemInMainHand().getType().isAir()) {
                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Sniper Bolt")) {
                    PreviousSpeed.put(p,p.getWalkSpeed());
                    PreviousHelm.put(p,p.getInventory().getHelmet());

                    p.setWalkSpeed((float) -.1);
                    p.getInventory().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
                    Zoomed.add(p);
                }
            }
        }
    }
}

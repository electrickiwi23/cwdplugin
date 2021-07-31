package me.mintnetwork.initialization;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class WizardInit{

    public static HashMap<UUID, Wizard> playersWizards = new HashMap<>();

    public void WizardInitialization(Main plugin) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            //on start up add player to the list
            playersWizards.put(player.getUniqueId(), new Wizard(player));
        }
    }

    public void resetAllClasses(){
        for (Player p:Bukkit.getOnlinePlayers()) {
            resetClass(p);
        }
    }

    public static void resetClass(Player p){
        //shadow
        p.setCustomNameVisible(false);
        //aviator
        StatusEffects.cloudFloating.remove(p);
        p.removePotionEffect(PotionEffectType.SLOW_FALLING);
        //cosmonaut
        p.setAllowFlight(false);
        //protector
        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        for (AttributeModifier modifier : p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getModifiers()) {
            if (modifier.getName().equals("ProtectorSlow")) {

                p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
            }
        }
    }


}

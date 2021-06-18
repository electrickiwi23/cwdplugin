package me.mintnetwork.Objects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class ShadowGrapple {
    int time = 60;
    Player shadow;
    LivingEntity victim;
    Plugin plugin;

    public static ArrayList<ShadowGrapple> allGrapples = new ArrayList<>();

    public ShadowGrapple(Player p, LivingEntity v, Plugin plugin){
        shadow = p;
        victim = v;
        this.plugin = plugin;

        p.addPassenger(v);

        if (v instanceof Player) {
            p.hidePlayer(plugin, (Player) v);
        }


        allGrapples.add(this);

    }

    public Player getShadow() { return shadow; }

    public LivingEntity getVictim(){
        return victim;
    }

    //returns true if the grapple ran out
    public boolean tick(){
        time--;
        if (time<=0){
            disband();
            return true;
        }
        return false;
    }

    //returns true if the grapple ran out
    public boolean tick(int i){
        time-=i;
        if (time<=0){
            disband();
            return true;
        }
        return false;
    }

    public void disband(){

        allGrapples.remove(this);

        System.out.println("worked: " + shadow.eject());

        if (victim instanceof Player) {
            shadow.showPlayer(plugin, (Player) victim);
        }

        victim.removePotionEffect(PotionEffectType.BLINDNESS);
        victim.removePotionEffect(PotionEffectType.WEAKNESS);
        shadow.removePotionEffect(PotionEffectType.WEAKNESS);




    }

}

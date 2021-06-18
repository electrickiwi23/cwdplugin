package me.mintnetwork.Objects;

import me.mintnetwork.repeaters.BlockDecay;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DecayBlock {
    public Block block;
    public float maxHealth;
    public float health;
    public float decayRate;
    public int ID;
    public Material decayInto;
    public boolean forceful = false;

    public DecayBlock(int health,float rate,Block b){
        block=b;
        this.decayRate = rate;
        this.health=health;
        maxHealth=health;
        ID=new Random().nextInt();
        BlockDecay.decay.put(block,this);
    }

    public void setForceful(boolean b){
        forceful = b;
    }

    public DecayBlock(int health,float rate,Block b,Material into){
        decayInto = into;
        block=b;
        this.decayRate = rate;
        this.health=health;
        ID=new Random().nextInt();
        BlockDecay.decay.put(block,this);
    }

    public void damage(int amount){
        health-= amount;
    }

    public boolean tickBlock() {
        health -= decayRate;

        if (block.getType().isAir()) {
            forceful = true;
            health = 0;
        }

        if (maxHealth != health) {
            int damage = (int) Math.ceil(((double) maxHealth - health + 1) / maxHealth * 9);

            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(ID, (new BlockPosition(block.getX(), block.getY(), block.getZ())), damage);
            try {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) player).getHandle().b.sendPacket(packet);
                }

            } catch (Exception ignored) {
            }
        }

        if (health<=0){
            remove();
            return true;
        }
        return false;
    }

    public void remove(){
        if (decayInto==null||forceful){
            block.breakNaturally();
        } else{
            block.setType(decayInto);
        }

        BlockDecay.decay.remove(block);

    }

}

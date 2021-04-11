package me.mintnetwork.Objects;

import me.mintnetwork.repeaters.BlockDecay;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class DecayBlock {
    public Block block;
    public int age = 0;
    public int decayTime;
    public int ID;

    public DecayBlock(int decay,Block b){
        block=b;
        decayTime=decay;
        ID=new Random().nextInt();
        BlockDecay.decay.put(block,this);
    }

    public void tickBlock(){
        age++;

        if (block.getType().isAir()){
            age=decayTime;
        }

        int damage = (int) Math.ceil(((double) age+1)/decayTime*9);

        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(ID,(new BlockPosition(block.getX(), block.getY(), block.getZ())),damage);
        try {
            for (Player player:Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }

        } catch (Exception ignored) {
        }
    }

}

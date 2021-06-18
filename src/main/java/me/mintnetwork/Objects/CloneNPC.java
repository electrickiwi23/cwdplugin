package me.mintnetwork.Objects;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CloneNPC {

    private static List<EntityPlayer> NPCS = new ArrayList<>();

    public static String[] getSkin(Player player){

        EntityPlayer p = ((CraftPlayer) player).getHandle();
        GameProfile profile = p.getProfile();
        Property property = profile.getProperties().get("textures").iterator().next();
        String texture = property.getValue();
        String signature = property.getSignature();
        return new String[] {texture, signature};

    }


    public static void createNPC(Player player){

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        EntityPlayer npc = new EntityPlayer(server, world, gameProfile);

        npc.setLocation(player.getLocation().getX(),player.getLocation().getY(), player.getLocation().getZ(),player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch());
        npc.setCustomNameVisible(false);

        String[] name = getSkin(player);
        gameProfile.getProperties().put("textures", new Property("textures", name[0],name[1]));


        addNPCPacket(npc);
        NPCS.add(npc);

    }

    public static void addNPCPacket(EntityPlayer npc){
        for (Player player: Bukkit.getOnlinePlayers()){
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a,npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc,(byte) (npc.getYRot() * 256/360)));
        }
    }

    public static void addJoinPacket(Player player){
        for (EntityPlayer npc: NPCS){
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a,npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc,(byte) (npc.getYRot() * 256/360)));
        }

    }

    public static List<EntityPlayer> getNPCS(){
        return NPCS;
    }


}

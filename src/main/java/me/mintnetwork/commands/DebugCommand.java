package me.mintnetwork.commands;

import me.mintnetwork.Main;
import me.mintnetwork.repeaters.Ultimate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DebugCommand implements CommandExecutor {

    private final Main plugin;

    public DebugCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("test").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }
        Player p = (Player) sender;
        Ultimate.FullCharge(p);


//
//
//
////        ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation().add(0,-1.9,0), EntityType.ARMOR_STAND);
////        stand.setInvisible(true);
////        stand.setMarker(true);
////        stand.getEquipment().setHelmet(new ItemStack(Material.BLUE_BANNER),true);
//
//        Sheep sheep = (Sheep) p.getWorld().spawnEntity(p.getEyeLocation().add(0,1,0),EntityType.SHEEP);
//        sheep.setGravity(false);
//        sheep.setColor(DyeColor.BLUE);
//
//        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
//            @Override
//            public void run() {
//
//                sheep.teleport(p.getEyeLocation().add(0,1,0));
//
//
//            }
//        },1,1);


        return false;
    }
}

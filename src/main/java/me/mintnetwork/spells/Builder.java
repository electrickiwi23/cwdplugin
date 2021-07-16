package me.mintnetwork.spells;

import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;

public class Builder extends KitItems {


    public Builder(){
        ultTime = Utils.BUILDER_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();


        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.QUICK_BUILD_COST);
        lore.add(ChatColor.GRAY +"Cause a line of blocks to shoot");
        lore.add(ChatColor.GRAY +"from the ground or walls.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Quick Build"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.POPUP_TOWER_COST);
        lore.add(ChatColor.GRAY +"Build a tower that can be used for ");
        lore.add(ChatColor.GRAY +"shelter or to shape terrain.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Pop up Tower"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.BUILD_BOLT_COST);
        lore.add(ChatColor.GRAY +"Shoot a projectile that creates blocks ");
        lore.add(ChatColor.GRAY +"underneath as it travels.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Bridge Bolt"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY +"Build a large wall to separate enemy teams ");
        lore.add(ChatColor.GRAY +"and make your position more secure.");
        meta.setDisplayName(ChatColor.GOLD+"Netherite Wall");
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Gets a wool block of your team's");
        lore.add(ChatColor.GRAY + "color every two seconds, max 1 stack.");
        meta.setDisplayName(ChatColor.WHITE + "Builder's Materials");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Dominate the terrain by shaping the");
        lore.add(ChatColor.GRAY + "environment to your teams advantage.");

        menuItem.setType(Material.CRAFTING_TABLE);
        meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW+"Builder");
        meta.setLore(lore);
        menuItem.setItemMeta(meta);

        //create itemstacks for each wand of the class
    }
    public static void QuickBuild(Player p, Plugin plugin, BlockFace face, Block block) {
        if (Mana.spendMana(p, Utils.QUICK_BUILD_COST)) {
            final Location[] current = {block.getLocation().add(face.getDirection().normalize())};
            final int[] count = {0};

            String TeamName = TeamsInit.getTeamName(p);
            Material color = Material.GRAY_WOOL;
            switch (TeamName) {
                case "blue":
                    color = Material.BLUE_WOOL;
                    break;
                case "red":
                    color = Material.RED_WOOL;
                    break;
                case "green":
                    color = Material.LIME_WOOL;
                    break;
                case "yellow":
                    color = Material.YELLOW_WOOL;
                    break;
            }


            Material finalColor = color;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (current[0].getBlock().isPassable()) {

                        current[0].getBlock().setType(finalColor);

                        current[0].getWorld().playSound(current[0].clone().add(.5,.5,.5), Sound.BLOCK_WOOL_PLACE,.8F,1);
                        new DecayBlock(100,.6F,current[0].getBlock());

                        for (Entity e : block.getWorld().getNearbyEntities(current[0].clone().add(.5, .5, .5), .6, .5, .6)) {
                            if (e instanceof LivingEntity) e.setVelocity(face.getDirection());
                        }

                        current[0] = current[0].add(face.getDirection().normalize());

                        count[0]++;
                        if (count[0] >=4){
                            this.cancel();
                        }
                    }else{
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin,1,1);
        }
    }

    public static void PopUpTower(Player p, Plugin plugin, Block block) {
        if (Mana.spendMana(p, Utils.POPUP_TOWER_COST)) {
            String TeamName = TeamsInit.getTeamName(p);
            Material concrete = Material.GRAY_CONCRETE_POWDER;
            switch (TeamName) {
                case "blue":
                    concrete = Material.BLUE_CONCRETE_POWDER;
                    break;
                case "red":
                    concrete = Material.RED_CONCRETE_POWDER;
                    break;
                case "green":
                    concrete = Material.LIME_CONCRETE_POWDER;
                    break;
                case "yellow":
                    concrete = Material.YELLOW_CONCRETE_POWDER;
                    break;
            }
            Material finalConcrete = concrete;
            for (int i = 1; i < 6; i++) {
                int finalI = i;
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        block.getWorld().playSound(block.getLocation().add(.5,.5,.5).add(0,finalI,0),Sound.BLOCK_SAND_PLACE,1,1);
                        block.getWorld().playSound(block.getLocation().add(.5,.5,.5).add(0,finalI,0),Sound.BLOCK_STONE_PLACE,1,1);
                        for (int j = 0; j < 5; j++) {
                            for (int k = 0; k < 5; k++) {
                                Block place = block.getLocation().add(j - 2, finalI, k - 2).getBlock();

                                if (place.isPassable()) {

                                    if (Math.abs(j - 2) == 2 && Math.abs(k - 2) == 0) {
                                        place.setType(Material.SCAFFOLDING);
                                    } else if (Math.abs(j - 2) == 0 && Math.abs(k - 2) == 2) {
                                        place.setType(Material.SCAFFOLDING);
                                    } else {
                                        place.setType(finalConcrete);

                                    }
                                    new DecayBlock(305- finalI ,1,place);
                                }


                            }
                        }
                        for (Entity e : block.getWorld().getNearbyEntities(block.getLocation().add(.5, .5 + finalI, .5), 2.5, 1.5, 2.5)) {
                            if (finalI == 5) {
                                if (e instanceof LivingEntity) e.setVelocity(new Vector(0, .6, 0));
                            }else {
                                if (e instanceof LivingEntity) e.setVelocity(new Vector(0, 1.2, 0));
                            }
                        }
                    }
                }, i);
            }
        }
    }

    public static void BuildBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, Utils.BUILD_BOLT_COST)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
            bolt.teleport(bolt.getLocation().add(0,-1,0));
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(bolt, p.getEyeLocation().getDirection().multiply(.4));
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            bolt.setVelocity(p.getEyeLocation().getDirection().multiply(.4));

            String TeamName = TeamsInit.getTeamName(p);

            if (TeamName.equals("blue")){
                bolt.setItem(new ItemStack(Material.BLUE_WOOL));
            } else if (TeamName.equals("red")){
                bolt.setItem(new ItemStack(Material.RED_WOOL));
            } else if (TeamName.equals("green")){
                bolt.setItem(new ItemStack(Material.GREEN_WOOL));
            } else if (TeamName.equals("yellow")){
                bolt.setItem(new ItemStack(Material.YELLOW_WOOL));
            } else {
                bolt.setItem(new ItemStack(Material.GRAY_WOOL));
            }

            bolt.setGravity(false);
            ID.put(bolt, "BuildBolt");
            p.teleport(p.getLocation().add(0, 1.5, 0));

            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    Vector v = velocity.get(bolt);
                    bolt.setVelocity(v);
                    Block b = bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock();
                    if (b.isPassable()) {
                        if (TeamName.equals("blue")){
                            b.setType(Material.BLUE_WOOL);
                        } else if (TeamName.equals("red")){
                            b.setType(Material.RED_WOOL);
                        } else if (TeamName.equals("green")){
                            b.setType(Material.LIME_WOOL);
                        } else if (TeamName.equals("yellow")){
                            b.setType(Material.YELLOW_WOOL);
                        } else {
                            b.setType(Material.GRAY_WOOL);
                        }

                        new DecayBlock(100,1,b);

                    }
                }
            }, 3, 1));
            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    tick.get(bolt).cancel();
                    bolt.remove();
                }
            }, 80);

        }
    }

    public static void ObsidianWall(Player p, Plugin plugin) {
        if (Ultimate.spendUlt(p)) {
            Map<Block, Integer> decay = StatusEffects.ObsidianDecay;
            Location l = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(5));
            l.getBlock().setType(Material.OBSIDIAN);
            l.setYaw(l.getYaw() + 90);
            l.setPitch(0);
            final int[] i = {0};

            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    Location current = l.clone();
                    current.add(l.getDirection().multiply(i[0]));
                    current.add(0, -6, 0);
                    for (int j = 0; j < 13; j++) {
                        current.getBlock().setType(Material.NETHERITE_BLOCK);
                        decay.put(current.getBlock(), (int) (Math.random() * 160 - 80));
                        current = current.add(0, 1, 0);
                    }
                    current = l.clone();
                    current.add(l.getDirection().multiply(i[0]).multiply(-1));
                    current.add(0, -6, 0);
                    for (int j = 0; j < 13; j++) {
                        current.getBlock().setType(Material.NETHERITE_BLOCK);
                        decay.put(current.getBlock(), (int) (Math.random() * 160 - 80));
                        current = current.add(0, 1, 0);
                    }
                    i[0]++;

                }
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> task.cancel(), 12);
        }
    }
}

package me.mintnetwork.spells;

import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Map;

public class Builder {
    public static void QuickBuild(Player p, Plugin plugin, BlockFace face, Block block) {
        if (Mana.spendMana(p, 1)) {
            final Location[] current = {block.getLocation().add(face.getDirection().normalize())};
            final int[] count = {0};
            String TeamName = TeamsInit.getTeamName(p);
            BukkitTask tick = new BukkitRunnable() {
                @Override
                public void run() {
                    if (current[0].getBlock().isPassable()) {
                        if (TeamName.equals("blue")) {
                            current[0].getBlock().setType(Material.BLUE_WOOL);
                        } else if (TeamName.equals("red")) {
                            current[0].getBlock().setType(Material.RED_WOOL);
                        } else if (TeamName.equals("green")) {
                            current[0].getBlock().setType(Material.LIME_WOOL);
                        } else if (TeamName.equals("yellow")) {
                            current[0].getBlock().setType(Material.YELLOW_WOOL);
                        } else {
                            current[0].getBlock().setType(Material.GRAY_WOOL);
                        }

                        current[0] = current[0].add(face.getDirection().normalize());

                        for (Entity e : block.getWorld().getNearbyEntities(current[0].clone().add(.5, .5, .5), .5, .5, .5)) {
                            if (e instanceof LivingEntity) e.setVelocity(face.getDirection());
                        }

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

    public static void PopUpTower(Player p, Plugin plugin, BlockFace face, Block block) {
        if (Mana.spendMana(p, 4)) {
            String TeamName = TeamsInit.getTeamName(p);
            for (int i = 1; i < 6; i++) {
                int finalI = i;
                Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < 5; j++) {
                            for (int k = 0; k < 5; k++) {
                                Block place = block.getLocation().add(j - 2, finalI, k - 2).getBlock();

                                if (place.isPassable()) {

                                    if (Math.abs(j - 2) == 2 && Math.abs(k - 2) == 0) {
                                        place.setType(Material.SCAFFOLDING);
                                    } else if (Math.abs(j - 2) == 0 && Math.abs(k - 2) == 2) {
                                        place.setType(Material.SCAFFOLDING);
                                    } else {
                                        if (TeamName.equals("blue")){
                                            place.setType(Material.BLUE_CONCRETE_POWDER);
                                        } else if (TeamName.equals("red")){
                                            place.setType(Material.RED_CONCRETE_POWDER);
                                        } else if (TeamName.equals("green")){
                                            place.setType(Material.LIME_CONCRETE_POWDER);
                                        } else if (TeamName.equals("yellow")){
                                            place.setType(Material.YELLOW_CONCRETE_POWDER);
                                        } else {
                                            place.setType(Material.GRAY_CONCRETE_POWDER);
                                        }
                                    }
                                }


                            }
                        }
                        for (Entity e : block.getWorld().getNearbyEntities(block.getLocation().add(.5, .5 + finalI, .5), 2.5, .5, 2.5)) {
                            if (finalI == 5) {
                                if (e instanceof LivingEntity) e.setVelocity(new Vector(0, .6, 0));
                            }else {
                                if (e instanceof LivingEntity) e.setVelocity(new Vector(0, 1, 0));
                            }
                        }
                    }
                }, i);
            }
        }
    }

    public static void BuildBolt(Player p, Plugin plugin) {
        if (Mana.spendMana(p, 3)) {
            Snowball bolt = p.launchProjectile(Snowball.class);
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
            p.teleport(p.getLocation().add(0, 2.5, 0));

            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            tick.put(bolt, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
                    Vector v = velocity.get(bolt);
                    bolt.setVelocity(v);
                    if (bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().isPassable()) {
                        if (TeamName.equals("blue")){
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.BLUE_WOOL);
                        } else if (TeamName.equals("red")){
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.RED_WOOL);
                        } else if (TeamName.equals("green")){
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.LIME_WOOL);
                        } else if (TeamName.equals("yellow")){
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.YELLOW_WOOL);
                        } else {
                            bolt.getLocation().subtract(v).subtract(v).subtract(v).getBlock().setType(Material.GRAY_WOOL);
                        }

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

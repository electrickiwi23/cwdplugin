package me.mintnetwork.listeners;

import me.mintnetwork.Main;
import me.mintnetwork.Objects.DecayBlock;
import me.mintnetwork.Objects.Shield;
import me.mintnetwork.Objects.Wizard;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.repeaters.BlockDecay;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.Hunter;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class BowShootListener implements Listener {

    private final Main plugin;

    public BowShootListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            if (ChatColor.stripColor(event.getBow().getItemMeta().getDisplayName()).equals("Hunter's Bow")) {
            Player p = (Player) event.getEntity();
            Wizard w = WizardInit.playersWizards.get(p.getUniqueId());
            ((Arrow) event.getProjectile()).setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
            ((Arrow) event.getProjectile()).setDamage(((Arrow) event.getProjectile()).getDamage()*.7);

            switch (w.nextArrow) {
                case BOMB:
                    if (Mana.spendMana(p, Utils.BOMB_ARROW_COST)) {
                        Arrow arrow = (Arrow) event.getProjectile();
                        BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Vector v = arrow.getVelocity().normalize().multiply(-.5);
                                arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation().add(v), 0, v.getX(), v.getY(), v.getZ(), 1,null,true);
                            }
                        }, 2, 2);
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                arrow.getWorld().createExplosion(arrow.getLocation().add(arrow.getVelocity().normalize().multiply(-.5)),3);
                                task.cancel();
                                arrow.remove();

                            }
                        },80);
                        Hunter.deselectArrow(p);
                    } else{
                        event.setCancelled(true);
                    }

                    break;
                case SCAN:
                    if (Mana.spendMana(p, Utils.SCAN_ARROW_COST)) {
                        Arrow arrow = (Arrow) event.getProjectile();
                        BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Vector v = arrow.getVelocity().normalize().multiply(-.5);
                                arrow.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, arrow.getLocation().add(v), 0, v.getX(), v.getY(), v.getZ(), 1,new Particle.DustTransition(Color.AQUA, Color.BLACK,2),true);
                            }
                        }, 2, 2);
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                arrow.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,arrow.getLocation().add(arrow.getVelocity().normalize().multiply(-.5)),1,0,0,0,0,null,true);
                                for (Entity e : arrow.getWorld().getNearbyEntities(arrow.getLocation(), 15, 15, 15)) {
                                    if (arrow.getLocation().distance(e.getLocation()) <= 14) {
                                        if (e instanceof LivingEntity) {
                                            if (!(e instanceof ArmorStand)) {
                                                if (!TeamsInit.getTeamName(e).matches(TeamsInit.getTeamName(p))) {
                                                    ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));
                                                }
                                            }
                                        }
                                    }
                                }
                                task.cancel();
                                arrow.remove();

                            }
                        },80);
                        Hunter.deselectArrow(p);
                    } else{
                        event.setCancelled(true);
                    }

                    break;
                case DECAY:
                    if (Mana.spendMana(p, Utils.DECAY_ARROW_COST)) {
                        Arrow arrow = (Arrow) event.getProjectile();
                        ProjectileInfo.projectileID.put(arrow,"Mark Arrow");
                        Hunter.deselectArrow(p);
                    } else{
                        event.setCancelled(true);
                    }
                    break;
                case LIGHT:
                    if (Ultimate.spendUlt(p)){
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {


                                Location current = p.getEyeLocation().add(p.getEyeLocation().getDirection());
                                Vector direction = p.getEyeLocation().getDirection();
                                int range = 0;
                                while (true) {
                                    if (current.isWorldLoaded()) {
                                        RayTraceResult ray = p.getWorld().rayTraceEntities(current,direction,2,.4);
                                        if (ray != null) {
                                            Location hitLocation = null;
                                            LivingEntity hitEntity = null;
                                            try {
                                                hitEntity = (LivingEntity) ray.getHitEntity();
                                            } catch (Exception ignore) {
                                            }
                                            try {
                                                hitLocation = ray.getHitPosition().toLocation(p.getWorld());
                                            } catch (Exception ignore) {
                                            }
                                            if (hitLocation != null && hitEntity == null) {
                                                if (BlockDecay.decay.containsKey(ray.getHitBlock())) {
                                                    DecayBlock block = BlockDecay.decay.get(ray.getHitBlock());
                                                    block.damage(30);
                                                    block.setForceful(true);
                                                }
                                                p.getWorld().spawnParticle(Particle.END_ROD, hitLocation, 1, 0, 0, 0, 0,null,true);
                                                break;
                                            }
                                            if (hitEntity != null && (!(hitEntity == p && range <= 2))) {

                                                p.getWorld().spawnParticle(Particle.END_ROD, hitLocation, 1, 0, 0, 0, 0);



                                                LivingEntity v = hitEntity;
                                                final Location finalCurrent = current.clone().setDirection(direction.clone());
                                                for (int i = 0; i < 5; i++) {
                                                    v.getWorld().playSound(v.getLocation(), Sound.BLOCK_GLASS_BREAK, .5f, 1);
                                                }

                                                v.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20,0));
                                                BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        v.setVelocity(new Vector(0,0,0));
                                                    }
                                                },1,1);
                                                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                                                        stand.setInvisible(true);
                                                        stand.setMarker(true);
                                                        stand.setCustomNameVisible(false);
                                                        stand.setCustomName(p.getDisplayName() + "'s Arrow of light");
                                                        TeamsInit.addToTeam(stand, TeamsInit.getTeamName(p));
                                                        v.damage(16, stand);

                                                        stand.remove();



                                                        for (int i = 1; i < 5; i++) {
                                                            v.getWorld().spawnParticle(Particle.ITEM_CRACK,finalCurrent.add(finalCurrent.getDirection().multiply(i/2)),3,.1,.1,.1,0,new ItemStack(Material.RED_DYE));

                                                        }

                                                        task.cancel();
                                                    }
                                                },20);





                                            }
                                        }
                                        current = current.add(direction);
                                        p.getWorld().spawnParticle(Particle.END_ROD, current, 1, 0, 0, 0, 0);

                                        range++;
                                        if (range >= 60) break;

                                        //code for shield reflection--------------------------------------------------------
                                        for (Entity shield : Shield.shieldMap.keySet()) {
                                            if (shield.getLocation().distance(current) < Shield.shieldMap.get(shield).getRadius() + .5) {
                                                direction = Shield.shieldMap.get(shield).reflectVector(current, direction);
                                            }
                                        }
                                        //----------------------------------------------------------------------------------
                                    } else {
                                        break;
                                    }

                                }
                            }
                        },2);
                        Hunter.deselectArrow(p);
                    }
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}

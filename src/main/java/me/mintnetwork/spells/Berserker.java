package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.VortexEffect;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Berserker {
    public static void SwordLunge(Player p){
        if (Mana.spendMana(p,2)){
         p.setVelocity(p.getEyeLocation().getDirection().add(new Vector(0,.4,0)).normalize().multiply(1.2));

        }
    }

    public static void SpeedBoost(Player p) {
        if (Mana.spendMana(p, Utils.SPEED_BOOST_COST)) {
            Map<LivingEntity, Integer> speedMap = StatusEffects.speedTimer;
            if (!speedMap.containsKey(p)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, false, true));
                speedMap.put(p, 30);

            }
        }
    }

    public static void ForcePull(Player p, Plugin plugin, EffectManager em) {
        if (Mana.spendMana(p, Utils.FORCE_PULL_COST)) {
        int distance = 12;
        Location start = p.getEyeLocation();
        RayTraceResult ray = p.getWorld().rayTraceBlocks(start, start.getDirection(), distance, FluidCollisionMode.NEVER, true);
        if (ray != null) {
            distance = (int) Math.ceil(ray.getHitPosition().distance(start.toVector()));
        }

        final Location[] Current = {p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(distance)).setDirection(p.getEyeLocation().getDirection().multiply(-1))};
        VortexEffect effect = new VortexEffect(em);
        effect.setLocation(Current[0]);
        effect.radius = 3;
        effect.circles = 20;
        effect.iterations = distance;
        effect.particle = Particle.BUBBLE_POP;
        List<Entity> CantHit = new ArrayList<>();
        CantHit.add(p);

        em.start(effect);
        final int[] count = {0};
        int finalDistance = distance;
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                count[0]++;
                for (Entity e : Current[0].getWorld().getNearbyEntities(Current[0], 3, 3, 3)) {
                    if (e instanceof LivingEntity) {
                        if (!(e instanceof ArmorStand)) {
                            if (!CantHit.contains(e)) {
                                e.setVelocity(e.getVelocity().add(e.getLocation().toVector().subtract(start.toVector()).multiply(-1).add(new Vector(0, 1, 0)).normalize().multiply(1.3)));
                                CantHit.add(e);
                            }
                        }
                    }
                }
                Current[0] = Current[0].add(Current[0].getDirection());
                if (count[0] >= finalDistance) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 1, 1);
    }
}

    public static void RageUlt(Player p) {
        if (Ultimate.spendUlt(p)) {
            for (ItemStack i : p.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType().equals(Material.IRON_SWORD)) {
                        i.addEnchantment(Enchantment.KNOCKBACK, 1);
                    }
                }
            }
            p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_RAVAGER_ROAR, 3, 1);
            if (StatusEffects.RageUlt.containsKey(p)) {
                StatusEffects.RageUlt.replace(p, 150);
            } else {
                StatusEffects.RageUlt.put(p, 150);
            }
        }
    }
}

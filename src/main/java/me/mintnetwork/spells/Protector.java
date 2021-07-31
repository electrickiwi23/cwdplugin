package me.mintnetwork.spells;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.SphereEffect;
import me.mintnetwork.Objects.Shield;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;

public class Protector extends KitItems {


    public Protector(){
        ultTime = Utils.PROTECTOR_ULT_TIME;

        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();


        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SLAM_COST);
        lore.add(ChatColor.GRAY +"Jump up and slam the ground dealing");
        lore.add(ChatColor.GRAY +"damage to anyone near you.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Aerial Slam"));
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.SHIELD_DOME_COST);
        lore.add(ChatColor.GRAY +"Create a magical dome which ");
        lore.add(ChatColor.GRAY +"blocks all projectiles");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Dome of Safety"));
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.GIVE_ARMOR_COST);
        lore.add(ChatColor.GRAY +"Gives a teammate extra hearts that");
        lore.add(ChatColor.GRAY +"will take damage and knockback for them.");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET+("Crystal Armor"));
        wand3.setItemMeta(meta);
        wands.add(wand3);

        lore.add(ChatColor.GRAY +"Gives you a large amount of hearts");
        lore.add(ChatColor.GRAY +"and for a short time you take damage");
        lore.add(ChatColor.GRAY +"in place of nearby teammates.");
        meta.setDisplayName(ChatColor.GOLD+("Aura of Protection"));
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "You have 2 hearts more health,");
        lore.add(ChatColor.GRAY + "but move 15% slower.");
        meta.setDisplayName(ChatColor.WHITE + "Reinforced");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY +"Support and protect your teammates");
        lore.add(ChatColor.GRAY +"with a variety of magic");

        menuItem.setType(Material.SHIELD);
        meta = menuItem.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD +"Protector");
        meta.setLore(lore);
        menuItem.setItemMeta(meta);
        menuItem.setItemMeta(meta);

        //create itemstacks for each wand of the class
    }

    public static void Slam(Player p, Plugin plugin) {

        if (Mana.spendMana(p, Utils.SLAM_COST)) {

            Vector v = p.getEyeLocation().getDirection().setY(0).normalize();
            v.add(new Vector(v.getX() * 2, 2, v.getZ() * 2).normalize().multiply(1.2));
            p.setVelocity(v);
            StatusEffects.UsingMove.add(p);

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation().add(0, 1, 0), 2, -.2, .4, -.2, 0);
                    if (p.isOnGround()) {
                        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getLocation(), 50, 2, 0, 2, p.getLocation().add(0, -1, 0).getBlock().getBlockData());
                        p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation(), 40, 0, 0, 0, .3);
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BASALT_BREAK,.6F,1);
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GOAT_RAM_IMPACT,.6F,1);

                        StatusEffects.UsingMove.remove(p);
                        for (Entity e : p.getNearbyEntities(5, 2, 5)) {
                            if (e instanceof LivingEntity) {
                                if (!(e instanceof ArmorStand)) {
                                    if (!TeamsInit.getTeamName(e).equals(TeamsInit.getTeamName(p))){
                                        if (e.isOnGround()) {

                                        ((LivingEntity) e).damage(2, p);
                                        Vector v = e.getLocation().toVector().subtract(p.getLocation().toVector()).setY(0).normalize().multiply(1.6);
                                        e.setVelocity(new Vector(v.getX(), .5, v.getZ()));
                                        }
                                    }
                                }
                            }
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 3, 1);
        }


    }

    public static void ShieldDome(Player p, EffectManager em, Plugin plugin) {
        if (Mana.spendMana(p, Utils.SHIELD_DOME_COST)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();
            ID.put(stand, "ShieldDome");
            stand.setVisible(false);
            stand.setMarker(true);
            SphereEffect effect = new SphereEffect(em);
            effect.setLocation(p.getLocation().add(0, 0, 0));
            effect.particle = Particle.END_ROD;
            effect.particleCount = 1;
            effect.radius = 4;
            effect.iterations = 200;
            em.start(effect);

            Shield shield = new Shield(stand,4,plugin);

            Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    stand.remove();
                    shield.remove();
                }
            }, 200);
        }
    }

    public static void GiveArmor(Player p,EffectManager em,Plugin plugin){
        Player currentVictim = null;
        double currentAngle = 90;
        for (Player victim:Bukkit.getOnlinePlayers()) {
            if (TeamsInit.getTeamName(victim).equals(TeamsInit.getTeamName(p)) && p != victim) {
                if (p.hasLineOfSight(victim)) {
                    Vector direction = victim.getLocation().toVector().subtract(p.getLocation().toVector());
                    double angle = Math.toDegrees(p.getEyeLocation().getDirection().angle(direction));
                    if (angle < 20 && angle < currentAngle && victim.getEyeLocation().distance(p.getLocation()) < 30) {
                        currentAngle = angle;
                        currentVictim = victim;
                    }
                }
            }
        }


        if (currentVictim!=null && Mana.spendMana(p,Utils.GIVE_ARMOR_COST)){
           LineEffect line = new LineEffect(em);
           line.setLocation(p.getEyeLocation());
           line.setTargetEntity(currentVictim);
           line.particle = Particle.END_ROD;
           em.start(line);

           currentVictim.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,300,0));
           StatusEffects.EnergyShield.add(currentVictim);
           currentVictim.getWorld().playSound(currentVictim.getLocation(),Sound.BLOCK_AMETHYST_BLOCK_PLACE,1,1);
           currentVictim.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).addModifier(new AttributeModifier("EnergyShield",1,AttributeModifier.Operation.ADD_NUMBER));

        }
    }

    public static void ProtectionUlt(Player p){
        if (Ultimate.spendUlt(p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 4));
            StatusEffects.protectionAura.put(p, 200);
        }
    }
}

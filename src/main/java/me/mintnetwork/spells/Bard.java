package me.mintnetwork.spells;

import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.repeaters.Mana;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import me.mintnetwork.spells.projectiles.ProjectileInfo;
import me.mintnetwork.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;

public class Bard extends KitItems {

    public Bard(){
        ArrayList<String> lore = new ArrayList<>();

        ItemStack wand1 = new ItemStack(Material.STICK);
        ItemMeta meta = wand1.getItemMeta();

        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.HEAL_SONG_COST);
        lore.add("TEXT");
        meta.setLore(lore);
        lore.clear();
        meta.setDisplayName(ChatColor.RESET + "Song of Healing");
        wand1.setItemMeta(meta);
        wands.add(wand1);

        ItemStack wand2 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.HEAL_POTION_COST);
        lore.add(ChatColor.GRAY + "Throw a potion which heals");
        lore.add(ChatColor.GRAY + "allies within its radius over time.");
        meta.setDisplayName(ChatColor.RESET+("Healing Potion"));
        meta.setLore(lore);
        lore.clear();
        wand2.setItemMeta(meta);
        wands.add(wand2);

        ItemStack wand3 = new ItemStack(Material.STICK);
        lore.add(ChatColor.GREEN + "Mana Cost: " + Utils.DEBUFF_POTION_COST);
        lore.add(ChatColor.GRAY + "Throws a potion which slows and ");
        lore.add(ChatColor.GRAY + "weakens anyone within its radius.");
        meta.setDisplayName(ChatColor.RESET+("Plague Potion"));
        meta.setLore(lore);
        lore.clear();
        wand3.setItemMeta(meta);
        wands.add(wand3);


        lore.add(ChatColor.GRAY +"Throws a potion which shields ");
        lore.add(ChatColor.GRAY +"allies within its radius");
        meta.setDisplayName(ChatColor.RESET+("Elixir of Immortality"));
        meta.setLore(lore);
        ult.setItemMeta(meta);
        lore.clear();

        lore.add(ChatColor.GRAY + "Occasionally gets splash potions ");
        lore.add(ChatColor.GRAY + "to use on your allys and enemies.");
        meta.setDisplayName(ChatColor.RESET + "Mobile Brewery");
        meta.setLore(lore);
        passive.setItemMeta(meta);
        lore.clear();

        lore.add("Use powerful area of effect");
        lore.add("spells to control the battlefield");
        lore.add("and help your teammates.");

        menuItem.setType(Material.SPLASH_POTION);
        meta = menuItem.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setDisplayName(ChatColor.DARK_GREEN+"Alchemist");
        meta.setLore(lore);

        PotionMeta potionMeta = (PotionMeta) meta;
        potionMeta.setColor(Color.LIME);
        menuItem.setItemMeta(potionMeta);

        //create itemstacks for each wand of the class
    }
    public static void SpeedSong(Player p, Plugin plugin) {
        if (Mana.spendMana(p, Utils.SPEED_SONG_COST)) {
            String teamName = TeamsInit.getTeamName(p);
            for (Player victim:Bukkit.getOnlinePlayers() ) {
                if (victim.getLocation().distance(p.getLocation())<=10) {
                    String victimTeam = TeamsInit.getTeamName(victim);
                    if (teamName.matches(victimTeam)) {
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 140, 1, false, false));
                        if (StatusEffects.speedSong.containsKey(victim)) {
                            StatusEffects.speedSong.replace(victim, 70);
                        } else {
                            StatusEffects.speedSong.put(victim, 70);
                        }
                    }
                }
            }
            final int[] count = {0};
            BukkitTask task = new BukkitRunnable(){
                @Override
                public void run() {
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,(float).4, (float) 0.793701);
                    if (count[0] % 5==0){
                        p.getWorld().playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_BASEDRUM,(float).3, (float) .6);
                    }
                    if ((count[0] -2) % 5==0){
                        p.getWorld().playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_SNARE,(float).3, (float) .9);
                    }
                    count[0]++;
                    if (count[0] >=20){
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin,1,2);
        }
    }

    public static void HealSong(Player p,Plugin plugin) {
        if (Mana.spendMana(p, Utils.HEAL_SONG_COST)) {
            String teamName = TeamsInit.getTeamName(p);
            for (Player victim:Bukkit.getOnlinePlayers() ) {
                if (victim.getLocation().distance(p.getLocation())<=10) {
                    String victimTeam = TeamsInit.getTeamName(victim);
                    if (teamName.matches(victimTeam)) {
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0, false, false));
                        if (StatusEffects.healSong.containsKey(victim)) {
                            StatusEffects.healSong.replace(victim, 100);
                        } else {
                            StatusEffects.healSong.put(victim, 100);
                        }
                    }
                }
            }
            final int[] count = {0};
            BukkitTask task = new BukkitRunnable(){
                @Override
                public void run() {
                    p.getWorld().playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_FLUTE,(float).3, (float) 0.840896);
                    if (count[0] > 15){
                        p.getWorld().playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_FLUTE,(float).3, (float) 0.667420);
                    }
                    if (count[0] > 30){
                        p.getWorld().playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_FLUTE,(float).3, (float) 1);
                    }
                    count[0]++;
                    if (count[0] >=60){
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin,1,1);
        }
    }

    public static void StunSong(Player p, Plugin plugin){
        if (Mana.spendMana(p, Utils.STUN_SONG_COST)) {
            ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
            stand.setMarker(true);
            stand.setInvisible(true);
            Snowball grenade = p.launchProjectile(Snowball.class);
            Map<Entity, Vector> velocity = ProjectileInfo.getLockedVelocity();
            velocity.put(grenade, p.getEyeLocation().getDirection());
            Map<Entity, String> ID = ProjectileInfo.getProjectileID();

            grenade.setItem(new ItemStack(Material.NOTE_BLOCK));
            grenade.setBounce(true);

            Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
            linked.put(stand, grenade);
            linked.put(grenade, stand);
            ID.put(stand, "SongGrenade");
            Map<Entity, BukkitTask> tick = ProjectileInfo.getTickCode();
            Map<Entity, BukkitTask> activate = ProjectileInfo.getActivateCode();
            final int[] count = {0};
            tick.put(stand, Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    Map<Entity, Entity> linked = ProjectileInfo.getLinkedSnowball();
                    stand.teleport(linked.get(stand));
                    count[0]++;
                    if (count[0] >=5){
                        stand.getWorld().spawnParticle(Particle.NOTE, stand.getLocation(), 0, .6, .2, .92,1, null);
                        count[0] = 0;
                    }
                }
            }, 1, 1));



        }
    }
        public static void SirenSong(Player p, Plugin plugin) {
            if (Ultimate.spendUlt(p)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 300, 2));
                StatusEffects.sirenSong.put(p, 150);

                final int[] count = {0};
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, (float) 1.6, (float) 0.840896);
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, (float) 1.6, (float) 0.943874);
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, (float) 1.6, (float) 1.122462);
                        count[0]++;
                        if (count[0] >= 240) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 1, 1);

            }
        }
}

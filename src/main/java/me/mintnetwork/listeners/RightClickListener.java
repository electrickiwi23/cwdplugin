package me.mintnetwork.listeners;

import de.slikey.effectlib.EffectManager;
import me.mintnetwork.Main;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.spells.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RightClickListener implements Listener {

    private final Main plugin;

    public RightClickListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Map<UUID, Long> lastUsed = new HashMap<>();


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        EffectManager em = new EffectManager(plugin);

        if (lastUsed.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - lastUsed.get(p.getUniqueId()) < 380) {
                return;
            }
        }
        lastUsed.put(p.getUniqueId(), System.currentTimeMillis());
        if (!p.getInventory().getItemInMainHand().equals(new ItemStack(Material.AIR, 0))) {
            if (event.getHand() != null) {
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
//                                if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                            if (StatusEffects.CanCast(p)) {
                                if (Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).getDisplayName().contains("Blood Link Ritual")) BloodMage.BloodUlt(p,em);

                                switch(ChatColor.stripColor(Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).getDisplayName())){
                                    case ("Lightning Bolt"):
                                        SpellSlinger.LightningBolt(p,plugin,em);
                                        break;
                                    case ("Fire Bolt"):
                                        SpellSlinger.FireBolt(p,plugin);
                                        break;
                                    case ("Ice Bolt"):
                                        SpellSlinger.SnowBolt(p,plugin);
                                        break;
                                    case ("Elemental Blast"):
                                        SpellSlinger.ElementBlast(p,plugin,em);
                                        break;

                                    case "Berserker Blade":
                                        Berserker.SwordLunge(p);
                                        break;
                                    case ("Speed Rush"):
                                        Berserker.SpeedBoost(p);
                                        break;
                                    case ("Force Pull"):
                                        Berserker.ForcePull(p,plugin,em);
                                        break;
                                    case ("Unleash Rage"):
                                        Berserker.RageUlt(p);
                                        break;

                                    case ("Void Rift"):
                                        if (event.getClickedBlock() != null) {
                                            PillarMan.VoidPillar(p, plugin, event.getClickedBlock(), event.getBlockFace(), em);
                                        }
                                        break;
                                    case ("Beam Pillar"):
                                        if (event.getClickedBlock() != null) {
                                            PillarMan.BeamPillar(p, plugin, event.getClickedBlock(), event.getBlockFace());
                                        }
                                        break;
                                    case ("Mana Pillar"):
                                        if (event.getClickedBlock() != null) {
                                            PillarMan.ManaPillar(p, plugin, event.getClickedBlock(), event.getBlockFace(), em);
                                        }
                                        break;
                                    case ("Skeleton Spirit"):
                                        if (event.getClickedBlock() != null) {
                                            PillarMan.ArrowTurret(p, plugin, event.getClickedBlock(), event.getBlockFace(), em);
                                        }
                                        break;

                                    case ("Heal Bolt"):
                                        Cleric.HealBolt(p,plugin);
                                        break;
                                    case ("Healing Pillar"):
                                        if (event.getClickedBlock() != null) {
                                            Cleric.HealPillar(p, plugin, event.getBlockFace(),event.getClickedBlock());
                                        }
                                        break;
                                    case ("Purification Wave"):
                                        Cleric.PurificationWave(p,plugin,em);
                                        break;
                                    case ("Divine Intervention"):
                                        Cleric.TeamHeal(p);
                                        break;

                                    case("TNT Line"):
                                        Demolitionist.TNTLine(p,plugin);
                                        break;
                                    case("TNT Grenade"):
                                        Demolitionist.TNTGrenade(p,plugin);
                                        break;
                                    case("Sticky Grenade"):
                                        Demolitionist.StickyTNTGrenade(p,plugin);
                                        break;
                                    case("Cluster Bomb"):
                                        Demolitionist.ClusterBomb(p,plugin);
                                        break;

                                    case("Quick Build"):
                                        if (event.getClickedBlock() != null) {
                                            Builder.QuickBuild(p, plugin, event.getBlockFace(), event.getClickedBlock());
                                        }
                                        break;
                                    case("Pop up Tower"):
                                        if (event.getClickedBlock() != null) {
                                            Builder.PopUpTower(p, plugin, event.getClickedBlock());
                                        }
                                        break;
                                    case("Bridge Bolt"):
                                        Builder.BuildBolt(p,plugin);
                                        break;
                                    case("Netherite Wall"):
                                        Builder.ObsidianWall(p,plugin);
                                        break;

                                    case("Blood Bolt"):
                                        BloodMage.BloodBolt(p,plugin);
                                        break;
                                    case("Life Sacrifice"):
                                        BloodMage.BloodSacrifice(p);
                                        break;
                                    case("Blood Tracker"):
                                        BloodMage.BloodTracker(p,plugin);
                                        break;

                                    case("Darkness Camouflage"):
                                        Shadow.ShadowInvis(p,plugin);
                                        break;
                                    case("Shadow Dash"):
                                        Shadow.ShadowRetreat(p,plugin);
                                        break;
                                    case("Pray Abduction"):
                                        Shadow.ShadowGrapple(p,plugin);
                                        break;
                                    case("Consuming Midnight"):
                                        Shadow.ConsumingNight(p,plugin,em);
                                        break;

                                    case("Spray Paint"):
                                        Painter.SprayPaint(p,plugin);
                                        break;
                                    case("Paint Canister"):
                                        Painter.PaintBomb(p,plugin);
                                        break;
                                    case("Brush Stroke"):
                                        Painter.BrushStroke(p,plugin);
                                        break;
                                    case("Paint Activate"):
                                        Painter.PaintActivateUlt(p,plugin);
                                        break;

                                    case("Cloud Burst"):
                                        Aviator.CloudBurst(p,plugin);
                                        break;
                                    case("Air Dash"):
                                        Aviator.AirDash(p,plugin);
                                        break;
                                    case("Air Needles"):
                                        Aviator.AirNeedles(p,em,plugin);
                                        break;
                                    case("Tornado Blast"):
                                        Aviator.TornadoBlast(p,plugin);
                                        break;

                                    case("Song of Healing"):
                                        Bard.HealSong(p,plugin);
                                        break;
                                    case("Song of Haste"):
                                        Bard.SpeedSong(p,plugin);
                                        break;
                                    case("Concussion Ring"):
                                        Bard.StunSong(p,plugin);
                                        break;
                                    case("Siren Song"):
                                        Bard.SirenSong(p,plugin);
                                        break;

                                    case("Sniper Bolt"):
                                        Tactician.SniperBolt(p,plugin);
                                        break;
                                    case("Molotov Cocktail"):
                                        Tactician.Molotov(p,plugin);
                                        break;
                                    case("Grapple Hook"):
                                        Tactician.GrappleHook(p,plugin);
                                        break;
                                    case("Air Strike"):
                                        Tactician.AirStrike(p,plugin);
                                        break;

                                    case("Acid Vial"):
                                        Alchemist.AcidPotion(p);
                                        break;
                                    case("Healing Potion"):
                                        Alchemist.HealPotion(p);
                                        break;
                                    case("Plague Potion"):
                                        Alchemist.DebuffPotion(p);
                                        break;
                                    case("Elixir of Immortality"):
                                        Alchemist.ImmortalPotionUlt(p);
                                        break;

                                    case ("Arial Slam" ):
                                        Protector.Slam(p,plugin);
                                        break;
                                    case ("Dome of Safety"):
                                        Protector.ShieldDome(p,em,plugin);
                                        break;
                                    case ("Armor Aura"):
                                        Protector.GiveArmor(p,em,plugin);
                                        break;
                                    case ("Aura of Protection"):
                                        Protector.ProtectionUlt(p);
                                        break;

                                    case ("Tripwire"):
                                        if (event.getClickedBlock() != null) {
                                            Trapper.tripwire(p, plugin, event.getBlockFace(), event.getClickedBlock());
                                        }
                                        break;

                                    case ("Create Clone"):
                                        RealityShifter.CreateClone(p);
                                        break;


                                    case("Firework Bolt"):
                                        GenericCast.FireworkBolt(p);
                                        break;
                                    case("Jump Boost"):
                                        GenericCast.JumpBoost(p);
                                        break;
                                    case("Engine Blast"):
                                        GenericCast.EngineBurst(p);
                                        break;
                                    case("Dragon Orb"):
                                        GenericCast.DragonGrenade(p,plugin);
                                        break;
                                    case("Bat Sonar"):
                                        GenericCast.BatSonar(p,plugin);
                                        break;
                                    case("TNT Ring"):
                                        GenericCast.TNTRing(p);
                                        break;
                                    case("Hive Bolt"):
                                        GenericCast.BeeBolt(p,plugin);
                                        break;
                                    case("Black Hole"):
                                        GenericCast.BlackHole(p,plugin,em);
                                        break;
                                    case("End Warp"):
                                        GenericCast.EndWarp(p,plugin);
                                        break;
                                    case("Baby Boomer"):
                                        GenericCast.ChildBomber(p,plugin);
                                        break;
                                    case("Zombie Summon"):
                                        GenericCast.ZombieSpawn(p,plugin);
                                        break;
                                    case("Slime Ball"):
                                        GenericCast.SlimeBomb(p,plugin);
                                        break;
                                    case("Flash Step"):
                                        GenericCast.VoltStep(p,plugin);
                                        break;
                                    case("Shoulder Blitz"):
                                        GenericCast.BoostSlam(p,plugin);
                                        break;
                                    case("Anvil Toss"):
                                        GenericCast.AnvilLaunch(p,plugin);
                                        break;
                                    case("Storm Strike"):
                                        GenericCast.StormStrike(p,plugin);
                                        break;
                                    case("Mana Bullet"):
                                        GenericCast.ManaBullet(p);
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
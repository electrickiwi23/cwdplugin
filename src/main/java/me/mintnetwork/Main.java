package me.mintnetwork;

import me.mintnetwork.commands.DebugCommand;
import me.mintnetwork.commands.GiveWand;
import me.mintnetwork.listeners.*;
import me.mintnetwork.initialization.ScoreboardInit;
import me.mintnetwork.listeners.DamageListener;
import me.mintnetwork.listeners.ProjectileHitListener;
import me.mintnetwork.listeners.RightClickListener;
import me.mintnetwork.repeaters.Mana;
//import me.mintnetwork.spells.projectiles.BloodBolt;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.repeaters.Ultimate;
import org.bukkit.plugin.java.JavaPlugin;
//import com.aim.coltonjgriswold.ParticleProjectileApi;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new GiveWand(this);
        new RightClickListener(this);
        Mana manaGen = new Mana(this);
        manaGen.mana(this);
        StatusEffects statuses = new StatusEffects(this);
        statuses.statusEffects(this);
        new DebugCommand(this);
        new EntityDamageListener(this);
        new ProjectileHitListener(this);
        new EntityDieListener(this);
        new PlayerHoldsItemListener(this);
        new PlayerSneakListener(this);
        new TakeDamageListener(this);

        new ScoreboardInit(this);
        new Ultimate(this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
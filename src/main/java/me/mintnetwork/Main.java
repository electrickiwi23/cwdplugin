package me.mintnetwork;

import de.slikey.effectlib.EffectManager;
import me.mintnetwork.commands.DebugCommand;
import me.mintnetwork.commands.GiveWand;
import me.mintnetwork.listeners.ProjectileHitListener;
import me.mintnetwork.listeners.RightClickListener;
import me.mintnetwork.repeaters.Mana;
//import me.mintnetwork.spells.projectiles.BloodBolt;
import org.bukkit.Particle;
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
        new DebugCommand(this);
        new ProjectileHitListener(this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}

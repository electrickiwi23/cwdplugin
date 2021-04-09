package me.mintnetwork;

import me.mintnetwork.commands.*;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.listeners.*;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.listeners.ProjectileHitListener;
import me.mintnetwork.listeners.RightClickListener;
//import me.mintnetwork.spells.projectiles.BloodBolt;
import me.mintnetwork.repeaters.Passives;
import me.mintnetwork.repeaters.StatusEffects;
import me.mintnetwork.initialization.WizardInit;
import org.bukkit.plugin.java.JavaPlugin;



public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new GiveWand(this);
        new RightClickListener(this);
        new CancelTill(this);
        new GameStart(this);
        WizardInit wizardInit = new WizardInit();
        wizardInit.WizardInitialization(this);
        Passives.PassivesStart(this);
        StatusEffects statuses = new StatusEffects();
        statuses.statusEffects(this);

        new ClassSelect(this);
        new DebugCommand(this);
        new Start(this);
        new EntityDamageListener(this);
        new ProjectileHitListener(this);
        new EntityDieListener(this);
        new PlayerJoinListener(this);
        new PlayerHoldsItemListener(this);
        new PlayerSneakListener(this);
        new TakeDamageListener(this);
        new EntityExplodeListener(this);
        getCommand("class").setTabCompleter(new ClassAutoCompleter());
        getCommand("givewands").setTabCompleter(new WandsAutoCompleter());
        new TeamsInit(this);
        //hello


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}

package me.mintnetwork;

import me.mintnetwork.commands.*;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.listeners.*;
import me.mintnetwork.repeaters.BlockDecay;
import me.mintnetwork.repeaters.Passives;
import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new RightClickListener(this);
        new CancelTill(this);
        new GameStart(this);
        WizardInit wizardInit = new WizardInit();
        wizardInit.WizardInitialization(this);
        Passives.PassivesStart(this);
        BlockDecay.decayRepeater(this);
        StatusEffects statuses = new StatusEffects();
        statuses.statusEffects(this);

        new ClassSelect(this);
        new DebugCommand(this);
        new Start(this);
        new GiveWand(this);
        new AssignTeams(this);

        getCommand("class").setTabCompleter(new ClassAutoCompleter());
        getCommand("givewands").setTabCompleter(new WandsAutoCompleter());
        getCommand("start").setTabCompleter(new StartAutoCompleter());

        new FireworkExpolodeListener(this);
        new BlockPlaceListener(this);
        new EntityDamageListener(this);
        new ProjectileHitListener(this);
        new EntityDieListener(this);
        new PlayerJoinListener(this);
        new PlayerHoldsItemListener(this);
        new PlayerSneakListener(this);
        new TakeDamageListener(this);
        new EntityExplodeListener(this);
        new PlayerDismountListener(this);

        new TeamsInit(this);
        //hello


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}

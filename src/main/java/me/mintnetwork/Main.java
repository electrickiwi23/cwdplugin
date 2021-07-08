package me.mintnetwork;

import me.mintnetwork.Objects.CapturePoint;
import me.mintnetwork.commands.*;
import me.mintnetwork.initialization.GameStart;
import me.mintnetwork.initialization.TeamsInit;
import me.mintnetwork.initialization.WizardInit;
import me.mintnetwork.listeners.*;
import me.mintnetwork.repeaters.BlockDecay;
import me.mintnetwork.repeaters.Passives;
import me.mintnetwork.repeaters.StatusEffects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import static me.mintnetwork.commands.ClassSelect.createClassMenu;
import static me.mintnetwork.commands.Wands.createWandsMenu;


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

        new ChangeClass(this);
        new DebugCommand(this);
        new ClassSelect(this);
        new Start(this);
        new Wands(this);
        new GiveWand(this);
        new AssignTeams(this);
        new setFlare(this);

        getCommand("changeclass").setTabCompleter(new ClassAutoCompleter());
        getCommand("givewands").setTabCompleter(new WandsAutoCompleter());
        getCommand("start").setTabCompleter(new StartAutoCompleter());

        FileConfiguration config = this.getConfig();

        config.addDefault("FlareAx",0.0);
        config.addDefault("FlareAy",0.0);
        config.addDefault("FlareAz",0.0);
        config.addDefault("FlareBx",0.0);
        config.addDefault("FlareBy",0.0);
        config.addDefault("FlareBz",0.0);
        config.addDefault("FlareCx",0.0);
        config.addDefault("FlareCy",0.0);
        config.addDefault("FlareCz",0.0);

        TeamsInit.refreshTeams();

        config.options().copyDefaults(true);
        saveConfig();

        this.saveDefaultConfig();

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
        new InventoryClickListener(this);
        new DropItemListener(this);

        new TeamsInit(this);

        // Sets up GUIs
        createClassMenu();
        createWandsMenu();
    }

    @Override
    public void onDisable() {
        CapturePoint.Shutdown();
        // Plugin shutdown logic

    }
}

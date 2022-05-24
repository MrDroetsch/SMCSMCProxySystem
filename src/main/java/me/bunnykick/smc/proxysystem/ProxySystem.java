package me.bunnykick.smc.proxysystem;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.system.ReloadCommand;
import me.bunnykick.smc.proxysystem.system.SystemConfigManager;
import net.md_5.bungee.api.plugin.Plugin;

public final class ProxySystem extends Plugin {

    /**
     * Variables
     */

    // Instance
    private static ProxySystem instance;

    // System-Variables
    private BanSystem banSystem;

    // SystemConfig
    private SystemConfigManager systemConfig;

    /**
     * onEn-/onDisable
     */

    @Override
    public void onEnable() {

        // initialize instance
        instance = this;

        // Load SystemConfig
        systemConfig = new SystemConfigManager(this);

        // Register SystemCommands
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand("proxysystem", this));

        // Enable Ban-System
        banSystem = new BanSystem(this); // declaring...
        banSystem.enable(); // enabling...

        // TODO: load other systems

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // TODO: shutdown systems
    }

    /**
     * Getter and Setter
     */

    public SystemConfigManager getSystemConfig() {
        return systemConfig;
    }

    public BanSystem getBanSystem() {
        return banSystem;
    }

    /**
     * Reloads the System
     */
    public void reload() {

        // reload SystemConfig
        systemConfig.load();

        // reload Ban-System
        banSystem.reload();

        // TODO: reload other systems
    }

    /**
     * getInstance
     */
    public static ProxySystem getInstance() {
        return instance;
    }

}

package me.bunnykick.smc.proxysystem;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.system.database.MySQLConnect;
import me.bunnykick.smc.proxysystem.system.database.MySQLUUID;
import me.bunnykick.smc.proxysystem.system.ReloadCommand;
import me.bunnykick.smc.proxysystem.system.SystemConfigManager;
import me.bunnykick.smc.proxysystem.system.listeners.Join;
import net.md_5.bungee.api.plugin.Plugin;

public final class ProxySystem extends Plugin {

    /**
     * Variables
     */

    // Instance
    private static ProxySystem instance;

    // System-Variables
    private BanSystem banSystem;

    // Commands
    private ReloadCommand reloadCommand;

    // Listeners
    private Join joinListenerUUID;

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

        // MySQL Connection
        setupMySQLConnection();

        // create MySQLUUID Table
        MySQLUUID.createTableIFNotExists();

        // Register SystemCommands
        registerCommands();

        // register Listeners
        registerListeners();

        // Enable Ban-System
        banSystem = new BanSystem(this); // declaring...
        banSystem.enable(); // enabling...

        // TODO: load other systems

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MySQLConnect.disconnect();
        // TODO: shutdown systems
        // BanSystem
        banSystem.disable();
    }

    /**
     * Connect to MySQL
     */
    private void setupMySQLConnection() {

        // Getting Information out of SystemConfig
        String host = systemConfig.getMySQL("Host");
        String port = systemConfig.getMySQL("Port");
        String database = systemConfig.getMySQL("Database");
        String username = systemConfig.getMySQL("Username");
        String password = systemConfig.getMySQL("Password");

        // Connect to MySQL
        MySQLConnect.setVariables(database, username, password, host, port);
        MySQLConnect.connect();

    }

    /**
     * Register Commands/Listeners
     */
    private void registerCommands() {
        // Proxysystem reload command
        reloadCommand = new ReloadCommand("proxysystem", this);
        getProxy().getPluginManager().registerCommand(this, reloadCommand);
    }

    private void registerListeners() {
        // JoinEvent for UUID/IP Log
        joinListenerUUID = new Join(this);
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

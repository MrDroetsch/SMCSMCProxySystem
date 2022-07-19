package me.bunnykick.smc.proxysystem;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.mutesystem.MuteSystem;
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

    // disabled boolean
    private boolean disabled = false;

    // Instance
    private static ProxySystem instance;

    // System-Variables
    private BanSystem banSystem;
    private MuteSystem muteSystem;

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
        if(!setupMySQLConnection()) {
            System.err.println("Das ProxySystem kann nicht geladen werden, da keine Connection zum MySQL-Server aufgebaut werden konnte.");
            disabled = true;
            return;
        }

        // create MySQLUUID Table
        MySQLUUID.createTableIFNotExists();

        // Register SystemCommands
        registerCommands();

        // register Listeners
        registerListeners();

        // Enable Ban-System
        banSystem = new BanSystem(); // declaring...
        banSystem.enable(); // enabling...

        // Enable Mute-System
        muteSystem = new MuteSystem();
        muteSystem.enable();

        // TODO: load other systems

    }

    @Override
    public void onDisable() {
        if(disabled)
            return;
        // Plugin shutdown logic
        MySQLConnect.disconnect();
        // TODO: shutdown systems
        // Systems Shutdown
        banSystem.disable();
        muteSystem.disable();
    }

    /**
     * Connect to MySQL
     */
    private boolean setupMySQLConnection() {

        // Getting Information out of SystemConfig
        String host = systemConfig.getMySQL("Host");
        String port = systemConfig.getMySQL("Port");
        String database = systemConfig.getMySQL("Database");
        String username = systemConfig.getMySQL("Username");
        String password = systemConfig.getMySQL("Password");

        // Connect to MySQL
        MySQLConnect.setVariables(database, username, password, host, port);

        return MySQLConnect.connect();

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

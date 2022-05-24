package me.bunnykick.smc.proxysystem.bansystem;

import me.bunnykick.smc.proxysystem.ProxySystem;
import me.bunnykick.smc.proxysystem.bansystem.commands.BanCommand;
import me.bunnykick.smc.proxysystem.bansystem.configurations.BanConfigManager;
import me.bunnykick.smc.proxysystem.bansystem.database.SQLiteBanConnect;

public class BanSystem {

    // System-Plugin variable
    private final ProxySystem plugin;

    /**
     * BanSystem Variables
     */
    private BanConfigManager banConfig;

    /**
     * Constructor
     * @param plugin System-Plugin
     */
    public BanSystem(ProxySystem plugin) {
        this.plugin = plugin;
    }

    /**
     * Enable BanSystem if it's enabled in SystemConfig
     */
    public void enable() {

        /*
         * return if it's not enabled
         */
        if(!plugin.getSystemConfig().isEnabled(BanSystem.class.getSimpleName())) {
            System.err.println("[ProxySystem] ----------404----------");
            System.err.println("[ProxySystem] Das BanSystem wird NICHT geladen. Falls das ein Fehler ist, stelle dies in der SystemConfig.yml um und nutze /proxysystem reload.");
            System.err.println("[ProxySystem] ----------404----------");
            return;
        }

        /*
         * Enabling BanSystem...
         */

        // load config
        banConfig = new BanConfigManager(this);

        // load database
        SQLiteBanConnect.connect();
        SQLiteBanConnect.createNewTable();

        // laodCommands
        getPlugin().getProxy().getPluginManager().registerCommand(getPlugin(), new BanCommand("ban"));

    }

    /**
     * Disable BanSystem
     */
    public void disable() {

        // Disconnect SQLite Connection
        SQLiteBanConnect.disconnect();

        // TODO: Unregister Commands and Listeners

    }

    /**
     * reload BanSystem
     */
    public void reload() {
        disable();
        enable();
    }

    /**
     * Getter
     */
    public ProxySystem getPlugin() {
        return plugin;
    }
}

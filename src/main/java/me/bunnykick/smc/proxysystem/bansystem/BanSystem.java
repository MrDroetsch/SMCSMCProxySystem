package me.bunnykick.smc.proxysystem.bansystem;

import me.bunnykick.smc.proxysystem.ProxySystem;
import me.bunnykick.smc.proxysystem.bansystem.commands.BanCommand;
import me.bunnykick.smc.proxysystem.bansystem.configurations.BanConfigManager;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.system.MySQLConnect;

public class BanSystem {

    // System-Plugin variable
    private final ProxySystem plugin;

    /**
     * BanSystem Variables
     */

    // Config
    private BanConfigManager banConfig;

    // Commands
    private BanCommand banCommand;

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
        MySQLBan.createNewTableIfNotExists();

        // registerCommands
        registerCommands();

    }

    /**
     * Disable BanSystem
     */
    public void disable() {

        // reload Config
        banConfig.load();

        // Unregister commands
        unregisterCommands();

        // TODO: Unregister Listeners
    }

    /**
     * reload BanSystem
     */
    public void reload() {
        disable();
        enable();
    }

    /**
     * Un-/Register Commands and Listeners
     */
    private void registerCommands() {
        // Ban-Command
        banCommand = new BanCommand("ban", this);
        getPlugin().getProxy().getPluginManager().registerCommand(getPlugin(), banCommand);
    }

    private void unregisterCommands() {
        getPlugin().getProxy().getPluginManager().unregisterCommand(banCommand);
    }

    /**
     * Getter
     */
    public ProxySystem getPlugin() {
        return plugin;
    }

    public BanConfigManager getBanConfig() {
        return banConfig;
    }
}

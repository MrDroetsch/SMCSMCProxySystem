package me.bunnykick.smc.proxysystem.bansystem;

import me.bunnykick.smc.proxysystem.ProxySystem;
import me.bunnykick.smc.proxysystem.bansystem.commands.*;
import me.bunnykick.smc.proxysystem.bansystem.configurations.BanConfigManager;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.bansystem.listeners.Join;

public class BanSystem {

    // System-Plugin variable
    private final ProxySystem plugin;

    /**
     * BanSystem Variables
     */

    // Config
    private BanConfigManager banConfig;

    // Commands
    private Ban banCommand;
    private Tempban tempbanCommand;
    private Unban unbanCommand;
    private BanIP banIPCommand;
    private TempbanIP tempbanIPCommand;
    private BanInfo banInfoCommand;
    private BanStat banStatCommand;

    // Listeners
    private Join joinListener;

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

        // register commands
        registerCommands();

        // register listeners
        registerListeners();

    }

    /**
     * Disable BanSystem
     */
    public void disable() {

        // reload Config
        banConfig.load();

        // unregister commands
        unregisterCommands();

        // unregister listeners
        unregisterListeners();

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

        banCommand = new Ban("ban", this);
        tempbanCommand = new Tempban("tempban", this);
        unbanCommand = new Unban("unban", this);
        banIPCommand = new BanIP("banip", this);
        tempbanIPCommand = new TempbanIP("tempbanip", this);
        banInfoCommand = new BanInfo("baninfo", this);
        banStatCommand = new BanStat("banstat", this);

    }

    private void unregisterCommands() {
        // Ban-Command
        getPlugin().getProxy().getPluginManager().unregisterCommand(banCommand);
    }

    private void registerListeners() {
        // Join Listener
        joinListener = new Join(this);
    }

    private void unregisterListeners() {
        // Join Listener
        getPlugin().getProxy().getPluginManager().unregisterListener(joinListener);
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

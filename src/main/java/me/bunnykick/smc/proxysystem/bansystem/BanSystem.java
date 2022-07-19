package me.bunnykick.smc.proxysystem.bansystem;

import me.bunnykick.smc.proxysystem.bansystem.commands.*;
import me.bunnykick.smc.proxysystem.bansystem.configurations.BanConfigManager;
import me.bunnykick.smc.proxysystem.bansystem.configurations.BanLog;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.bansystem.listeners.Join;
import me.bunnykick.smc.proxysystem.utils.childs.SystemChild;
import net.md_5.bungee.api.plugin.PluginManager;

public class BanSystem implements SystemChild {

    /**
     * BanSystem Variables
     */

    // Enabled?
    private boolean enabled;

    // Config/Log
    private BanConfigManager banConfig;
    private BanLog banLog;

    // Commands
    private Ban banCommand;
    private Tempban tempbanCommand;
    private Unban unbanCommand;
    private BanIP banIPCommand;
    private TempbanIP tempbanIPCommand;
    private BanInfo banInfoCommand;
    private BanStat banStatCommand;
    private ArchiveBans archiveBansCommand;

    // Listeners
    private Join joinListener;

    /**
     * Enable BanSystem if it's enabled in SystemConfig
     */
    @Override
    public void enable() {
        SystemChild.super.enable();
        setEnabled(true);
    }

    /**
     * Disable BanSystem
     */
    @Override
    public void disable() {
        SystemChild.super.disable();
        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadConfig() {
        banConfig = new BanConfigManager(this);
        banLog = new BanLog(this);
    }

    @Override
    public void reloadConfig() {
        banConfig.reload();
    }

    /**
     * Un-/Register Modules
     */

    @Override
    public void MySQLConnect() {
        MySQLBan.createNewTableIfNotExists();
    }

    public void loadCommands() {

        PluginManager pluginManager = plugin.getProxy().getPluginManager();

        banCommand = new Ban("ban", this);
        pluginManager.registerCommand(plugin, banCommand);

        tempbanCommand = new Tempban("tempban", this);
        pluginManager.registerCommand(plugin, tempbanCommand);

        unbanCommand = new Unban("unban", this);
        pluginManager.registerCommand(plugin, unbanCommand);

        banIPCommand = new BanIP("banip", this);
        pluginManager.registerCommand(plugin, banIPCommand);

        tempbanIPCommand = new TempbanIP("tempbanip", this);
        pluginManager.registerCommand(plugin, tempbanIPCommand);

        banInfoCommand = new BanInfo("baninfo", this);
        pluginManager.registerCommand(plugin, banInfoCommand);

        banStatCommand = new BanStat("banstat", this);
        pluginManager.registerCommand(plugin, banStatCommand);

        archiveBansCommand = new ArchiveBans("archivebans", this);
        pluginManager.registerCommand(plugin, archiveBansCommand);

    }

    public void unloadCommands() {

        PluginManager pluginManager = plugin.getProxy().getPluginManager();

        pluginManager.unregisterCommand(banCommand);
        pluginManager.unregisterCommand(tempbanCommand);
        pluginManager.unregisterCommand(unbanCommand);
        pluginManager.unregisterCommand(banIPCommand);
        pluginManager.unregisterCommand(tempbanIPCommand);
        pluginManager.unregisterCommand(banInfoCommand);
        pluginManager.unregisterCommand(banStatCommand);
        pluginManager.unregisterCommand(archiveBansCommand);

    }

    public void registerListeners() {
        // Join Listener
        joinListener = new Join(this);
        plugin.getProxy().getPluginManager().registerListener(plugin, joinListener);
    }

    public void unregisterListeners() {
        // Join Listener
        plugin.getProxy().getPluginManager().unregisterListener(joinListener);
    }

    /**
     * Getter
     */
    public BanConfigManager getBanConfig() {
        return banConfig;
    }

    public BanLog getBanLogfileManager() {
        return banLog;
    }

}

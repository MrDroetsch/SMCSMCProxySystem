package me.bunnykick.smc.proxysystem.mutesystem;

import me.bunnykick.smc.proxysystem.mutesystem.cofigurations.MuteConfigManager;
import me.bunnykick.smc.proxysystem.mutesystem.commands.*;
import me.bunnykick.smc.proxysystem.mutesystem.database.MySQLMute;
import me.bunnykick.smc.proxysystem.mutesystem.listeners.PlayerChat;
import me.bunnykick.smc.proxysystem.utils.childs.SystemChild;
import net.md_5.bungee.api.plugin.PluginManager;

public class MuteSystem implements SystemChild {

    /**
     * Mute-System variables
     */

    // enabled?
    private boolean enabled = true;

    // Files
    private MuteConfigManager configManager;

    // Commands
    private Mute muteCommand;
    private UnMute unMuteCommand;
    private MuteInfo muteInfoCommand;

    // Listeners
    private PlayerChat playerChatListener;

    /**
     * Enable/Disable System
     */
    @Override
    public void enable() {
        SystemChild.super.enable();
        setEnabled(true);
    }

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

    /**
     * Load/Unload Modules
     */
    @Override
    public void loadConfig() {
        // Config Manager
        configManager = new MuteConfigManager(this);
    }

    @Override
    public void reloadConfig() {
        // Config Manager
        configManager.reload();
    }

    @Override
    public void MySQLConnect() {
        MySQLMute.createTableIfNotExists();
    }

    @Override
    public void loadCommands() {

        // getting Plugin-Manager
        PluginManager pluginManager = plugin.getProxy().getPluginManager();

        // Mute
        muteCommand = new Mute(this, "mute");
        pluginManager.registerCommand(plugin, muteCommand);

        // Unmute
        unMuteCommand = new UnMute(this, "unmute");
        pluginManager.registerCommand(plugin, unMuteCommand);

        // MuteInfo
        muteInfoCommand = new MuteInfo(this, "muteinfo");
        pluginManager.registerCommand(plugin, muteInfoCommand);

    }

    @Override
    public void registerListeners() {

        // Chat Listener
        playerChatListener = new PlayerChat(this);
        plugin.getProxy().getPluginManager().registerListener(plugin, playerChatListener);

    }

    @Override
    public void unloadCommands() {

        // getting Plugin-Manager
        PluginManager pluginManager = plugin.getProxy().getPluginManager();

        // Mute
        pluginManager.unregisterCommand(muteCommand);

        // Unmute
        pluginManager.unregisterCommand(unMuteCommand);

        // MuteInfo
        pluginManager.unregisterCommand(muteInfoCommand);

    }

    @Override
    public void unregisterListeners() {

        // Chat Listener
        plugin.getProxy().getPluginManager().unregisterListener(playerChatListener);

    }

    /**
     * Getter and Setter
     */
    public MuteConfigManager getConfigManager() {
        return configManager;
    }
}

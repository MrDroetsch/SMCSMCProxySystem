package me.bunnykick.smc.proxysystem.utils.childs;

import me.bunnykick.smc.proxysystem.ProxySystem;

public interface SystemChild {

    // Plugin lobby
    ProxySystem plugin = ProxySystem.getInstance();

    // System-Methods
    default void enable() {
        loadConfig();
        MySQLConnect();
        loadCommands();
        registerListeners();
    }
    default void disable() {
        unloadCommands();
        unregisterListeners();
    }
    default void reload() {
        disable();
        enable();
    }

    void setEnabled(boolean enabled);
    boolean isEnabled();

    // SQL, Commands, Listeners
    void loadConfig();
    void reloadConfig();
    void MySQLConnect();
    void loadCommands();
    void registerListeners();
    void unloadCommands();
    void unregisterListeners();


}

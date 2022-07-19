package me.bunnykick.smc.proxysystem.reportsystem;

import me.bunnykick.smc.proxysystem.utils.childs.SystemChild;

public class ReportSystem implements SystemChild {

    private boolean enabled;

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

    @Override
    public void loadConfig() {

    }

    @Override
    public void reloadConfig() {

    }

    @Override
    public void MySQLConnect() {

    }

    @Override
    public void loadCommands() {

    }

    @Override
    public void registerListeners() {

    }

    @Override
    public void unloadCommands() {

    }

    @Override
    public void unregisterListeners() {

    }
}

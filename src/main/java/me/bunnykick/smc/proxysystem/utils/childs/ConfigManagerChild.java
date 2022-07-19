package me.bunnykick.smc.proxysystem.utils.childs;

public interface ConfigManagerChild {

    void save();
    void load();
    default void reload() {
        load();
    }

}

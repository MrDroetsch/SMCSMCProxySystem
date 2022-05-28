package me.bunnykick.smc.proxysystem.utils;

public enum SystemPermissions {

    ADMIN("AdminPermission"),
    RELOAD("ReloadCommand"),
    BAN("BanPlayer"),
    TEMPBAN("TempbanPlayer"),
    BANIP("BanIP"),
    TEMPBANIP("TempbanIP"),
    BANINFO("BanInfo");

    public final String label;

    private SystemPermissions(String label) {
        this.label = label;
    }

}

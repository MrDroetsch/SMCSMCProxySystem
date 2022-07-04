package me.bunnykick.smc.proxysystem.utils;

public enum SystemPermissions {

    ADMIN("AdminPermission"),
    RELOAD("ReloadCommand"),
    BAN("BanPlayer"),
    BAN_NOTIFY("BanNotify"),
    TEMP_BAN("TempbanPlayer"),
    UNBAN("UnbanPlayer"),
    BAN_IP("BanIP"),
    TEMP_BAN_IP("TempbanIP"),
    BAN_INFO("BanInfo");

    public final String label;

    private SystemPermissions(String label) {
        this.label = label;
    }

}

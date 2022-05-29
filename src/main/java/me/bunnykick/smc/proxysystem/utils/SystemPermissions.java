package me.bunnykick.smc.proxysystem.utils;

public enum SystemPermissions {

    ADMIN("AdminPermission"),
    RELOAD("ReloadCommand"),
    BAN("BanPlayer"),
    TEMP_BAN("TempbanPlayer"),
    UNBAN("UnbanPlayer"),
    BAN_IP("BanIP"),
    TEMP_BAN_IP("TempbanIP"),
    UNBAN_IP("UnbanIP"),
    BAN_INFO("BanInfo");

    public final String label;

    private SystemPermissions(String label) {
        this.label = label;
    }

}

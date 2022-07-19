package me.bunnykick.smc.proxysystem.utils.enums;

public enum SystemPermissions {

    ADMIN("AdminPermission"),
    RELOAD("ReloadCommand"),
    BAN("BanPlayer"),
    BAN_NOTIFY("BanNotify"),
    TEMP_BAN("TempbanPlayer"),
    UNBAN("UnbanPlayer"),
    BAN_IP("BanIP"),
    TEMP_BAN_IP("TempbanIP"),
    BAN_INFO("BanInfo"),
    BAN_STAT("BanStatistic"),
    BAN_ARCHIVE("ArchiveBans"),
    MUTE("Mute"),
    MUTE_BYPASS("MuteBypass"),
    MUTE_INFO("MuteInfo"),
    UNMUTE("Unmute"),
    MUTE_NOTIFY("muteNotify");

    public final String label;

    SystemPermissions(String label) {
        this.label = label;
    }

}

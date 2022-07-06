package me.bunnykick.smc.proxysystem.bansystem.utils;

public enum BanMessages {

    BAN_PLAYER("BanPlayer"),
    BAN_NOTIFY("BanNotify"),
    UNBAN_NOTIFY("UnbanNotify"),
    BAN_INFO("BanInfo"),
    BAN_STAT("BanStatistic");

    public final String label;

    BanMessages(String label) {
        this.label = label;
    }

}

package me.bunnykick.smc.proxysystem.bansystem.utils;

public enum BanMessages {

    BAN_PLAYER("BanPlayer"),
    BAN_NOTIFY("BanNotify"),
    TEMPBAN_PLAYER("TempbanPlayer"),
    BAN_IP("BanIP"),
    TEMPBAN_IP("TempbanIP"),
    BAN_INFO("BanInfo");

    public final String label;

    private BanMessages(String label) {
        this.label = label;
    }

}

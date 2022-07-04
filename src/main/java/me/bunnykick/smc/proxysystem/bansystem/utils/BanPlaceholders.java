package me.bunnykick.smc.proxysystem.bansystem.utils;

public enum BanPlaceholders {

    REASON("{REASON}"),
    PLAYER("{PLAYER}"),
    IP("{IP}"),
    DURATION("{DURATION}"),
    ADMIN("{ADMIN}"),
    COUNT("{COUNT}"),
    STILL_BANNED("{STILL_BANNED}"),
    IP_BANNED("{IP_BANNED}");

    public final String label;

    BanPlaceholders(String label) {
        this.label = label;
    }

}

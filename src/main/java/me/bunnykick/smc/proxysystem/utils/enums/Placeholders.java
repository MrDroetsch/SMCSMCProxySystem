package me.bunnykick.smc.proxysystem.utils.enums;

public enum Placeholders {

    PLAYER("{PLAYER}"),
    ADMIN("{ADMIN}"),
    REASON("{REASON}"),
    MUTED_WHEN("{MUTED_WHEN}"),
    MUTED_TO("{MUTED_TO}"),
    PARDON("{UNMUTED}"),
    UUID("{UUID}"),
    IP("{IP}"),
    DURATION("{DURATION}"),
    COUNT("{COUNT}"),
    STILL_BANNED("{STILL_BANNED}"),
    IP_BANNED("{IP_BANNED}"),
    BANNED_FROM("{BANNED_FROM}"),
    BANNED_TO("{BANNED_TO}"),
    STATUS("{STATUS}");

    public final String label;

    Placeholders(String label) {
        this.label = label;
    }
}

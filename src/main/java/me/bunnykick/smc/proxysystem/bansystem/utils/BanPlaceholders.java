package me.bunnykick.smc.proxysystem.bansystem.utils;

public enum BanPlaceholders {

    REASON("{REASON}"),
    PLAYER("{PLAYER}"),
    DURATION("{DURATION}"),
    ADMIN("{ADMIN}");

    public final String label;

    private BanPlaceholders(String label) {
        this.label = label;
    }

}

package me.bunnykick.smc.proxysystem.utils;

public enum SystemMessages {
    NOPERM("NoPerm"),
    ERROR("ERROR");

    public final String label;

    private SystemMessages(String label) {
        this.label = label;
    }
}

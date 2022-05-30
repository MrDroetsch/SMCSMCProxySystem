package me.bunnykick.smc.proxysystem.bansystem.utils;

public enum CheckBanIndex {
    ADMIN(0),
    REASON(1),
    DURATION(2);

    public final int i;

    CheckBanIndex(int i) {
        this.i = i;
    }
}

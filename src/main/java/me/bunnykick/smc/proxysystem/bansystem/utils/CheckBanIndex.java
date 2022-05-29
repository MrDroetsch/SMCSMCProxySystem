package me.bunnykick.smc.proxysystem.bansystem.utils;

public enum CheckBanIndex {
    ADMIN(1),
    REASON(2),
    DURATION(3);

    public final int i;

    CheckBanIndex(int i) {
        this.i = i;
    }
}

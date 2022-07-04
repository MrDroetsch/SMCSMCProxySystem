package me.bunnykick.smc.proxysystem.bansystem.utils;

public enum BanInfoIndex {

    PLAYER(0),
    IP(1),
    ADMIN(2),
    LAST_REASON(3),
    DURATION(4),
    BANNED(5),
    IP_BANNED(6),
    BAN_COUNT(7);

    public final int index;

    BanInfoIndex(int i) {
        this.index = i;
    }

}

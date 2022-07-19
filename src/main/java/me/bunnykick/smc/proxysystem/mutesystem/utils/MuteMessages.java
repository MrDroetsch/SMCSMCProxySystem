package me.bunnykick.smc.proxysystem.mutesystem.utils;

public enum MuteMessages {

    MUTE_NOTIFY("MuteNotify"),
    MUTE_PLAYER("MutePlayer"),
    MUTE_INFO("MuteInfo"),
    UNMUTE("UnMute");

    public String label;

    MuteMessages(String label) {
        this.label = label;
    }
}

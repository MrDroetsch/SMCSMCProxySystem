package me.bunnykick.smc.proxysystem.mutesystem.listeners;

import me.bunnykick.smc.proxysystem.mutesystem.MuteSystem;
import me.bunnykick.smc.proxysystem.mutesystem.database.MySQLMute;
import me.bunnykick.smc.proxysystem.utils.Methods;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Timestamp;

public class PlayerChat implements Listener {

    /**
     * Variables
     */
    private final MuteSystem muteSystem;

    /**
     * Constructor
     * @param muteSystem
     */
    public PlayerChat(MuteSystem muteSystem) {
        this.muteSystem = muteSystem;
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {

        // get Player
        if(!(event.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String name = player.getName();
        String uuid = player.getUniqueId().toString();

        // check if Muted
        Timestamp mutedTo = MySQLMute.isMuted(name, uuid);
        if(mutedTo == null)
            return;

        if(mutedTo.after(new Timestamp(System.currentTimeMillis()))) {
            MySQLMute.unMutePlayer(name);
            return;
        }

        // cancel it
        event.setCancelled(true);

        // send Message
        Methods.sendMessage(player, "§2Du bist noch gemutet bis: §c" + Methods.translateTimestampToString(mutedTo));

    }

}

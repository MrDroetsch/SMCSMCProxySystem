package me.bunnykick.smc.proxysystem.system.listeners;

import me.bunnykick.smc.proxysystem.ProxySystem;
import me.bunnykick.smc.proxysystem.system.MySQLUUID;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Join implements Listener {

    public Join(ProxySystem plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPlayerJoin(PreLoginEvent event) {
        PendingConnection con = event.getConnection();
        String name = con.getName().toLowerCase();
        String uuid = con.getUniqueId().toString();
        String ip = con.getSocketAddress().toString();
        if(ip.contains(":"))
            ip = ip.split(":")[0];
        MySQLUUID.registerPlayerIfNotRegistered(name, uuid, ip);
    }

}

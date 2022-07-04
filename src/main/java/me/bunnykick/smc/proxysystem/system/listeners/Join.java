package me.bunnykick.smc.proxysystem.system.listeners;

import me.bunnykick.smc.proxysystem.ProxySystem;
import me.bunnykick.smc.proxysystem.system.database.MySQLUUID;
import me.bunnykick.smc.proxysystem.utils.Methods;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Join implements Listener {

    public Join(ProxySystem plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPlayerJoin(LoginEvent event) {
        PendingConnection con = event.getConnection();
        String name = con.getName().toLowerCase();
        String uuid = con.getUniqueId().toString();
        String ip = Methods.getIP(con.getSocketAddress());
        MySQLUUID.registerPlayerIfNotRegistered(name, uuid, ip);
    }

}

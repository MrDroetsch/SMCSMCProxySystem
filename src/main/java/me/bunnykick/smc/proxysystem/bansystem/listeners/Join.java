package me.bunnykick.smc.proxysystem.bansystem.listeners;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanMessages;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanPlaceholders;
import me.bunnykick.smc.proxysystem.bansystem.utils.CheckBanIndex;
import me.bunnykick.smc.proxysystem.utils.Methods;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class Join implements Listener {

    /**
     * BanSystem variable
     */
    private final BanSystem banSystem;

    /**
     * Constructor
     * @param banSystem
     */
    public Join(BanSystem banSystem) {
        this.banSystem = banSystem;
        banSystem.getPlugin().getProxy().getPluginManager().registerListener(banSystem.getPlugin(), this);
    }

    /**
     * OnPlayerJoin kicks the player if he is banned before he can join
     * @param event
     */
    @EventHandler
    public void onPlayerJoin(PreLoginEvent event) {
        PendingConnection pc = event.getConnection();
        String uuid = pc.getUniqueId().toString();
        String name = pc.getName();
        String ip = pc.getSocketAddress().toString();
        ip = ip.contains(":") ? ip.split(":")[0] : ip;

        // Check if uuid, name or ip is banned
        String[] banInfo = MySQLBan.checkBanned(uuid, name, ip); // getting information
        if(banInfo != null) { // Checking if banned
            String admin = banInfo[CheckBanIndex.ADMIN.i];
            String reason = banInfo[CheckBanIndex.ADMIN.i];
            String duration = banInfo[CheckBanIndex.DURATION.i];

            // Building KickMessage
            List<String> kickMessageList = banSystem.getBanConfig().getMessage(BanMessages.BAN_PLAYER);
            String kickMessage = "";
            for(String cur : kickMessageList) {
                kickMessage += cur + "\n";
            }
            kickMessage = kickMessage.substring(0, kickMessage.length()-1);

            kickMessage = Methods.translatePlaceholder(BanPlaceholders.PLAYER, kickMessage, name);
            kickMessage = Methods.translatePlaceholder(BanPlaceholders.ADMIN, kickMessage, admin);
            kickMessage = Methods.translatePlaceholder(BanPlaceholders.DURATION, kickMessage, duration);
            kickMessage = Methods.translatePlaceholder(BanPlaceholders.REASON, kickMessage, reason);

            // kick player
            pc.disconnect(Methods.translateChatColors(kickMessage));
        }

    }

}

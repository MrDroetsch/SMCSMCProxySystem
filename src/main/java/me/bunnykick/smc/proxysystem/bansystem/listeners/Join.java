package me.bunnykick.smc.proxysystem.bansystem.listeners;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanMessages;
import me.bunnykick.smc.proxysystem.bansystem.utils.CheckBanIndex;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.enums.Placeholders;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
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
        banSystem.plugin.getProxy().getPluginManager().registerListener(banSystem.plugin, this);
    }

    /**
     * OnPlayerJoin kicks the player if he is banned before he can join
     * @param event
     */
    @EventHandler
    public void onPlayerJoin(LoginEvent event) {
        PendingConnection pc = event.getConnection();
        String uuid = pc.getUniqueId().toString();
        String name = pc.getName();
        String ip = Methods.getIP(pc.getSocketAddress());

        // Check if uuid, name or ip is banned
        String[] banInfo = MySQLBan.checkBanned(uuid, name, ip); // getting information
        if(banInfo != null) { // Checking if banned
            String admin = banInfo[CheckBanIndex.ADMIN.i];
            String reason = banInfo[CheckBanIndex.REASON.i];
            String duration = banInfo[CheckBanIndex.DURATION.i];
            String ipBanned = banInfo[CheckBanIndex.IP_BANNED.i];

            // Building KickMessage
            List<String> kickMessageList = banSystem.getBanConfig().getMessage(BanMessages.BAN_PLAYER);
            String kickMessage = "";
            for(String cur : kickMessageList) {
                kickMessage += cur + "\n";
            }
            kickMessage = kickMessage.substring(0, kickMessage.length()-1);

            kickMessage = Methods.translatePlaceholder(Placeholders.PLAYER, kickMessage, name);
            kickMessage = Methods.translatePlaceholder(Placeholders.ADMIN, kickMessage, admin);
            kickMessage = Methods.translatePlaceholder(Placeholders.DURATION, kickMessage, duration);
            kickMessage = Methods.translatePlaceholder(Placeholders.REASON, kickMessage, reason);
            kickMessage = Methods.translatePlaceholder(Placeholders.IP_BANNED, kickMessage, ipBanned);

            // kick player
            pc.disconnect(Methods.translateChatColors(kickMessage));
        }

    }

}

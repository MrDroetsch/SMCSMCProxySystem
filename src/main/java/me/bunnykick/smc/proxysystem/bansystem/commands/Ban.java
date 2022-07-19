package me.bunnykick.smc.proxysystem.bansystem.commands;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanMessages;
import me.bunnykick.smc.proxysystem.system.database.MySQLUUID;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.enums.Placeholders;
import me.bunnykick.smc.proxysystem.utils.enums.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.enums.SystemPermissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class Ban extends Command {

    /**
     * Variables
     */
    private final BanSystem banSystem;

    /**
     * Command Constructor
     * @param commandName Command name
     * @param banSystem BanSystem
     */
    public Ban(String commandName, BanSystem banSystem) {
        super(commandName);
        this.banSystem = banSystem;
    }

    /**
     * When /ban command is used
     * @param sender CommandSender
     * @param args Arguments given
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {       // Sender = Player

            // Initialize Player
            ProxiedPlayer player = (ProxiedPlayer) sender;
            ProxiedPlayer banned = null;

            // Check Permissions
            String permission = banSystem.getBanConfig().getPermission(SystemPermissions.BAN);
            String adminPermission = banSystem.plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!player.hasPermission(permission) && !player.hasPermission(adminPermission)) {
                banSystem.plugin.getSystemConfig().getMessage(SystemMessages.NOPERM);
                return;
            }

            // Check arguments      /ban <Player> <Grund>
            switch(args.length) {
                case 0:
                    Methods.sendMessage(player, ChatColor.DARK_RED + "/ban <Spieler> Grund");
                    return;
                case 1:
                    Methods.sendMessage(player, ChatColor.DARK_RED + "/ban " + args[0] + " Grund");
                    return;
            }

            // Getting Information
            String name = args[0];

            // Check Bypass
            List<String> bypass = banSystem.getBanConfig().getBypass();
            if(bypass.contains(name.toLowerCase())) {
                Methods.sendMessage(player, "§4Du kannst diesen Spieler nicht bannen!");
                return;
            }

            // Try getting UUID and IP
            String uuid = null;
            String ip = null;
            try {
                banned = banSystem.plugin.getProxy().getPlayer(name);
                uuid = banned.getUniqueId().toString();
                ip = Methods.getIP(banned.getSocketAddress());
            } catch(NullPointerException e) {
                uuid = MySQLUUID.getUUID(name);
                ip = MySQLUUID.getIP(name);
            }
            if(uuid == null) { // Message to Player that UUID is not registered yet
                Methods.sendMessage(player, ChatColor.RED + "UUID konnte nicht ermittelt werden. Nur der Name(" + name + ") wird gebannt!");
            }

            String admin = player.getName();

            // Build reason String
            String reason = "";
            for(int i = 1; i < args.length; i++) {
                reason += args[i] + " ";
            }

            boolean perma = true;
            boolean banIP = false;

            if(banned != null) { // banned Player is Online and gets Kicked

                // Create Kick Message
                List<String> kickMessageList = banSystem.getBanConfig().getMessage(BanMessages.BAN_PLAYER);
                String kickMessage = "";
                for(String current : kickMessageList) {
                    kickMessage += current + "\n";
                }
                kickMessage = Methods.translatePlaceholder(Placeholders.PLAYER, kickMessage, name);
                kickMessage = Methods.translatePlaceholder(Placeholders.ADMIN, kickMessage, admin);
                kickMessage = Methods.translatePlaceholder(Placeholders.DURATION, kickMessage, "PERMANENT");
                kickMessage = Methods.translatePlaceholder(Placeholders.REASON, kickMessage, reason);
                kickMessage = Methods.translatePlaceholder(Placeholders.IP_BANNED, kickMessage, "NEIN");

                banned.disconnect(Methods.translateChatColors(kickMessage));
            }

            // Register Ban in MySQL
            if(MySQLBan.checkBanned(name) == null) {
                if(!MySQLBan.banPlayer(name, uuid, ip, admin, reason, perma, banIP)) {
                    // Send Message ERROR
                    Methods.sendMessage(player, ChatColor.RED + "ERROR: Fehler bei Verbindung mit MySQL!");
                    return;
                }
            } else {
                Methods.sendMessage(player, ChatColor.RED + "ERROR: Dieser Spieler ist bereits gebannt!");
                return;
            }

            // Build Message Success
            List<String> messageSuccessList = banSystem.getBanConfig().getMessage(BanMessages.BAN_NOTIFY);
            String messageSuccess = "";
            for(String current : messageSuccessList) {
                messageSuccess += current + "\n";
            }
            messageSuccess = messageSuccess.substring(0, messageSuccess.length()-1);
            messageSuccess = Methods.translatePlaceholder(Placeholders.PLAYER, messageSuccess, name);
            messageSuccess = Methods.translatePlaceholder(Placeholders.ADMIN, messageSuccess, admin);
            messageSuccess = Methods.translatePlaceholder(Placeholders.DURATION, messageSuccess, "PERMANENT");
            messageSuccess = Methods.translatePlaceholder(Placeholders.REASON, messageSuccess, reason);
            messageSuccess = Methods.translatePlaceholder(Placeholders.IP_BANNED, messageSuccess, "NEIN");

            // Send Message Success
            String permissionNotify = banSystem.getBanConfig().getPermission(SystemPermissions.BAN_NOTIFY);
            for(ProxiedPlayer onlinePlayers : banSystem.plugin.getProxy().getPlayers()) {
                if(onlinePlayers.hasPermission(permissionNotify) || player.hasPermission(adminPermission)) {
                    Methods.sendMessage(onlinePlayers, messageSuccess);
                }
            }

            if(!(player.hasPermission(permissionNotify) || player.hasPermission(adminPermission))) {
                Methods.sendMessage(player, "§2Der Spieler wurde gebannt.");
            }

        } else {    // Sender = Console
            // TODO: Console Command
        }
    }

}

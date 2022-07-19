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

public class BanIP extends Command {

    private final BanSystem banSystem;

    /**
     * constructor of ip ban command
     * @param commandName
     * @param banSystem
     */
    public BanIP(String commandName, BanSystem banSystem) {
        super(commandName);
        this.banSystem = banSystem;
    }

    /**
     * executing ip ban command
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {       // Sender = Player

            // Initialize Player
            ProxiedPlayer player = (ProxiedPlayer) sender;
            ProxiedPlayer banned = null;

            // Check Permissions
            String permission = banSystem.getBanConfig().getPermission(SystemPermissions.BAN_IP);
            String adminPermission = banSystem.plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!player.hasPermission(permission) && !player.hasPermission(adminPermission)) {
                banSystem.plugin.getSystemConfig().getMessage(SystemMessages.NOPERM);
                return;
            }

            // Check arguments      /banip <Player> <Grund>
            switch(args.length) {
                case 0:
                    Methods.sendMessage(player, ChatColor.DARK_RED + "/banip <Spieler|IP> Grund");
                    return;
                case 1:
                    Methods.sendMessage(player, ChatColor.DARK_RED + "/banip " + args[0] + " Grund");
                    return;
            }

            // Getting Information
            String name = args[0];

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
                if(!name.contains(".")) {
                    Methods.sendMessage(player, ChatColor.RED + "IP konnte nicht ermittelt werden. " + name + " wird NICHT gebannt!");
                    return;
                } else {
                    ip = name;
                    name = "NULL";
                    uuid = "NULL";
                    if(ip.contains(":")) {
                        ip = ip.split(":")[0];
                    }
                    Methods.sendMessage(player, "§cNur die IP (" + ip + ") wird gebannt. Sicherer ist: /banip <SpielerName>");
                }
            }

            String admin = player.getName();

            // Build reason String
            String reason = "";
            for(int i = 1; i < args.length; i++) {
                reason += args[i] + " ";
            }

            boolean perma = true;
            boolean banIP = true;

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
            kickMessage = Methods.translatePlaceholder(Placeholders.IP_BANNED, kickMessage, "JA");

            // banned Player is Online and gets Kicked
            if(banned != null) {
                banned.disconnect(Methods.translateChatColors(kickMessage));
            }

            // Kick Players with same IP
            for(ProxiedPlayer onlinePlayers : banSystem.plugin.getProxy().getPlayers()) {
                if(Methods.getIP(onlinePlayers.getSocketAddress()).equals(ip)) {
                    onlinePlayers.disconnect(Methods.translateChatColors(kickMessage));
                }
            }

            // Register Ban in MySQL
            if(MySQLBan.checkBanned(name) == null || name.equals("NULL")) {
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
            messageSuccess = Methods.translatePlaceholder(Placeholders.IP_BANNED, messageSuccess, "JA");

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
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

import java.sql.Timestamp;
import java.util.List;

public class Tempban extends Command {

    private final BanSystem banSystem;

    /**
     * constructor of tempban command
     * @param commandName
     * @param banSystem
     */
    public Tempban(String commandName, BanSystem banSystem) {
        super(commandName);
        this.banSystem = banSystem;
    }

    /**
     * executing tempban command
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
            String permission = banSystem.getBanConfig().getPermission(SystemPermissions.TEMP_BAN);
            String adminPermission = banSystem.plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!player.hasPermission(permission) && !player.hasPermission(adminPermission)) {
                banSystem.plugin.getSystemConfig().getMessage(SystemMessages.NOPERM);
                return;
            }

            // Check arguments      /ban <Player> <Zeit> <Grund>
            switch(args.length) {
                case 0:
                    Methods.sendMessage(player, ChatColor.DARK_RED + "/tempban <Spieler> <Zeit> Grund");
                    return;
                case 1:
                    Methods.sendMessage(player, ChatColor.DARK_RED + "/tempban " + args[0] + " <Zeit> Grund");
                    return;
                case 2:
                    Methods.sendMessage(player, ChatColor.DARK_RED + "/tempban " + args[0] + " " + args[1] + " Grund");
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
            for(int i = 2; i < args.length; i++) {
                reason += args[i] + " ";
            }

            // Get BannedTo Timestamp
            long addedMillis = Methods.getAddedMillis(args[1]);
            if(addedMillis == 0) {
                Methods.sendMessage(player, "§cUngültige Zeitangabe! Beispiel: 5m (5 Minuten) Einheiten: (s,m,h,d,w,y)");
                return;
            }
            Timestamp bannedTo = new Timestamp((System.currentTimeMillis() + addedMillis));

            boolean perma = false;
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
                kickMessage = Methods.translatePlaceholder(Placeholders.DURATION, kickMessage, Methods.translateTimestampToString(bannedTo));
                kickMessage = Methods.translatePlaceholder(Placeholders.REASON, kickMessage, reason);
                kickMessage = Methods.translatePlaceholder(Placeholders.IP_BANNED, kickMessage, "NEIN");

                banned.disconnect(Methods.translateChatColors(kickMessage));
            }

            // Register Ban in MySQL
            if(MySQLBan.checkBanned(name) == null) {
                if(!MySQLBan.banPlayer(name, uuid, ip, admin, reason, bannedTo, perma, banIP)) {
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
            messageSuccess = Methods.translatePlaceholder(Placeholders.DURATION, messageSuccess, Methods.translateTimestampToString(bannedTo));
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
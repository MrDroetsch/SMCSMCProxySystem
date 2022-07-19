package me.bunnykick.smc.proxysystem.mutesystem.commands;

import me.bunnykick.smc.proxysystem.mutesystem.MuteSystem;
import me.bunnykick.smc.proxysystem.mutesystem.database.MySQLMute;
import me.bunnykick.smc.proxysystem.mutesystem.utils.MuteMessages;
import me.bunnykick.smc.proxysystem.system.database.MySQLUUID;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.enums.Placeholders;
import me.bunnykick.smc.proxysystem.utils.enums.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.enums.SystemPermissions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Timestamp;
import java.util.List;

public class Mute extends Command {

    /**
     * Variables
     */
    private final MuteSystem muteSystem;

    /**
     * Constructor
     * @param muteSystem
     */
    public Mute(MuteSystem muteSystem, String commandName) {
        // register Command
        super(commandName);

        // initialize Mute-System
        this.muteSystem = muteSystem;
    }

    /**
     * On Command Execute
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) { // sender = Player

            // get Player
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // check Permissions
            String permission = muteSystem.getConfigManager().getPermission(SystemPermissions.MUTE);
            String adminPermission = muteSystem.plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!(player.hasPermission(permission) || player.hasPermission(adminPermission))) {
                Methods.sendMessage(player, muteSystem.plugin.getSystemConfig().getMessage(SystemMessages.NOPERM));
                return;
            }

            // check arguments          /mute <Spieler> <Dauer> <Grund>
            switch(args.length) {
                case 0:
                    Methods.sendMessage(player, "§c/mute <Spieler> <Dauer> <Grund>");
                    return;
                case 1:
                    Methods.sendMessage(player, "§c/mute " + args[0] + " <Dauer> <Grund>");
                    return;
                case 2:
                    Methods.sendMessage(player, "§c/mute " + args[0] + " " + args[1] + " <Grund>");
                    return;
            }

            // get target Player
            String name = args[0];

            // Check Bypass
            List<String> bypass = muteSystem.getConfigManager().getBypass();
            if(bypass.contains(name.toLowerCase())) {
                Methods.sendMessage(player, "§4Du kannst diesen Spieler nicht muten!");
                return;
            }

            // Check Muted
            if(MySQLMute.isMuted(name) != null) {
                Methods.sendMessage(player, "§4Dieser Spieler ist bereits gemutet!");
                return;
            }

            // try getting UUID
            String uuid = null;
            ProxiedPlayer target = null;
            try {
                target = muteSystem.plugin.getProxy().getPlayer(name);
                uuid = target.getUniqueId().toString();
            } catch(NullPointerException e) {
                uuid = MySQLUUID.getUUID(name);
            }
            if(uuid == null) {
                Methods.sendMessage(player, "§cUUID konnte nicht wemittelt werden. Nur der Name wird gemutet");
            }

            // Get MutedTo Timestamp
            long addedMillis = Methods.getAddedMillis(args[1]);
            if(addedMillis == 0) {
                Methods.sendMessage(player, "§cUngültige Zeitangabe! Beispiel: 5m (5 Minuten) Einheiten: (s,m,h,d,w,y)");
                return;
            }
            Timestamp mutedTo = new Timestamp((System.currentTimeMillis() + addedMillis));

            // Get Reason
            String reason = "";
            for(int i = 2; i < args.length; i++) {
                reason += args[i] + " ";
            }

            // register in MySQL
            String admin = player.getName();
            if(!MySQLMute.mutePlayer(name, uuid, admin, mutedTo, reason)) {
                Methods.sendMessage(player, "§cFehler bei der Verbindung mit MySQL");
                return;
            }

            // Build Messages
            List<String> muteMessageList = muteSystem.getConfigManager().getMessage(MuteMessages.MUTE_PLAYER);
            String muteMessage = "";
            for(String cur : muteMessageList) {
                muteMessage += cur + " ";
            }
            muteMessage = muteMessage.substring(0, muteMessage.length()-1);
            muteMessage = Methods.translatePlaceholder(Placeholders.ADMIN, muteMessage, admin);
            muteMessage = Methods.translatePlaceholder(Placeholders.MUTED_TO, muteMessage, Methods.translateTimestampToString(mutedTo));
            muteMessage = Methods.translatePlaceholder(Placeholders.REASON, muteMessage, reason);

            List<String> muteNotifyList = muteSystem.getConfigManager().getMessage(MuteMessages.MUTE_NOTIFY);
            String muteNotify = "";
            for(String cur : muteNotifyList) {
                muteNotify += cur + " ";
            }
            muteNotify = muteNotify.substring(0, muteNotify.length()-1);
            muteNotify = Methods.translatePlaceholder(Placeholders.ADMIN, muteNotify, admin);
            muteNotify = Methods.translatePlaceholder(Placeholders.MUTED_TO, muteNotify, Methods.translateTimestampToString(mutedTo));
            muteNotify = Methods.translatePlaceholder(Placeholders.PLAYER, muteNotify, name);
            muteNotify = Methods.translatePlaceholder(Placeholders.REASON, muteNotify, reason);

            // send Messages
            if(target != null)
                Methods.sendMessage(target, muteMessage);

            String notifyPermission = muteSystem.getConfigManager().getPermission(SystemPermissions.MUTE_NOTIFY);
            for(ProxiedPlayer onlinePlayers : muteSystem.plugin.getProxy().getPlayers()) {
                if(onlinePlayers.hasPermission(notifyPermission)) {
                    Methods.sendMessage(onlinePlayers, muteNotify);
                }
            }

            if(!player.hasPermission(notifyPermission))
                Methods.sendMessage(player, "§2Der Spieler wurde gemutet.");

        } else { // sender = CONSOLE
            // TODO: CONSOLE Implementation
        }

    }

}

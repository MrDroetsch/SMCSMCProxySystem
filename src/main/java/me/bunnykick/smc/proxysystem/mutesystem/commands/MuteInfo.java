package me.bunnykick.smc.proxysystem.mutesystem.commands;

import me.bunnykick.smc.proxysystem.mutesystem.MuteSystem;
import me.bunnykick.smc.proxysystem.mutesystem.database.MySQLMute;
import me.bunnykick.smc.proxysystem.mutesystem.utils.MuteMessages;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.enums.Placeholders;
import me.bunnykick.smc.proxysystem.utils.enums.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.enums.SystemPermissions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class MuteInfo extends Command {

    /**
     * Variables
     */
    private final MuteSystem muteSystem;

    /**
     * Constructor
     * @param muteSystem
     */
    public MuteInfo(MuteSystem muteSystem, String commandName) {
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
            String permission = muteSystem.getConfigManager().getPermission(SystemPermissions.MUTE_INFO);
            String adminPermission = muteSystem.plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!(player.hasPermission(permission) || player.hasPermission(adminPermission))) {
                Methods.sendMessage(player, muteSystem.plugin.getSystemConfig().getMessage(SystemMessages.NOPERM));
                return;
            }

            // check arguments
            if(args.length == 0) {
                Methods.sendMessage(player, "§c/muteinfo <Spieler>");
                return;
            }

            // get Player
            String name = args[0];

            // get info
            String[] info = MySQLMute.getMuteInfo(name);

            // check if muted
            if(info == null) {
                Methods.sendMessage(player, "§c" + name + " §2ist nicht gemutet");
                return;
            }

            String admin = info[0], mutedTo = info[1], reason = info[2];

            // build Message
            List<String> muteInfoMessageList = muteSystem.getConfigManager().getMessage(MuteMessages.MUTE_INFO);
            String muteInfoMessage = "";
            for(String cur : muteInfoMessageList) {
                muteInfoMessage += cur + " ";
            }
            muteInfoMessage = muteInfoMessage.substring(0, muteInfoMessage.length()-1);
            muteInfoMessage = Methods.translatePlaceholder(Placeholders.ADMIN, muteInfoMessage, admin);
            muteInfoMessage = Methods.translatePlaceholder(Placeholders.PLAYER, muteInfoMessage, name);
            muteInfoMessage = Methods.translatePlaceholder(Placeholders.REASON, muteInfoMessage, reason);
            muteInfoMessage = Methods.translatePlaceholder(Placeholders.MUTED_TO, muteInfoMessage, mutedTo);

            // send Message
            Methods.sendMessage(player, muteInfoMessage);

        } else { // sender = CONSOLE
            // TODO: CONSOLE Implementation
        }

    }

}

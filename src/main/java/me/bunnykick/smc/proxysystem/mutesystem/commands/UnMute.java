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

public class UnMute extends Command {

    /**
     * Variables
     */
    private final MuteSystem muteSystem;

    /**
     * Constructor
     * @param muteSystem
     */
    public UnMute(MuteSystem muteSystem, String commandName) {
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

            // check permissions
            String permission = muteSystem.getConfigManager().getPermission(SystemPermissions.UNMUTE);
            String adminPermissions = muteSystem.plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!(player.hasPermission(permission) || player.hasPermission(adminPermissions))) {
                Methods.sendMessage(player, muteSystem.plugin.getSystemConfig().getMessage(SystemMessages.NOPERM));
                return;
            }

            // check arguments
            if(args.length == 0) {
                Methods.sendMessage(player, "§c/unmute <Spieler>");
                return;
            }

            // get Name
            String name = args[0];

            // check Muted
            if(MySQLMute.isMuted(name) == null) {
                Methods.sendMessage(player, "§4Dieser Spieler ist nicht gemutet!");
                return;
            }

            // delete MySQL
            MySQLMute.unMutePlayer(name);

            // send Messages
            String notifyPermission = muteSystem.getConfigManager().getPermission(SystemPermissions.MUTE_NOTIFY);
            for(ProxiedPlayer onlinePlayers : muteSystem.plugin.getProxy().getPlayers()) {
                if(onlinePlayers.hasPermission(notifyPermission))
                    Methods.sendMessage(onlinePlayers, "§c" + name + " §2wurde entmutet.");
            }

            if(!player.hasPermission(notifyPermission))
                Methods.sendMessage(player, "§c" + name + " §2wurde entmutet.");

        } else { // sender = CONSOLE
            // TODO: CONSOLE Implementation
        }

    }

}

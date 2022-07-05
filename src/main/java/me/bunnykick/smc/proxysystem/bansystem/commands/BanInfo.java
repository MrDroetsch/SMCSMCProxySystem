package me.bunnykick.smc.proxysystem.bansystem.commands;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanMessages;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.SystemPermissions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class BanInfo extends Command {

    private final BanSystem banSystem;

    /**
     * constructor of ban info command
     * @param commandName
     * @param banSystem
     */
    public BanInfo(String commandName, BanSystem banSystem) {
        super(commandName);
        this.banSystem = banSystem;
        banSystem.getPlugin().getProxy().getPluginManager().registerCommand(banSystem.getPlugin(), this);
    }

    /**
     * executing ban info command
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) { // Sender = Player

            // get Player
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // check Permission
            String permission = banSystem.getBanConfig().getPermission(SystemPermissions.BAN_INFO);
            String adminPermission = banSystem.getPlugin().getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!(player.hasPermission(permission) || player.hasPermission(adminPermission))) {
                Methods.sendMessage(player, banSystem.getPlugin().getSystemConfig().getMessage(SystemMessages.NOPERM));
                return;
            }

            // check arguments
            if(args.length != 1) {
                Methods.sendMessage(player, "§c/baninfo <Spieler>");
                return;
            }

            // get info
            String name = args[0];
            String[] banInfo = MySQLBan.getInfo(name);

            // check if info could be getted
            if(banInfo == null) {
                Methods.sendMessage(player, "§c" + name + " §2wurde bisher noch nicht gebannt");
                return;
            }

            // building Message
            List<String> banInfoMessageList = banSystem.getBanConfig().getMessage(BanMessages.BAN_INFO);
            String banInfoMessage = "";
            for(String cur : banInfoMessageList) {
                banInfoMessage += cur + "\n";
            }
            banInfoMessage = banInfoMessage.substring(0, banInfoMessage.length()-1);
            banInfoMessage = Methods.translatePlaceholders(banInfoMessage, banInfo);

            Methods.sendMessage(player, banInfoMessage);

        } else { // Sender = Console
            // TODO: CONSOLE Command
        }

    }

}
package me.bunnykick.smc.proxysystem.bansystem.commands;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanMessages;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanPlaceholders;
import me.bunnykick.smc.proxysystem.bansystem.utils.CheckBanIndex;
import me.bunnykick.smc.proxysystem.system.database.MySQLUUID;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.SystemPermissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class Unban extends Command {

    private final BanSystem banSystem;

    /**
     * constructor of unban command
     * @param commandName
     * @param banSystem
     */
    public Unban(String commandName, BanSystem banSystem) {
        super(commandName);
        this.banSystem = banSystem;
        banSystem.getPlugin().getProxy().getPluginManager().registerCommand(banSystem.getPlugin(), this);
    }

    /**
     * executing unban command
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) { // Player Command

            // initializing Player
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // Checking Arguments
            if(args.length == 0) {
                player.sendMessage(Methods.translateChatColors(ChatColor.DARK_RED + "/unban <Spieler>"));
                return;
            }

            // Getting Banned Player
            String banned = args[0];
            String ip = MySQLUUID.getIP(banned);
            String uuid = MySQLUUID.getUUID(banned);

            // Check Banned
            String[] checkBanned = (ip == null || uuid == null) ? MySQLBan.checkBanned(banned) : MySQLBan.checkBanned(uuid, banned, ip);

            if(checkBanned == null) { // Player not banned
                player.sendMessage(Methods.translateChatColors(ChatColor.RED + banned + ChatColor.DARK_GREEN + " ist nicht gebannt"));
            } else {
                // Unban Player
                if(ip == null || uuid == null) {
                    MySQLBan.pardon(banned);
                } else {
                    MySQLBan.pardon(uuid, banned, ip);
                }

                // Build unban Message
                List<String> unbanMessageList = banSystem.getBanConfig().getMessage(BanMessages.UNBAN_NOTIFY);
                String unbanMessage = "";
                for(String cur : unbanMessageList) {
                    unbanMessage += cur + "\n";
                }
                unbanMessage = unbanMessage.substring(0, unbanMessage.length()-1);

                unbanMessage = Methods.translatePlaceholder(BanPlaceholders.ADMIN, unbanMessage, checkBanned[CheckBanIndex.ADMIN.i]);
                unbanMessage = Methods.translatePlaceholder(BanPlaceholders.PLAYER, unbanMessage, banned);
                unbanMessage = Methods.translatePlaceholder(BanPlaceholders.IP_BANNED, unbanMessage, checkBanned[CheckBanIndex.IP_BANNED.i]);
                unbanMessage = Methods.translatePlaceholder(BanPlaceholders.REASON, unbanMessage, checkBanned[CheckBanIndex.REASON.i]);
                unbanMessage = Methods.translatePlaceholder(BanPlaceholders.DURATION, unbanMessage, checkBanned[CheckBanIndex.DURATION.i]);

                // Send UnbanMessage
                String permission = banSystem.getBanConfig().getPermission(SystemPermissions.BAN_NOTIFY);
                for(ProxiedPlayer onlinePlayers : banSystem.getPlugin().getProxy().getPlayers()) {
                    if(onlinePlayers.hasPermission(permission))
                        Methods.sendMessage(onlinePlayers, unbanMessage);
                }

                // Send alternative Message
                if(!player.hasPermission(permission)) {
                    Methods.sendMessage(player, "&c" + banned + " &2wurde entbannt.");
                }
            }

        } else { // Console Command
            // TODO: CONSOLE function
        }

    }

}

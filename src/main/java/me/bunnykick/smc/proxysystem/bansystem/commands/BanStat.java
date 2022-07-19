package me.bunnykick.smc.proxysystem.bansystem.commands;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanMessages;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.enums.Placeholders;
import me.bunnykick.smc.proxysystem.utils.enums.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.enums.SystemPermissions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class BanStat extends Command {

    private final BanSystem banSystem;

    /**
     * Constructor
     * @param name
     * @param banSystem
     */
    public BanStat(String name, BanSystem banSystem) {
        super(name);
        this.banSystem = banSystem;
    }

    /**
     * OnCommand Execute
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) { // sender = Player

            // get Player
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // check Permssion
            String permission = banSystem.getBanConfig().getPermission(SystemPermissions.BAN_STAT);
            String adminPermission = banSystem.plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!(player.hasPermission(permission) || player.hasPermission(adminPermission))) {
                Methods.sendMessage(player, banSystem.plugin.getSystemConfig().getMessage(SystemMessages.NOPERM));
                return;
            }

            // check arguments
            if(args.length <= 0 || args.length >= 3) {
                Methods.sendMessage(player, "§4/banstat <Spieler> || /banstat <Spieler> <BanID>");
                return;
            }

            // get checked Name
            String name = args[0];

            // Get Information
            String[][] info = MySQLBan.getAllInformation(name);

            // Check if info is available
            if(info == null) {
                Methods.sendMessage(player, "§c" + name + " §2wurde noch nie gebannt");
                return;
            }

            // Check if BanID is given
            if(args.length == 1) {
                Methods.sendMessage(player, "&c" + name + " &2hat folgende BanIDs: &c(1 bis " + info.length + ")");
                Methods.sendMessage(player, "&2Verwende §c/banstat " + name + " <BanID> §2Für Infos über einen Spezifischen Ban");
                return;
            }

            // getting BanID
            int banID = 0;
            try {
                banID = Integer.parseInt(args[1]);
                if(banID < 1 || banID > info.length) {
                    Methods.sendMessage(player, "§cUngültige BanID! (1 bis " + info.length + ")");
                    return;
                }
            } catch(NumberFormatException e) {
                Methods.sendMessage(player, "§cUngültige BanID! (1 bis " + info.length + ")");
                return;
            }

            // Getting Ban from BanID
            String[] wantedInformation = info[banID-1];

            // Saving Information
            String uuid = wantedInformation[0];
            String ip = wantedInformation[1];
            String admin = wantedInformation[2];
            String reason = wantedInformation[3];
            String bannedFrom = wantedInformation[4];
            String bannedTo = wantedInformation[5];
            String IPBanned = wantedInformation[6];
            String status = wantedInformation[7];

            // creating Message
            List<String> banStatMessageList = banSystem.getBanConfig().getMessage(BanMessages.BAN_STAT);
            String banStatMessage = "";
            for(String cur : banStatMessageList) {
                banStatMessage += cur + "\n";
            }
            banStatMessage = banStatMessage.substring(0, banStatMessage.length()-1);

            // replace placeholders
            banStatMessage = Methods.translatePlaceholder(Placeholders.PLAYER, banStatMessage, name);
            banStatMessage = Methods.translatePlaceholder(Placeholders.UUID, banStatMessage, uuid);
            banStatMessage = Methods.translatePlaceholder(Placeholders.IP, banStatMessage, ip);
            banStatMessage = Methods.translatePlaceholder(Placeholders.ADMIN, banStatMessage, admin);
            banStatMessage = Methods.translatePlaceholder(Placeholders.REASON, banStatMessage, reason);
            banStatMessage = Methods.translatePlaceholder(Placeholders.BANNED_FROM, banStatMessage, bannedFrom);
            banStatMessage = Methods.translatePlaceholder(Placeholders.BANNED_TO, banStatMessage, bannedTo);
            banStatMessage = Methods.translatePlaceholder(Placeholders.IP_BANNED, banStatMessage, IPBanned);
            banStatMessage = Methods.translatePlaceholder(Placeholders.STATUS, banStatMessage, status);

            // send Message
            Methods.sendMessage(player, banStatMessage);

        } else { // sender = CONSOLE
            // TODO: CONSOLE Implementation
        }

    }
}

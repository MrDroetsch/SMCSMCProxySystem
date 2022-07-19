package me.bunnykick.smc.proxysystem.bansystem.commands;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.enums.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.enums.SystemPermissions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class ArchiveBans extends Command {

    private final BanSystem banSystem;

    /**
     * Constructor
     * @param name
     * @param banSystem
     */
    public ArchiveBans(String name, BanSystem banSystem) {
        super(name);
        this.banSystem = banSystem;
    }

    /**
     * Command Execute
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) { // sender = Player

            // get Player
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // check Permissions
            String permission = banSystem.plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!player.hasPermission(permission)) {
                Methods.sendMessage(player, banSystem.plugin.getSystemConfig().getMessage(SystemMessages.NOPERM));
                return;
            }

            // get All Archived Data
            String[][] allData = MySQLBan.getArchiveInformation();

            // check if Data are available
            if(allData == null) {
                Methods.sendMessage(player, "§2Derzeit sind keine Bans in der Datenbank archiviert.");
                return;
            }

            // Save File
            try {
                banSystem.getBanLogfileManager().saveFile(allData);
                Methods.sendMessage(player, "§2Datei erfolgreich erstellt.");
            } catch (IOException e) {
                Methods.sendMessage(player, "§cFehler beim erstellen der Datei!");
                return;
            }

            // delete Bans in DB
            MySQLBan.deleteArchiveInformation();

        } else { // sender = CONSOLE
            // TODO: CONSOLE Command
        }

    }
}

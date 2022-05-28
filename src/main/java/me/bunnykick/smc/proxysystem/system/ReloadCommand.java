package me.bunnykick.smc.proxysystem.system;

import me.bunnykick.smc.proxysystem.ProxySystem;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.SystemPermissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class ReloadCommand extends Command {

    /**
     * Command Variables
     */
    private final ProxySystem plugin;

    /**
     * Constructor
     * @param command Command String
     */
    public ReloadCommand(String command, ProxySystem plugin) {
        super(command);
        this.plugin = plugin;
    }

    /**
     * OnCommand Method
     * @param sender Sender of reload Command
     * @param args Arguments given
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) { // ProxiedPlayer

            // Initializing Player
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // Checking Permissions
            String permission = plugin.getSystemConfig().getPermission(SystemPermissions.RELOAD);
            String adminPermission = plugin.getSystemConfig().getPermission(SystemPermissions.ADMIN);
            if(!player.hasPermission(permission) && !player.hasPermission(adminPermission)) {
                player.sendMessage(Methods.translateChatColors(plugin.getSystemConfig().getMessage(SystemMessages.NOPERM)));
                return;
            }

            // Checking arguments
            if(args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(Methods.translateChatColors(ChatColor.RED + "Ungültige Eingabe: /proxysystem reload"));
                return;
            }

            // Reloading Proxy System...
            plugin.reload();

            // Send Message
            Methods.sendMessage(player, ChatColor.GRAY + "Das ProxySystem-Plugin wurde erfolgreich reloadet.");

        } else { // Console

            // Checking arguments
            if(args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(Methods.translateChatColors("Ungueltige Eingabe: /proxysystem reload"));
                return;
            }

            plugin.reload();

            sender.sendMessage(Methods.translateChatColors("Das ProxySystem-Plugin wurde erfolgreich reloadet."));

        }

    }

}

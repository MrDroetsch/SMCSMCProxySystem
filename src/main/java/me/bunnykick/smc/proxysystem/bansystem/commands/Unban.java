package me.bunnykick.smc.proxysystem.bansystem.commands;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.database.MySQLBan;
import me.bunnykick.smc.proxysystem.utils.Methods;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Unban extends Command {

    /**
     * constructor of unban command
     * @param commandName
     * @param banSystem
     */
    public Unban(String commandName, BanSystem banSystem) {
        super(commandName);
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

            if(MySQLBan.)

        } else { // Console Command

        }

    }

}

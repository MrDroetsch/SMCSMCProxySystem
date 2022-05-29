package me.bunnykick.smc.proxysystem.bansystem.commands;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import net.md_5.bungee.api.CommandSender;
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

    }

}

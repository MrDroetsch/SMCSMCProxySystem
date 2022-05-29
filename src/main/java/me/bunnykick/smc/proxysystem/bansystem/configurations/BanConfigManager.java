package me.bunnykick.smc.proxysystem.bansystem.configurations;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanMessages;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanPlaceholders;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.SystemPermissions;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BanConfigManager {

    /**
     * Configuration Variables
     */
    private final BanSystem banSystem;
    private File file;
    private Configuration config;


    /**
     * Constructor
     * @param banSystem BanSystem
     */
    public BanConfigManager(BanSystem banSystem) {
        // Initialize BanSystem
        this.banSystem = banSystem;

        // Create DataFolder
        File banFolder = new File(banSystem.getPlugin().getDataFolder(), "BanSystem");
        if(!banFolder.exists()) {
            banFolder.mkdirs();
        }

        // Create and load ConfigFile
        this.file = new File(banFolder, "BanConfig.yml");
        load();
    }

    /**
     * Load ConfigFile
     */
    public void load() {
        try {
            if(!file.exists()) {
                file.createNewFile();
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

                // Defaults

                // Permissions
                config.set("Permissions.BanPlayer", "proxysystem.banplayer");
                config.set("Permissions.TempbanPlayer", "proxysystem.tempbanplayer");
                config.set("Permissions.UnbanPlayer", "proxysystem.unbanplayer");
                config.set("Permissions.BanIP", "proxysystem.banip");
                config.set("Permissions.TempbanIP", "proxysystem.tempbanip");
                config.set("Permissions.UnbanIP", "proxysystem.unbanip");
                config.set("Permissions.BanInfo", "proxysystem.baninfo");

                // Messages
                config.set("Messages.BanPlayer", Methods.getList("&7Du wurdest auf diesem Netzwerk gebannt!",
                        "&7Von: §c" + BanPlaceholders.ADMIN.label,
                        "&7Grund: &c" + BanPlaceholders.REASON.label,
                        "&7Dauer: &c" + BanPlaceholders.DURATION.label));
                config.set("Messages.BanNotify", Methods.getList("&7Der Spieler &c" + BanPlaceholders.PLAYER.label + "&7 wurde gebannt!",
                        "&7Von: §c" + BanPlaceholders.ADMIN.label,
                        "&7Grund: &c" + BanPlaceholders.REASON.label,
                        "&7Dauer: &c" + BanPlaceholders.DURATION.label));

                save();
            } else {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save ConfigFile
     */
    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all Permissions defined for the given Command
     * @param permissionEnum Enum to identify the Command
     * @return StringList containing all Permissions
     */
    public String getPermission(SystemPermissions permissionEnum) {
        String permission = null;

        String path = "Permissions." + permissionEnum.label;
        if(config.contains(path)) {
            permission = config.getString(path);
        }

        return permission;
    }

    /**
     * Gets the Kick Message for the Player
     * @param messageEnum Which kind of Kick is it?
     * @return List of Lines in the Message
     */
    public List<String> getMessage(BanMessages messageEnum) {
        List<String> retList = new ArrayList<>();

        String path = "Messages." + messageEnum.label;
        if(config.contains(path)) {
            retList = config.getStringList(path);
        }

        return retList;
    }

}

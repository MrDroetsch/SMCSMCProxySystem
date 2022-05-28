package me.bunnykick.smc.proxysystem.system;

import me.bunnykick.smc.proxysystem.ProxySystem;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.SystemPermissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SystemConfigManager {

    /**
     * Configuration Variables
     */
    private final ProxySystem plugin;   // SystemPlugin
    private File file;                  // ConfigFile
    private Configuration config;       // Config

    /**
     * Constructor
     * @param plugin SystemPlugin
     */
    public SystemConfigManager(ProxySystem plugin) {

        // Initialize SystemPlugin
        this.plugin = plugin;

        // Create DataFolder
        if(!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdirs();
        }

        // Create and load ConfigFile
        this.file = new File(this.plugin.getDataFolder(), "SystemConfig.yml");
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

                /*
                 * Defaults
                 */

                // MySQL Connection
                config.set("MySQL.Host", "localhost");
                config.set("MySQL.Port", "3306");
                config.set("MySQL.Database", "Database");
                config.set("MySQL.Username", "Username");
                config.set("MySQL.Password", "Password");

                // Systems Enabled
                config.set("BanSystem.Enabled", true);
                config.set("GeneralFunctions.Enabled", true);
                config.set("MuteSystem.Enabled", true);
                config.set("ReportSystem.Enabled", true);
                config.set("ServerAPI.Enabled", true);
                config.set("WartungSystem.Enabled", true);

                // Permissions
                config.set("Permissions.ReloadCommand", Methods.getList("proxysystem.reload", "proxysystem.admin"));

                // Messages
                config.set("Messages.NoPerm", "&4Keine Berechtigung!");
                config.set("Messages.ERROR", "&cERROR!");

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
     * Get the Information of SQL Connection out of SQL String
     * @param SQLString String to identify which Information is wanted
     * @return SQL Connection Information as String
     */
    public String getMySQL(String SQLString) {
        String information = null;
        String path = "MySQL." + SQLString;
        if(config.contains(path)) {
            information = config.getString(path);
        }
        return information;
    }

    /**
     * Checks if the Part of the System is Enabled
     * @param systemString String to identify the System
     * @return Boolean if the System is Enabled
     */
    public boolean isEnabled(String systemString) {

        String path = systemString + ".Enabled";

        if(!config.contains(path))
            return false;

        return config.getBoolean(path);

    }

    /**
     * Gets the permission for given Permission String
     * @param permission given Permission Enum
     * @return Permission as String
     */
    public String getPermission(SystemPermissions permission) {
        String retPerm = null;

        String path = "Permissions." + permission.label;
        if(config.contains(path)) {
            retPerm = config.getString(path);
        }

        return retPerm;
    }

    /**
     * Get a specific Message which is defined
     * @param message Enum which defines the Message
     * @return Message as String
     */
    public String getMessage(SystemMessages message) {
        String retString = ChatColor.RED + "ERROR!";

        String path = "Messages." + message.label;
        if(config.contains(path)) {
            retString = config.getString(path);
        }

        return retString;
    }

}

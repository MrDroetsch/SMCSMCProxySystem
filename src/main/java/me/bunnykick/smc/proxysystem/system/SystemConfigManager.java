package me.bunnykick.smc.proxysystem.system;

import me.bunnykick.smc.proxysystem.ProxySystem;
import me.bunnykick.smc.proxysystem.utils.childs.ConfigManagerChild;
import me.bunnykick.smc.proxysystem.utils.enums.SystemMessages;
import me.bunnykick.smc.proxysystem.utils.enums.SystemPermissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SystemConfigManager implements ConfigManagerChild {

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
    @Override
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

                // Patterns
                config.set("Pattern.FilePattern", "//");
                config.set("Pattern.CSVPattern", ";");

                // Permissions
                config.set("Permissions.AdminPermission", "proxysystem.admin");
                config.set("Permissions.ReloadCommand", "proxysystem.reload");

                // Messages
                config.set("Messages.NoPerm", "&4Keine Berechtigung!");

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
    @Override
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

    /**
     * Gets the String part which defines a new directory on the System
     * default: "//"
     * @return
     */
    public String getFilePattern() {
        String path = "Pattern.FilePattern";

        if(config.contains(path)) {
            return config.getString(path);
        }

        return "//";
    }

    /**
     * Gets the String part which defines a new slot in CSV-files
     * default: ";"
     * @return
     */
    public String getCSVPattern() {
        String path = "Pattern.CSVPattern";

        if(config.contains(path)) {
            return config.getString(path);
        }

        return ";";
    }

}

package me.bunnykick.smc.proxysystem.bansystem.configurations;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.utils.Methods;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                config.set("Permissions.BanPlayer", Methods.getList("proxysystem.banplayer", "proxysystem.admin"));
                config.set("Permissions.TempbanPlayer", Methods.getList("proxysystem.tempbanplayer", "proxysystem.admin"));
                config.set("Permissions.BanIP", Methods.getList("proxysystem.banip", "proxysystem.admin"));
                config.set("Permissions.TempbanIP", Methods.getList("proxysystem.tempbanip", "proxysystem.admin"));
                config.set("Permissions.BanInfo", Methods.getList("proxysystem.baninfo", "proxysystem.admin"));

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
     * @param commandString String to identify the Command
     * @return StringList containing all Permissions
     */
    public List<String> getPermissions(String commandString) {
        List<String> retList = new ArrayList<>();

        String path = "Permissions." + commandString;
        if(config.contains(path)) {
            retList = config.getStringList(path);
        }

        return retList;
    }

}

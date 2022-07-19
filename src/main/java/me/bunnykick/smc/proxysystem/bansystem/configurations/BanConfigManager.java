package me.bunnykick.smc.proxysystem.bansystem.configurations;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanMessages;
import me.bunnykick.smc.proxysystem.utils.Methods;
import me.bunnykick.smc.proxysystem.utils.childs.ConfigManagerChild;
import me.bunnykick.smc.proxysystem.utils.enums.Placeholders;
import me.bunnykick.smc.proxysystem.utils.enums.SystemPermissions;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BanConfigManager implements ConfigManagerChild {

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
        File banFolder = new File(banSystem.plugin.getDataFolder(), "BanSystem");
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
                config.set("Permissions." + SystemPermissions.BAN.label, "proxysystem.banplayer");
                config.set("Permissions." + SystemPermissions.TEMP_BAN.label, "proxysystem.tempbanplayer");
                config.set("Permissions." + SystemPermissions.UNBAN.label, "proxysystem.unban");
                config.set("Permissions." + SystemPermissions.BAN_IP.label, "proxysystem.banip");
                config.set("Permissions." + SystemPermissions.TEMP_BAN_IP.label, "proxysystem.tempbanip");
                config.set("Permissions." + SystemPermissions.BAN_INFO.label, "proxysystem.baninfo");
                config.set("Permissions." + SystemPermissions.BAN_NOTIFY.label, "proxysystem.bannotify");
                config.set("Permissions." + SystemPermissions.BAN_STAT.label, "proxysystem.banstat");
                config.set("Permissions." + SystemPermissions.BAN_ARCHIVE.label, Methods.getList("Increase_Gaming", "BunnyKick"));

                // Messages
                config.set("Messages." + BanMessages.BAN_PLAYER.label, Methods.getList(
                        "&7Du wurdest auf diesem Netzwerk gebannt!",
                        "&7Von: &c" + Placeholders.ADMIN.label,
                        "&7Grund: &c" + Placeholders.REASON.label,
                        "&7Bis: &c" + Placeholders.DURATION.label,
                        "&7IP-Ban: &c" + Placeholders.IP_BANNED.label
                ));
                config.set("Messages." + BanMessages.BAN_NOTIFY.label, Methods.getList(
                        "&7Der Spieler &c" + Placeholders.PLAYER.label + "&7 wurde gebannt!",
                        "&7Von: &c" + Placeholders.ADMIN.label,
                        "&7Grund: &c" + Placeholders.REASON.label,
                        "&7Bis: &c" + Placeholders.DURATION.label,
                        "&7IP-Ban: &c" + Placeholders.IP_BANNED.label
                ));
                config.set("Messages." + BanMessages.BAN_INFO.label, Methods.getList(
                        "&7Ban-Info von &c" + Placeholders.PLAYER.label + "&7:",
                        "&7IP: &c" + Placeholders.IP.label,
                        "&7Ban-Count: &c" + Placeholders.COUNT.label,
                        "&7Aktuell gebannt: &c" + Placeholders.STILL_BANNED.label,
                        "&7Zuletzt gebannt von: &c" + Placeholders.ADMIN.label,
                        "&7IP-Ban: &c" + Placeholders.IP_BANNED.label,
                        "&7Grund: &c" + Placeholders.REASON.label,
                        "&7Bis: &c" + Placeholders.DURATION.label
                ));
                config.set("Messages." + BanMessages.BAN_STAT.label, Methods.getList(
                        "&7Ban-Statistic von &c" + Placeholders.PLAYER.label + "&7:",
                        "&7UUID: &c" + Placeholders.UUID.label,
                        "&7IP: &c" + Placeholders.IP.label,
                        "&7Gebannt von: &c" + Placeholders.ADMIN.label,
                        "&7Grund: &c" + Placeholders.REASON.label,
                        "&7Gebannt am: &c" + Placeholders.BANNED_FROM.label,
                        "&7Gebannt bis: &c" + Placeholders.BANNED_TO.label,
                        "&7IP-Ban: &c" + Placeholders.IP_BANNED.label,
                        "&7Status: &c" + Placeholders.STATUS.label
                ));
                config.set("Messages." + BanMessages.UNBAN_NOTIFY.label, Methods.getList(
                        "&7Der Spieler &c" + Placeholders.PLAYER.label + "&7 wurde entbannt!",
                        "&7Gebannt von: &c" + Placeholders.ADMIN.label,
                        "&7IP-Ban: &c" + Placeholders.IP_BANNED.label,
                        "&7Grund: &c" + Placeholders.REASON.label,
                        "&7Bis: &c" + Placeholders.DURATION.label
                ));

                // ByPass
                config.set("Bypass.Normalban", Methods.getList(
                        "increase_gaming",
                        "bunnykick",
                        "crazy_old_men",
                        "Halt jeder, der nicht durch normale Bans gebannt werden darf. IP-Bans sind dennoch moeglich.",
                        "WICHTIG: alles klein schreiben in den Namen!! (so wie oben)"
                ));

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

    /**
     * gets a list of low-case Player names which cant be Banned with /ban or /tempban
     * @return
     */
    public List<String> getBypass() {
        String path = "Bypass.Normalban";
        List<String> retList = null;

        if(config.contains(path)) {
            retList = config.getStringList(path);
        }

        return retList;
    }

}

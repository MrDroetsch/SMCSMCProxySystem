package me.bunnykick.smc.proxysystem.mutesystem.cofigurations;

import me.bunnykick.smc.proxysystem.mutesystem.MuteSystem;
import me.bunnykick.smc.proxysystem.mutesystem.utils.MuteMessages;
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

public class MuteConfigManager implements ConfigManagerChild {

    /**
     * Config Files
     */
    private final MuteSystem muteSystem;
    private File file;
    private Configuration config;

    /**
     * Constructor
     * @param muteSystem
     */
    public MuteConfigManager(MuteSystem muteSystem) {

        // initialize Mute-System
        this.muteSystem = muteSystem;

        // Create DataFolder
        File muteFolder = new File(muteSystem.plugin.getDataFolder(), "MuteSystem");
        if(!muteFolder.exists()) {
            muteFolder.mkdirs();
        }

        // Create and load ConfigFile
        this.file = new File(muteFolder, "MuteConfig.yml");
        load();

    }

    /**
     * Save Config File
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
     * Load Config File
     */
    @Override
    public void load() {
        try {
            if(!file.exists()) {
                file.createNewFile();
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

                // Defaults

                // Messages
                config.set("Messages." + MuteMessages.MUTE_PLAYER.label, Methods.getList(
                        "&2Du wurdest von &c" + Placeholders.ADMIN.label + " &2gemutet.",
                        "&2Du kannst den chat wieder verwenden: &c" + Placeholders.MUTED_TO.label,
                        "&2Grund: &c" + Placeholders.REASON.label
                ));
                config.set("Messages." + MuteMessages.MUTE_NOTIFY.label, Methods.getList(
                        "&c" + Placeholders.PLAYER.label + "&2 wurde von &c" + Placeholders.ADMIN.label + " &2gemutet.",
                        "&2Gemutet bis: &c" + Placeholders.MUTED_TO.label,
                        "&2Grund: &c" + Placeholders.REASON.label
                ));
                config.set("Messages." + MuteMessages.MUTE_INFO.label, Methods.getList(
                        "&2Mute-Info von: &c" + Placeholders.PLAYER.label,
                        "&2Gemutet von: &c" + Placeholders.ADMIN.label,
                        "&2Gemutet bis: &c" + Placeholders.MUTED_TO.label,
                        "&2Grund: &c" + Placeholders.REASON.label
                ));

                // Permissions
                config.set("Permissions." + SystemPermissions.MUTE, "proxysystem.mute");
                config.set("Permissions." + SystemPermissions.MUTE_BYPASS, "proxysystem.mutebypass");
                config.set("Permissions." + SystemPermissions.MUTE_INFO, "proxysystem.muteinfo");
                config.set("Permissions." + SystemPermissions.UNMUTE, "proxysystem.unmute");

                // ByPass
                config.set("Bypass.Mute", Methods.getList(
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
    public List<String> getMessage(MuteMessages messageEnum) {
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
        String path = "Bypass.Mute";
        List<String> retList = null;

        if(config.contains(path)) {
            retList = config.getStringList(path);
        }

        return retList;
    }

}

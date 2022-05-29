package me.bunnykick.smc.proxysystem.utils;

import me.bunnykick.smc.proxysystem.bansystem.utils.BanPlaceholders;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class Methods {

    public static String translateTimestampToString(Timestamp stamp) {
        SimpleDateFormat format = new SimpleDateFormat("[dd.MM.yyyy;HH:mm:ss]");

        return format.format(stamp);
    }

    /**
     * Method to get a StringList out of a bunch of Strings
     * @param array a bunch of Strings
     * @return StringList containing all given Strings
     */
    public static List<String> getList(String... array) {
        return Arrays.stream(array).toList();
    }

    /**
     * Translating ChatColors from String to TextComponent
     * @param string Given String to edit
     * @return TextComponent. Not deprecated in ProxiedPlayer.sendMessage();
     */
    public static TextComponent translateChatColors(String string) {
        return new TextComponent(translateColors(string));
    }

    /**
     * Translating Color-Codes from String to String
     * @param string Given String to edit
     * @return String. Deprecated in ProxiedPlayer.sendMessage();
     */
    public static String translateColors(String string) {
        if(!string.contains("&"))
            return string;

        return string.replace("&", "§");
    }

    /**
     * Directly send the given string to given Player
     * @param player Player
     * @param messageToSend String
     */
    public static void sendMessage(ProxiedPlayer player, String messageToSend) {
        player.sendMessage(translateChatColors(messageToSend));
    }

    /**
     * Replaces the Placeholders to their Values
     * @param placeholder
     * @param string String to Change
     * @param value
     * @return Given String with Value
     */
    public static String translatePlaceholder(BanPlaceholders placeholder, String string, String value) {
        String label = placeholder.label;

        if(string.contains(label)) {
            string = string.replace(label, value);
        }

        return string;
    }
}

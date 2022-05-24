package me.bunnykick.smc.proxysystem.utils;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public class Methods {

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

}

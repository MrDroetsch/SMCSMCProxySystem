package me.bunnykick.smc.proxysystem.utils;

import me.bunnykick.smc.proxysystem.bansystem.utils.BanInfoIndex;
import me.bunnykick.smc.proxysystem.bansystem.utils.BanPlaceholders;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.SocketAddress;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Methods {

    /**
     * returns a String with the raw IP without / or Port
     * @param address
     * @return
     */
    public static String getIP(SocketAddress address) {
        String ip = address.toString();
        if(ip.contains(":"))
            ip = ip.split(":")[0];
        if(ip.startsWith("/"))
            ip = ip.substring(1);
        return ip;
    }

    /**
     * gets the SDF string out of a timestamp
     * @param stamp
     * @return
     */
    public static String translateTimestampToString(Timestamp stamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        return format.format(stamp);
    }

    /**
     * Method to get a StringList out of a bunch of Strings
     * @param array a bunch of Strings
     * @return StringList containing all given Strings
     */
    public static List<String> getList(String... array) {
        List<String> list = new ArrayList<>();
        for(String current : array) {
            list.add(current);
        }
        return list;
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

    /**
     * Replaces the Placeholders to their Values
     * @param string
     * @param values
     * @return
     */
    public static String translatePlaceholders(String string, String[] values) {
        String label;
        for(BanPlaceholders holder : BanPlaceholders.values()) {
            label = holder.label;
            switch(holder) {
                case PLAYER:
                    string = string.replace(label, values[BanInfoIndex.PLAYER.index]);
                    break;
                case ADMIN:
                    string = string.replace(label, values[BanInfoIndex.ADMIN.index]);
                    break;
                case REASON:
                    string = string.replace(label, values[BanInfoIndex.LAST_REASON.index]);
                    break;
                case DURATION:
                    string = string.replace(label, values[BanInfoIndex.DURATION.index]);
                    break;
                case IP_BANNED:
                    string = string.replace(label, values[BanInfoIndex.IP_BANNED.index]);
                    break;
                case IP:
                    string = string.replace(label, values[BanInfoIndex.IP.index]);
                    break;
                case COUNT:
                    string = string.replace(label, values[BanInfoIndex.BAN_COUNT.index]);
                    break;
                case STILL_BANNED:
                    string = string.replace(label, values[BanInfoIndex.BANNED.index]);
                    break;
            }
        }

        return string;
    }


    public static long getAddedMillis(String argument) {
        try {
            char[] charArray = argument.toCharArray();
            char einheit = charArray[charArray.length-1];
            long zahl = Long.parseLong(argument.substring(0, argument.length()-1));
            switch(einheit) {
                case 's':
                    return zahl*1000L;
                case 'm':
                    return zahl*1000L*60L;
                case 'h':
                    return zahl*1000L*60L*60L;
                case 'd':
                    return zahl*1000L*60L*60L*24L;
                case 'w':
                    return zahl*1000L*60L*60L*24L*7L;
                case 'y':
                    return zahl*1000L*60L*60L*24L*365L;
            }
        } catch(NumberFormatException e) {}
        return 0L;
    }

}

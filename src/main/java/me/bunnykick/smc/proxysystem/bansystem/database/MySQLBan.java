package me.bunnykick.smc.proxysystem.bansystem.database;

import me.bunnykick.smc.proxysystem.bansystem.utils.BanInfoIndex;
import me.bunnykick.smc.proxysystem.bansystem.utils.CheckBanIndex;
import me.bunnykick.smc.proxysystem.system.database.MySQLConnect;
import me.bunnykick.smc.proxysystem.utils.Methods;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLBan {

    /**
     * Create new Table if not exists
     * Structure:
     * Name (String)
     * UUID (String)
     * IP (String)
     * Admin (String)
     * Reason (String)
     * Banned (Timestamp)
     * Unbanned (Timestamp)
     * Permanent (boolean)
     * IPBanned (boolean)
     * Pardon (boolean)
     */
    public static void createNewTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Banned ("
                + "Name VARCHAR(100),"
                + "UUID VARCHAR(100),"
                + "IP VARCHAR(100),"
                + "Admin VARCHAR(100),"
                + "Reason VARCHAR(100),"
                + "Banned TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "BannedTo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "Permanent BOOLEAN,"
                + "IPBanned BOOLEAN,"
                + "Pardon BOOLEAN DEFAULT false"
                + ");";

        try{
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Add a banned player to database
     * @param name
     * @param UUID
     * @param IP
     * @param admin
     * @param reason
     * @param perma
     * @param banIP
     * @return Return Boolean if it was successfull
     */
    public static boolean banPlayer(String name, String UUID, String IP, String admin, String reason, boolean perma, boolean banIP) {

        String sql = "INSERT INTO Banned (Name, UUID, IP, Admin, Reason, Permanent, IPBanned) VALUES (?,?,?,?,?,?,?);";

        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name.toLowerCase());
            ps.setString(2, UUID);
            ps.setString(3, IP);
            ps.setString(4, admin);
            ps.setString(5, reason);
            ps.setBoolean(6, perma);
            ps.setBoolean(7, banIP);
            ps.executeUpdate();
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Add a timed banned player to database
     * @param name
     * @param UUID
     * @param IP
     * @param admin
     * @param reason
     * @param bannedTo
     * @param perma
     * @param banIP
     * @return
     */
    public static boolean banPlayer(String name, String UUID, String IP, String admin, String reason, Timestamp bannedTo, boolean perma, boolean banIP) {

        String sql = "INSERT INTO Banned (Name, UUID, IP, Admin, Reason, BannedTo, Permanent, IPBanned) VALUES (?,?,?,?,?,?,?,?);";

        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name.toLowerCase());
            ps.setString(2, UUID);
            ps.setString(3, IP);
            ps.setString(4, admin);
            ps.setString(5, reason);
            ps.setTimestamp(6, bannedTo);
            ps.setBoolean(7, perma);
            ps.setBoolean(8, banIP);
            ps.executeUpdate();
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Archives the not longer handling Ban
     * @param uuid
     * @param name
     * @param ip
     */
    public static void pardon(String uuid, String name, String ip) {
        String sql = "UPDATE Banned SET Pardon = true WHERE UUID = ? OR Name = ? OR IP = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, uuid);
            ps.setString(2, name.toLowerCase());
            ps.setString(3, ip);
            ps.executeUpdate();
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Archives the not longer handling Tempban
     * @param name
     */
    public static void pardon(String name) {
        String sql = "UPDATE Banned SET Pardon = true WHERE Name = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name.toLowerCase());
            ps.executeUpdate();
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Checking if a player is banned and return message values
     * @param uuid
     * @param name
     * @param ip
     * @return String[] CheckBanIndex ADMIN, REASON, DURATION
     */
    public static String[] checkBanned(String uuid, String name, String ip) {
        String sql = "SELECT * FROM Banned WHERE UUID = ? OR Name = ? OR IP = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, uuid);
            ps.setString(2, name.toLowerCase());
            ps.setString(3, ip);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(!rs.getBoolean("Pardon")) {
                    if(!rs.getBoolean("IPBanned") && !rs.getString("Name").equalsIgnoreCase(name)) {
                        return null;
                    }
                    String[] retVal = new String[CheckBanIndex.values().length];
                    retVal[CheckBanIndex.ADMIN.i] = rs.getString("Admin");
                    retVal[CheckBanIndex.REASON.i] = rs.getString("Reason");
                    String duration;
                    if(rs.getBoolean("Permanent")) {
                        duration = "PERMANENT";
                    } else {
                        Timestamp bannedTo = rs.getTimestamp("BannedTo");
                        long dur = bannedTo.getTime() - System.currentTimeMillis();

                        if(dur <= 0) {
                            pardon(uuid, name, ip);
                            return null;
                        }
                        duration = Methods.translateTimestampToString(bannedTo);
                    }
                    retVal[CheckBanIndex.DURATION.i] = duration;
                    retVal[CheckBanIndex.IP_BANNED.i] = rs.getBoolean("IPBanned") ? "JA" : "NEIN";
                    return retVal;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checking if a player is banned and return message values
     * @param name
     * @return String[] CheckBanIndex ADMIN, REASON, DURATION
     */
    public static String[] checkBanned(String name) {
        String sql = "SELECT * FROM Banned WHERE Name = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name.toLowerCase());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(!rs.getBoolean("Pardon")) {
                    String[] retVal = new String[CheckBanIndex.values().length];
                    retVal[CheckBanIndex.ADMIN.i] = rs.getString("Admin");
                    retVal[CheckBanIndex.REASON.i] = rs.getString("Reason");
                    String duration;
                    if(rs.getBoolean("Permanent")) {
                        duration = "PERMANENT";
                    } else {
                        Timestamp bannedTo = rs.getTimestamp("BannedTo");
                        long dur = bannedTo.getTime() - System.currentTimeMillis();

                        if(dur <= 0) {
                            pardon(name);
                            return null;
                        }
                        duration = Methods.translateTimestampToString(bannedTo);
                    }
                    retVal[CheckBanIndex.DURATION.i] = duration;
                    retVal[CheckBanIndex.IP_BANNED.i] = rs.getBoolean("IPBanned") ? "JA" : "NEIN";
                    return retVal;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all Information out of Database from Username
     * @param name
     * @return returning string array with ban information
     */
    public static String[] getInfo(String name) {
        String sql = "SELECT * FROM Banned WHERE Name = ?;";
        try {
            String[] retVal = new String[BanInfoIndex.values().length];
            String player = name;
            String ip = "";
            String lastAdmin = "";
            String lastReason = "";
            String duration = "";
            boolean stillBanned = false;
            boolean ipBanned = false;
            int banCount = 0;

            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name.toLowerCase());
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                // increase bancount
                banCount++;

                // StillBanned?
                stillBanned = !rs.getBoolean("Pardon");

                // IPBanned?
                ipBanned = rs.getBoolean("IPBanned");

                // Last Reason
                lastReason = rs.getString("Reason");

                // admin
                lastAdmin = rs.getString("Admin");

                // ip
                ip = rs.getString("IP") == null ? "Nicht gefunden" : rs.getString("IP");

                // duration
                if(rs.getBoolean("Permanent")) {
                    duration = "PERMANENT";
                } else {
                    Timestamp bannedTo = rs.getTimestamp("BannedTo");
                    duration = Methods.translateTimestampToString(bannedTo);
                }

            }

            retVal[BanInfoIndex.PLAYER.index] = name;
            retVal[BanInfoIndex.IP.index] = ip;
            retVal[BanInfoIndex.ADMIN.index] = lastAdmin;
            retVal[BanInfoIndex.LAST_REASON.index] = lastReason;
            retVal[BanInfoIndex.DURATION.index] = duration;
            retVal[BanInfoIndex.BANNED.index] = stillBanned ? "JA" : "NEIN";
            retVal[BanInfoIndex.IP_BANNED.index] = ipBanned ? "JA" : "NEIN";
            retVal[BanInfoIndex.BAN_COUNT.index] = Integer.toString(banCount);

            if(banCount == 0)
                return null;

            return retVal;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*String sql = "CREATE TABLE IF NOT EXISTS Banned ("
            + "Name VARCHAR(100),"
            + "UUID VARCHAR(100),"
            + "IP VARCHAR(100),"
            + "Admin VARCHAR(100),"
            + "Reason VARCHAR(100),"
            + "Banned TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + "BannedTo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + "Permanent BOOLEAN,"
            + "IPBanned BOOLEAN,"
            + "Pardon BOOLEAN DEFAULT false"
            + ");";*/

    /**
     * Gets all Information that are saved in MySQL with the Name
     * @param name
     * @return
     */
    public static String[][] getAllInformation(String name) {
        String sql = "SELECT * FROM Banned WHERE Name = ?";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name.toLowerCase());
            ResultSet rs = ps.executeQuery();

            String[][] retVal;

            List<String[]> lines = new ArrayList<>();

            String[] line = new String[8];

            while (rs.next()) {

                line[0] = rs.getString("UUID") == null ? "Nicht gefunden" : rs.getString("UUID");
                line[1] = rs.getString("IP") == null ? "Nicht gefunden" : rs.getString("IP");
                line[2] = rs.getString("Admin");
                line[3] = rs.getString("Reason");
                line[4] = Methods.translateTimestampToString(rs.getTimestamp("Banned"));
                line[5] = rs.getBoolean("Permanent") ? "PERMANENT" : Methods.translateTimestampToString(rs.getTimestamp("BannedTo"));
                line[6] = rs.getBoolean("IPBanned") ? "JA" : "NEIN";
                line[7] = rs.getBoolean("Pardon") ? "ARCHIVIERT" : "GELTEND";
                
                lines.add(line.clone());

            }

            retVal = new String[lines.size()][8];

            String[] curLine;
            for(int i = 0; i < lines.size(); i++) {
                curLine = lines.get(i);
                retVal[i] = curLine;
            }

            if(lines.size() > 0) {
                return retVal;
            } else {
                return null;
            }

        } catch(SQLException e) {}
        return null;
    }

}

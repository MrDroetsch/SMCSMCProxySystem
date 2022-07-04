package me.bunnykick.smc.proxysystem.bansystem.database;

import me.bunnykick.smc.proxysystem.bansystem.utils.BanInfoIndex;
import me.bunnykick.smc.proxysystem.bansystem.utils.CheckBanIndex;
import me.bunnykick.smc.proxysystem.system.database.MySQLConnect;
import me.bunnykick.smc.proxysystem.utils.Methods;

import java.sql.*;

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
            ps.setString(1, name);
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
        // TODO: TempBan / TempBanIP
        return false;
    }

    /**
     * Archives the not longer handling Tempban
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
                    String[] retVal = new String[3];
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
    public String[] getInfo(String name) {
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
                stillBanned = rs.getBoolean("Pardon");

                // IPBanned?
                ipBanned = rs.getBoolean("IPBanned");

                // Last Reason
                lastReason = rs.getString("Reason");

                // admin
                lastAdmin = rs.getString("Admin");

                // ip
                ip = rs.getString("IP");

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
            retVal[BanInfoIndex.BANNED.index] = Boolean.toString(stillBanned);
            retVal[BanInfoIndex.IP_BANNED.index] = Boolean.toString(ipBanned);
            retVal[BanInfoIndex.BAN_COUNT.index] = Integer.toString(banCount);

            return retVal;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}

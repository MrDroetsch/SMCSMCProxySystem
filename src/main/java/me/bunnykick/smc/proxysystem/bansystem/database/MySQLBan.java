package me.bunnykick.smc.proxysystem.bansystem.database;

import me.bunnykick.smc.proxysystem.bansystem.utils.CheckBanIndex;
import me.bunnykick.smc.proxysystem.system.MySQLConnect;
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
                + "BannedTo TIMESTAMP,"
                + "Permanent BOOLEAN,"
                + "IPBanned BOOLEAN,"
                + "Pardon BOOLEAN DEFAULT false"
                + ");";

        try{
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.err.println(e.getMessage());
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
     * Checking if a player is banned and return message values
     * @param uuid
     * @param name
     * @param ip
     * @return boolean banned?
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
                    String[] retVal = new String[3];
                    retVal[CheckBanIndex.ADMIN.i] = rs.getString("Admin");
                    retVal[CheckBanIndex.REASON.i] = rs.getString("Reason");
                    String duration;
                    if(rs.getBoolean("Perma")) {
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
                    return retVal;
                }
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

}

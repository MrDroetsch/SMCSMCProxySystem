package me.bunnykick.smc.proxysystem.bansystem.database;

import me.bunnykick.smc.proxysystem.system.MySQLConnect;

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
     */
    public static void createNewTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Banned ("
                + "Name VARCHAR(100),"
                + "UUID VARCHAR(100),"
                + "IP VARCHAR(100),"
                + "Admin VARCHAR(100),"
                + "Reason VARCHAR(100),"
                + "Banned TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "Unbanned TIMESTAMP"
                + "Permanent BOOLEAN"
                + "IPBanned BOOLEAN"
                + ");";

        try{
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add a Banned Player to Database
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

        String sql = "INSERT INTO Banned (Name, UUID, IP, Admin, Reason, Permanent, IPBanned) VALUES (?,?,?,?,?,?,?)";

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

    public static boolean banPlayer(String name, String UUID, String IP, String admin, String reason, Timestamp bannedTo, boolean perma, boolean banIP) {
        // TODO: TempBan / TempBanIP
        return false;
    }

}

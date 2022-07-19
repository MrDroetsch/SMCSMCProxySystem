package me.bunnykick.smc.proxysystem.mutesystem.database;

import me.bunnykick.smc.proxysystem.system.database.MySQLConnect;
import me.bunnykick.smc.proxysystem.utils.Methods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MySQLMute {

    /**
     * create Table muted
     * Name VARCHAR(100)
     * UUID VARCHAR(100)
     * Admin VARCHAR(100)
     * Muted TIMESTAMP
     * MutedTo TIMESTAMP
     * Reason VARCHAR(100)
     * Pardon BOOLEAN
     */
    public static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS muted (" +
                "Name VARCHAR(100), " +
                "UUID VARCHAR(100), " +
                "Admin VARCHAR(100), " +
                "Muted TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "MutedTo TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "Reason VARCHAR(100));";

        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a new Player who is going to be muted
     * @param name
     * @param uuid
     * @param admin
     * @param mutedTo
     * @param reason
     */
    public static boolean mutePlayer(String name, String uuid, String admin, Timestamp mutedTo, String reason) {
        String sql = "INSERT INTO muted (Name, UUID, Admin, Muted, MutedTo, Reason) VALUES (?,?,?,?,?,?);";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, uuid);
            ps.setString(3, admin);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(5, mutedTo);
            ps.setString(6, reason);

            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete all lines with Player name
     * @param name
     */
    public static void unMutePlayer(String name) {
        String sql = "DELETE FROM Muted WHERE Name = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets Admin, MutedTo, Reason
     * @param name
     * @return
     */
    public static String[] getMuteInfo(String name) {
        String sql = "SELECT (Admin, MutedTo, Reason) FROM Muted WHERE Name = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                String[] retVal = new String[3];
                retVal[0] = rs.getString("Admin");
                retVal[1] = Methods.translateTimestampToString(rs.getTimestamp("MutedTo"));
                retVal[2] = rs.getString("Reason");
                return retVal;
            }
            return null;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the Date when he's inmuted if he's muted
     * @param name
     * @param uuid
     * @return
     */
    public static Timestamp isMuted(String name, String uuid) {
        String sql = "SELECT MutedTo FROM Muted WHERE Name = ? OR UUID = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, uuid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getTimestamp("MutedTo");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the Date when he's inmuted if he's muted
     * @param name
     * @return
     */
    public static Timestamp isMuted(String name) {
        String sql = "SELECT MutedTo FROM Muted WHERE Name = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getTimestamp("MutedTo");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

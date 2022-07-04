package me.bunnykick.smc.proxysystem.system.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUUID {

    /**
     * Creating UUID Table
     * Name String
     * UUID String
     */
    public static void createTableIFNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS PlayerLog (Name VARCHAR(100) NOT NULL, IP VARCHAR(100), UUID VARCHAR(100) NOT NULL UNIQUE);";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Register the UUID and the IP of a Player if it isn't already
     * @return Boolean if it was updated
     */
    public static boolean registerPlayerIfNotRegistered(String name, String uuid, String ip) {
        String sql = "SELECT Name, IP FROM PlayerLog WHERE UUID = ?;";

        // Update/Insert
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) { // Not registered yet
                sql = "INSERT INTO PlayerLog (UUID, IP, Name) VALUES (?,?,?);";
                ps = MySQLConnect.getConnection().prepareStatement(sql);
                ps.setString(1, uuid);
                ps.setString(2, ip);
                ps.setString(3, name);
                ps.executeUpdate();
            } else { // Update if IP/Name changed
                if(!rs.getString("Name").equals(name) || !rs.getString("IP").equals(ip)) {
                    sql = "UPDATE PlayerLog SET Name=?,IP=?;";
                    ps = MySQLConnect.getConnection().prepareStatement(sql);
                    ps.setString(1, name);
                    ps.setString(2, ip);
                    ps.executeUpdate();
                    return true;
                }
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static String getUUID(String name) {
        String sql = "SELECT UUID FROM PlayerLog WHERE Name = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name.toLowerCase());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getString("UUID");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static String getIP(String name) {
        String sql = "SELECT IP FROM PlayerLog WHERE Name = ?;";
        try {
            PreparedStatement ps = MySQLConnect.getConnection().prepareStatement(sql);
            ps.setString(1, name.toLowerCase());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getString("IP");
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

}

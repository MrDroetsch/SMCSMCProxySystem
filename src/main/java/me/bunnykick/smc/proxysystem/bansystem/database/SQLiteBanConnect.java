package me.bunnykick.smc.proxysystem.bansystem.database;

import me.bunnykick.smc.proxysystem.ProxySystem;

import java.sql.*;

public class SQLiteBanConnect {

    /**
     * Variables
     */
    public static Connection con;
    private static String url = "jdbc:sqlite:" + ProxySystem.getInstance().getDataFolder() + "/BannedPlayers.db";

    /**
     * Connect to DB
     */
    public static void connect() {
        con = null;
        try {
            // create a connection to the database
            con = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            createNewDatabase();
        }
    }

    /**
     * Disconnect from DB
     */
    public static void disconnect() {
        try {
            if(con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new Database File
     */
    public static void createNewDatabase() {
        try {
            con = DriverManager.getConnection(url);
            if (con != null) {
                DatabaseMetaData meta = con.getMetaData();
                System.out.println("[ProxySystem] BanSystem Database wurde erstellt.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
    public static void createNewTable() {
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
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

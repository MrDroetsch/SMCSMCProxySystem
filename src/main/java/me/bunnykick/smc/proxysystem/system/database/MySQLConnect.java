package me.bunnykick.smc.proxysystem.system.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {

    private static String url;
    private static Connection con;

    public static void setVariables(String database, String username, String password, String host, String port) {
        url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password;
    }

    public static boolean isConnected() {
        try {
            return con != null && !con.isClosed();
        } catch(SQLException e) {
            con = null;
            return false;
        }
    }

    public static boolean connect() {
        try {
            con = DriverManager.getConnection(url);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static void disconnect() {
        if(isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        if(!isConnected())
            connect();
        return con;
    }

}

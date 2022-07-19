package me.bunnykick.smc.proxysystem.reportsystem.database;

public class MySQLReport {

    public static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Reports (" +
                "Name VARCHAR(100), " +
                "ReportedFrom VARCHAR(100), " +
                "ReportMessage VARCHAR(100), " +
                "ReportedWhen TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "Handling BOOLEAN, " +
                "HandlingAdmin VARCHAR(100), " +
                "Answer VARCHAR(100), " +
                "Status INT DEFAULT 0);";
    }

}

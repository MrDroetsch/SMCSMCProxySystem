package me.bunnykick.smc.proxysystem.bansystem.configurations;

import me.bunnykick.smc.proxysystem.bansystem.BanSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BanLog {

    /**
     * Variables
     */
    private final BanSystem banSystem;

    /**
     * Constructor
     * @param banSystem
     */
    public BanLog(BanSystem banSystem) {
        this.banSystem = banSystem;
    }

    /**
     * Saves All data of the String array to File
     * Name (String)
     * UUID (String)
     * IP (String)
     * Admin (String)
     * Reason (String)
     * Banned (Timestamp)
     * BannedTo (Timestamp)
     * Permanent (boolean)
     * IPBanned (boolean)
     * @param data
     */
    public void saveFile(String[][] data) throws IOException {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filePattern = banSystem.getPlugin().getSystemConfig().getFilePattern();
        File file = new File(banSystem.getPlugin().getDataFolder(), filePattern + "BanSystem" + filePattern + "Logs");
        if(!file.exists())
            file.mkdirs();
        file = new File(banSystem.getPlugin().getDataFolder(), filePattern + "BanSystem" + filePattern + "Logs" + filePattern + sdf.format(date) + ".csv");
        for(int i = 1; file.exists(); i++) {
            file = new File(banSystem.getPlugin().getDataFolder(), filePattern + "BanSystem" + filePattern + "Logs" + filePattern + sdf.format(date) + "(" + i + ").csv");
        }

        file.createNewFile();

        String[] header = {"Name", "UUID", "IP", "Admin", "Reason", "BannedWhen", "BannedTo", "Permanent", "IPBan"};

        writeLines(file, header, data);
    }

    private void writeLines(File file, String[] header, String[][] data) throws IOException {
        List<String> lines = new ArrayList<>();
        String line = "";
        String CSVPattern = banSystem.getPlugin().getSystemConfig().getCSVPattern();
        for(int i = 0; i < header.length; i++) {
            line += header[i] + CSVPattern;
        }
        lines.add(line);
        line = "";
        for(int i = 0; i < data.length; i++) {
            String[] nextLine = data[i];
            for(int j = 0; j < nextLine.length; j++) {
                line += nextLine[j] + CSVPattern;
            }
            lines.add(line);
            line = "";
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for(String curLine : lines) {
            writer.write(curLine);
            writer.newLine();
        }
        writer.close();
    }

}

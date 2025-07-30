package me.waterarchery.litlibs.database;

import me.waterarchery.litlibs.LitLibs;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.sql.*;
import java.util.*;

public class SQLite {

    private final LitLibs litLibs;
    private Connection connection;

    public SQLite(LitLibs litLibs) { this.litLibs = litLibs; }

    public void createDatabase(ArrayList<String> SQLiteTokens) {
        for (String token : SQLiteTokens) {
            createTables(token);
        }

        initialize();
    }

    private void createTables(String token) {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(token);
            s.close();
        }
        catch (SQLException e) {
            litLibs.getLogger().error(e.getMessage());
        }
    }

    private void initialize() {
        connection = getSQLConnection();
        try (PreparedStatement vac = connection.prepareStatement("vacuum")) {
            vac.execute();
        }
        catch (SQLException ex) {
            litLibs.getLogger().error("Unable to retreive connection");
        }
    }

    public Connection getSQLConnection() {
        Plugin provider = litLibs.getPlugin();
        String fileName = "database";
        File dataFolder = new File(provider.getDataFolder(), fileName + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            }
            catch (IOException e) {
                litLibs.getLogger().error("File write error: " + fileName + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        }
        catch (SQLException ex) {
            litLibs.getLogger().error("SQLite exception on initialize");
        }
        catch (ClassNotFoundException ex) {
            litLibs.getLogger().error("You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

}

package DbStaff;

import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.io.*;
import java.sql.*;

public class Sector {

    public void execute() {
        String filePath = "/home/landry/Documents/projects/java/rwanda/Sector.txt";
        String dbPath = "/home/landry/Documents/projects/java/multiple/rwandaInt.db";
        String url = "jdbc:sqlite:" + dbPath;

        createDatabaseFile(dbPath);

        try (
                Connection conn = DriverManager.getConnection(url);
                BufferedReader br = new BufferedReader(new FileReader(filePath));
        ) {
            System.out.println("Database connection successful.");
            createTables(conn);
            insertData(conn, br);
            System.out.println("Data successfully inserted into the Sector table.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createSectorTableQuery = "CREATE TABLE IF NOT EXISTS Sector (" +
                "sectorId TEXT PRIMARY KEY, " +
                "districtId TEXT, " +
                "sectorName TEXT, " +
                "FOREIGN KEY (districtId) REFERENCES District(districtId));";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSectorTableQuery);
            System.out.println("Tables created or already exist.");
        }
    }

    private static void insertData(Connection conn, BufferedReader br) throws SQLException, IOException {
        String insertSectorQuery = "INSERT INTO Sector (sectorId, districtId, sectorName) VALUES (?, ?, ?);";

        try (PreparedStatement pstmtSector = conn.prepareStatement(insertSectorQuery)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String sectorId = parts[0];
                    String districtId = parts[1];
                    String sectorName = parts[2];

                    pstmtSector.setString(1, sectorId);
                    pstmtSector.setString(2, districtId);
                    pstmtSector.setString(3, sectorName);
                    pstmtSector.executeUpdate();
                    System.out.println("Inserted Sector: " + sectorName + " with ID: " + sectorId);
                }
            }
        }
    }

    private static void createDatabaseFile(String dbPath) {
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                if (dbFile.createNewFile()) {
                    System.out.println("Database file created at: " + dbPath);
                } else {
                    System.out.println("Failed to create database file at: " + dbPath);
                }
            } catch (IOException e) {
                System.out.println("Error creating database file: " + e.getMessage());
            }
        } else {
            System.out.println("Database file already exists at: " + dbPath);
        }
    }
}

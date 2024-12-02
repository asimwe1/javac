package DbStaff;

import java.io.*;
import java.sql.*;

public class Cell {

    public void execute() {
        String filePath = "/home/landry/Documents/projects/java/rwanda/Cell.txt";
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
            System.out.println("Data successfully inserted into the Cell table.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createCellTableQuery = "CREATE TABLE IF NOT EXISTS Cell (" +
                "cellId TEXT PRIMARY KEY, " +
                "sectorId TEXT, " +
                "cellName TEXT, " +
                "FOREIGN KEY (sectorId) REFERENCES Sector(sectorId));";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createCellTableQuery);
            System.out.println("Tables created or already exist.");
        }
    }

    private static void insertData(Connection conn, BufferedReader br) throws SQLException, IOException {
        String insertCellQuery = "INSERT INTO Cell (cellId, sectorId, cellName) VALUES (?, ?, ?);";

        try (PreparedStatement pstmtCell = conn.prepareStatement(insertCellQuery)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String cellId = parts[0];
                    String sectorId = parts[1];
                    String cellName = parts[2];

                    pstmtCell.setString(1, cellId);
                    pstmtCell.setString(2, sectorId);
                    pstmtCell.setString(3, cellName);
                    pstmtCell.executeUpdate();
                    System.out.println("Inserted Cell: " + cellName + " with ID: " + cellId);
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

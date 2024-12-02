package DbStaff;

import java.io.*;
import java.sql.*;

public class Village {

    public void execute() {
        String filePath = "/home/landry/Documents/projects/java/rwanda/Village.txt";
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
            System.out.println("Data successfully inserted into the Village table.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createVillageTableQuery = "CREATE TABLE IF NOT EXISTS Village (" +
                "villageId TEXT PRIMARY KEY, " +
                "cellId TEXT, " +
                "villageName TEXT, " +
                "FOREIGN KEY (cellId) REFERENCES Cell(cellId));";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createVillageTableQuery);
            System.out.println("Tables created or already exist.");
        }
    }

    private static void insertData(Connection conn, BufferedReader br) throws SQLException, IOException {
        String insertVillageQuery = "INSERT INTO Village (villageId, cellId, villageName) VALUES (?, ?, ?);";

        try (PreparedStatement pstmtVillage = conn.prepareStatement(insertVillageQuery)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String villageId = parts[0];
                    String cellId = parts[1];
                    String villageName = parts[2];

                    pstmtVillage.setString(1, villageId);
                    pstmtVillage.setString(2, cellId);
                    pstmtVillage.setString(3, villageName);
                    pstmtVillage.executeUpdate();
                    System.out.println("Inserted Village: " + villageName + " with ID: " + villageId);
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

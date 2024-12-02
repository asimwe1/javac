package DbStaff;

import java.io.*;
import java.sql.*;

public class Province {

    public void execute() {
        String filePath = "/home/landry/Documents/projects/java/rwanda/Province.txt";
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
            System.out.println("Data successfully inserted into the Province table.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createProvinceTableQuery = "CREATE TABLE IF NOT EXISTS Province (" +
                "provinceId TEXT PRIMARY KEY, " +
                "provinceName TEXT);";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createProvinceTableQuery);
            System.out.println("Tables created or already exist.");
        }
    }

    private static void insertData(Connection conn, BufferedReader br) throws SQLException, IOException {
        String insertProvinceQuery = "INSERT INTO Province (provinceId, provinceName) VALUES (?, ?);";

        try (PreparedStatement pstmtProvince = conn.prepareStatement(insertProvinceQuery)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String provinceId = parts[0];
                    String provinceName = parts[1];
                    pstmtProvince.setString(1, provinceId);
                    pstmtProvince.setString(2, provinceName);
                    pstmtProvince.executeUpdate();
                    System.out.println("Inserted Province: " + provinceName + " with ID: " + provinceId);
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

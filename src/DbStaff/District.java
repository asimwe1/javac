package DbStaff;

import java.io.*;
import java.sql.*;

public class District {

    public void execute() {
        String filePath = "/home/landry/Documents/projects/java/rwanda/District.txt";
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
            System.out.println("Data successfully inserted into the District table.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createDistrictTableQuery = "CREATE TABLE IF NOT EXISTS District (" +
                "districtId TEXT PRIMARY KEY, " +
                "provinceId TEXT, " +
                "districtName TEXT, " +
                "FOREIGN KEY (provinceId) REFERENCES Province(provinceId));";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createDistrictTableQuery);
            System.out.println("Tables created or already exist.");
        }
    }

    private static void insertData(Connection conn, BufferedReader br) throws SQLException, IOException {
        String insertDistrictQuery = "INSERT INTO District (districtId, provinceId, districtName) VALUES (?, ?, ?);";
        String checkDistrictExistsQuery = "SELECT COUNT(*) FROM District WHERE districtId = ?;";

        try (PreparedStatement pstmtDistrict = conn.prepareStatement(insertDistrictQuery);
             PreparedStatement pstmtCheckDistrict = conn.prepareStatement(checkDistrictExistsQuery)) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String districtId = parts[0];
                    String provinceId = parts[1];
                    String districtName = parts[2];

                    pstmtCheckDistrict.setString(1, districtId);
                    ResultSet rs = pstmtCheckDistrict.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        pstmtDistrict.setString(1, districtId);
                        pstmtDistrict.setString(2, provinceId);
                        pstmtDistrict.setString(3, districtName);
                        pstmtDistrict.executeUpdate();
                        System.out.println("Inserted District: " + districtName + " with ID: " + districtId);
                    } else {
                        System.out.println("District ID " + districtId + " already exists. Skipping insertion.");
                    }
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

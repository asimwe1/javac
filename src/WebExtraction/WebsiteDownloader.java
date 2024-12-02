package WebExtraction;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.sql.*;

import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class WebsiteDownloader {
     private static Connection conn;


//    public void execute() {
//        String filePath = "/home/landry/Documents/projects/java/youtube.html";
//        String dbPath = "/home/landry/Documents/projects/java/multiple/datadata.db";
//        String url = "jdbc:sqlite:" + dbPath;
//
//        createDatabaseFile(dbPath);
//
//        try (
//                Connection conn = DriverManager.getConnection(url);
//                BufferedReader br = new BufferedReader(new FileReader(filePath));
//        ) {
//            System.out.println("Database connection successful.");
//            createTables(conn);
//            insertData(conn, br);
//            System.out.println("Data successfully inserted into the websites table.");
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found: " + filePath);
//        } catch (IOException e) {
//            System.out.println("Error reading the file: " + e.getMessage());
//        } catch (SQLException e) {
//            System.out.println("Database error: " + e.getMessage());
//        }
//    }
//

    public static void main(String[] args) throws Exception {
        try {
            initializeDatabase();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter website URL: ");
            String url = scanner.nextLine();
            String domain = new URL(url).getHost();

            Path rootDir = Paths.get("websites");
            Files.createDirectories(rootDir);

            Path outputDir = rootDir.resolve(domain);
            Files.createDirectories(outputDir);

            long startTime = System.currentTimeMillis();
            String homepageFile = downloadFile(url, outputDir.resolve("index.html").toString());
            List<String> externalLinks = extractLinks(homepageFile, domain);
            storeLinksInDatabase(externalLinks, domain);
            downloadExternalResources(externalLinks, outputDir);

            long endTime = System.currentTimeMillis();
            generateCompletionReport(domain, startTime, endTime);
            scanner.close();
        } catch (MalformedURLException e) {
            System.out.println("The URL input is incorrectly formatted: " + e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println("Invalid domain: " + e.getMessage());
        }
    }

    private static void initializeDatabase() throws SQLException {
        String dbPath = "/home/Documents/projects/java/multiple";
        conn = DriverManager.getConnection("jdbc:sqlite:downloads.db");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS websites (id INTEGER PRIMARY KEY, name TEXT, start_time TEXT, end_time TEXT, elapsed_time INTEGER, total_kb INTEGER)");
        stmt.execute("CREATE TABLE IF NOT EXISTS links (id INTEGER PRIMARY KEY, url TEXT, website_id INTEGER, elapsed_time INTEGER, size_kb INTEGER)");
    }

    private static String downloadFile(String url, String outputPath) throws IOException {
        URL website = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) website.openConnection();
        try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(outputPath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return outputPath;
    }

    private static List<String> extractLinks(String htmlFilePath, String domain) throws IOException {
        List<String> externalLinks = new ArrayList<>();

        try {
            Document document = Jsoup.parse(new File(htmlFilePath), "UTF-8");
            Elements links = document.select("a[href], link[href], script[src], img[src]");

            for (Element link : links) {
                String href = link.attr("abs:href");

                if (isValidUrl(href) && !href.contains(domain) && !externalLinks.contains(href)) {
                    externalLinks.add(href);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading or parsing the HTML file: " + e.getMessage());
            throw e;
        }

        return externalLinks;
    }

    private static boolean isValidUrl(String url) {
        Pattern pattern2 = Pattern.compile("[ht{2}ps?]//[a-z0-9].[a-z]");
        Matcher matcher2 = pattern2.matcher(url);
        return true;
    }

    private static void storeLinksInDatabase(List<String> links, String domain) throws SQLException {
        String websiteInsertQuery = "INSERT INTO websites (name, start_time, end_time, elapsed_time, total_kb) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement websiteStmt = conn.prepareStatement(websiteInsertQuery, Statement.RETURN_GENERATED_KEYS);
        websiteStmt.setString(1, domain);
        websiteStmt.setString(2, LocalDateTime.now().toString());
        websiteStmt.setString(3, null);
        websiteStmt.setInt(4, 0);
        websiteStmt.setInt(5, 0);
        websiteStmt.executeUpdate();

        ResultSet keys = websiteStmt.getGeneratedKeys();
        keys.next();
        int websiteId = keys.getInt(1);

        String linkInsertQuery = "INSERT INTO links (url, website_id, elapsed_time, size_kb) VALUES (?, ?, ?, ?)";
        PreparedStatement linkStmt = conn.prepareStatement(linkInsertQuery);
        for (String link : links) {
            linkStmt.setString(1, link);
            linkStmt.setInt(2, websiteId);
            linkStmt.setInt(3, 0);
            linkStmt.setInt(4, 0);
            linkStmt.executeUpdate();
        }
    }


    private static void downloadExternalResources(List<String> links, Path outputDir) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<?>> futures = new ArrayList<>();
        for (String link : links) {
            futures.add(executor.submit(() -> {
                try {
                    String fileName = link.substring(link.lastIndexOf("/") + 1);
                    if (!fileName.endsWith(".html")) {
                        fileName += ".html";
                    }
                    String outputPath = outputDir.resolve(fileName).toString();
                    downloadFile(link, outputPath);
                } catch (Exception e) {
                    System.out.println("Failed to download: " + link);
                }
            }));
        }
        for (Future<?> future : futures) {
            future.get();
        }
        executor.shutdown();
    }

    private static void generateCompletionReport(String domain, long startTime, long endTime) throws SQLException{
        String updateWebsiteQuery = "UPDATE websites SET end_time = ?, elapsed_time = ?, total_kb = ? WHERE name = ?";
        PreparedStatement stmt = conn.prepareStatement(updateWebsiteQuery);
        stmt.setString(1, LocalDateTime.now().toString());
        stmt.setInt(2, (int) (endTime - startTime));
        stmt.setInt(3, calculateTotalDownloadedSize(domain));
        stmt.setString(4, domain);
        stmt.executeUpdate();

        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM websites WHERE name = '" + domain + "'");
        while (rs.next()) {
            System.out.println("Website: " + rs.getString("name"));
            System.out.println("Start Time: " + rs.getString("start_time"));
            System.out.println("End Time: " + rs.getString("end_time"));
            System.out.println("Elapsed Time: " + rs.getInt("elapsed_time") + " ms");
            System.out.println("Total Size: " + rs.getInt("total_kb") + " KB");
        }
    }

    private static int calculateTotalDownloadedSize(String domain) {
        String path = "/home/landry/Documents/java/multiple/" + domain;
        File directory = new File(path);


        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Directory does not exist or is not a directory.");
            return 0;
        }

        return calculateDirectorySize(directory);
    }

    private static int calculateDirectorySize(File directory) {
        int totalSize = 0;
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {

                    totalSize += calculateDirectorySize(file);
                } else {
                    // Add size of regular file (in kilobytes)
                    totalSize += file.length() / 1024; // Convert bytes to KB
                }
            }
        }
        return totalSize;
    }
}

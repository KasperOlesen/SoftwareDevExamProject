package Migration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import java.sql.*;

public class CitiesMigrate {
    private final String url;
    private final String username;
    private final String password;

    public CitiesMigrate(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void performMigration(String filePath) throws IOException {
        // Read citites
        final String dir = System.getProperty("user.dir");
        final String path = dir + filePath;

        performMigration(new FileInputStream(path));
    }

    public void performMigration(InputStream stream) throws IOException {
        String[] strCommands = createMigration(new InputStreamReader(stream, "UTF8")).split("\n");
        Collection<String> commands = Arrays.asList(strCommands);
        
        System.out.println("Cities loaded");

        // Loop over the commands in paralllel
        commands.parallelStream().forEach(command -> {
            // Fire the command against the DB

            try (Connection con = DriverManager.getConnection(this.url, this.username, this.password)) {
                try (Statement st = con.createStatement()) {
                    st.execute(command);
                }
            } catch (SQLException ex) {
                System.out.println("Could not fire \"" + command + "\" - " + ex.getMessage());
            }
        });

        System.out.println("Cities created");
    }

    public String createMigration(InputStreamReader readerStream) throws IOException {
        StringBuilder strBuilder = new StringBuilder();

        List<String> sqlParts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(readerStream)) {
            String line = reader.readLine();

            while (line != null) {
                String[] parts = line.split(",");

                String id = parts[0];
                String name = parts[1];
                double latitude = Double.parseDouble(parts[2]);
                double longitude = Double.parseDouble(parts[3]);

                sqlParts.add(createSqlString(id, name, latitude, longitude));

                line = reader.readLine();
            }
        }

        strBuilder.append("INSERT INTO cities (id, name, location) VALUES " + String.join(", ", sqlParts));

        return strBuilder.toString();
    }

    public String createSqlString(String id, String name, double latitude, double longitude) {
        return "(" + id + ", '" + name.replace("'", "\\'")
                + "', GeomFromText(CONCAT('POINT (', " + longitude + ", ' ', " + latitude + ", ')')))";
    }
}
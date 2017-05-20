package Migration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CitiesMigrate {
    private final String tableName;

    public CitiesMigrate(String tableName) {
        this.tableName = tableName;
    }

    public void performMigration() throws IOException {
        // Read citites
        final String dir = System.getProperty("user.dir");
        final String path = dir + "/data/citites.csv";

        try (FileInputStream stream = new FileInputStream(path)) {
            String commands = createMigration(new InputStreamReader(stream));

            // Fire away the commands
        }

        // System.out.println(dir);
    }

    public String createMigration(InputStreamReader readerStream) throws IOException {
        StringBuilder strBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(readerStream)) {
            String line = reader.readLine();

            while (line != null) {
                String[] parts = line.split(",");

                String id = parts[0];
                String name = parts[1];
                double latitude = Double.parseDouble(parts[2]);
                double longitude = Double.parseDouble(parts[3]);

                strBuilder.append(createSqlString(id, name, latitude, longitude) + "\n");

                line = reader.readLine();
            }
        }

        return strBuilder.toString();
    }

    public String createSqlString(String id, String name, double latitude, double longitude) {
        return "INSERT INTO " + this.tableName + " (id, name, latitude, longitude) VALUES (" + id + ", '" + name + "', "
                + latitude + ", " + longitude + ");";
    }
}
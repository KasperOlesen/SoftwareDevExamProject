package Migration;

import java.io.*;
import java.util.*;

import org.neo4j.driver.v1.*;

public class NeoCitiesMigrate {
    private final String url;
    private final AuthToken token;

    public NeoCitiesMigrate(String url, AuthToken token) {
        this.url = url;
        this.token = token;
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

        // Loop over the commands in paralllel

        try (PrintWriter writer = new PrintWriter(System.getProperty("user.dir") + "/neo/cities.csv", "UTF8")) {
            writer.println("id:ID,name,latitude:float,longitude:float,:LABEL");

            commands.stream().forEach(command -> {
                writer.println(command);
            });
        }

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

        strBuilder.append(String.join("\n", sqlParts));

        return strBuilder.toString();
    }

    public String createSqlString(String id, String name, double latitude, double longitude) {
        return "" + id.trim() + ",\"" + name + "\"," + latitude + "," + longitude + ",City";
    }
}
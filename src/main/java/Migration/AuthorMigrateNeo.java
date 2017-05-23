/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Migration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import org.neo4j.driver.v1.*;

/**
 *
 * @author Kasper
 */
public class AuthorMigrateNeo {
    private final String url;
    private final AuthToken token;

    public AuthorMigrateNeo(String url, AuthToken token) {
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
            Driver driver = GraphDatabase.driver(this.url, this.token);
            // Loop over the commands in paralllel
            commands.parallelStream().forEach(command -> {
                // Fire the command against the DB

                try (Session session = driver.session()) {
                    session.run(command);
                }

            });
    }

    public String createMigration(InputStreamReader readerStream) throws IOException {
        StringBuilder strBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(readerStream)) {
            String line = reader.readLine();
            
            while (line != null) {
                String[] parts = line.split("#");

                String id = parts[0];
                String name = parts[1].replace("'", "`");

                strBuilder.append(createCypherString(id, name) + "\n");

                line = reader.readLine();
            }
        }

        return strBuilder.toString();
    }

    public String createCypherString(String id, String name) {
        return "CREATE (a:Author {id: '" + id + "', name: '" + name + "'})";
    }
}

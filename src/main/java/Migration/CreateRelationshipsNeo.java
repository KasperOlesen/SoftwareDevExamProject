/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Migration;

/**
 *
 * @author Kasper
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import org.neo4j.driver.v1.*;

public class CreateRelationshipsNeo {
    private final String url;
    private final AuthToken token;

    public CreateRelationshipsNeo(String url, AuthToken token) {
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
                StatementResult result = session.run(command);
            }

        });
    }

    public String createMigration(InputStreamReader readerStream) throws IOException {
        StringBuilder strBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(readerStream)) {
            String line = reader.readLine();

            while (line != null) {
                String[] parts = line.split("#");

                String bookid = parts[0];
                String cityid = parts[1];

                strBuilder.append(createCypherString(bookid, cityid) + "\n");

                line = reader.readLine();
            }
        }

        return strBuilder.toString();
    }

    public String createCypherString(String bookid, String cityid) {
        return "MATCH (b:Book {id: '" + bookid + "'}), (c:City {id: '" + cityid + "'}) CREATE (b)-[r:MENTIONS]->(c)";
    }
}

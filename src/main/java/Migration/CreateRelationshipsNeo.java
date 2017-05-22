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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class CreateRelationshipsNeo {
    
public void performMigration() throws IOException {
        // Read citites
        final String dir = System.getProperty("user.dir");
        final String path = dir + "/data/book_city.txt";

        try (FileInputStream stream = new FileInputStream(path)) {
            String[] strCommands = createMigration(new InputStreamReader(stream)).split("\n");
            Collection<String> commands = Arrays.asList(strCommands);
            Driver driver = GraphDatabase.driver(
                    "bolt://localhost:7687",
                    AuthTokens.basic("neo4j", "class"));
            // Loop over the commands in paralllel
            commands.parallelStream().forEach(command -> {
                // Fire the command against the DB

                try (Session session = driver.session()) {
                    StatementResult result = session.run(command);

                    System.out.println("Executed!");
                }

            });
        }

        // System.out.println(dir);
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

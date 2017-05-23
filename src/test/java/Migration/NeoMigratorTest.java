package Migration;

import java.sql.*;
import java.io.*;

import org.junit.Test;

import java.util.UUID;

import TxtFormat.*;

import org.neo4j.driver.v1.*;

public class NeoMigratorTest {

    @Test
    public void performMigration_shouldInsertAllData() throws Exception, IOException {
        // String[] commands = new String[] { "DELETE FROM book_author WHERE 1 = 1;", "DELETE FROM book_city WHERE 1 = 1;",
        //         "DELETE FROM books WHERE 1 = 1;", "DELETE FROM authors WHERE 1 = 1;",
        //         "DELETE FROM cities WHERE 1 = 1;", };

        // try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testprojekt5", "root",
        //         "123123qwe")) {
        //     for (String command : commands) {
        //         try (Statement st = con.createStatement()) {
        //             st.execute(command);
        //         }
        //     }
        // }

        FileIO fio = new FileIO();

        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        fio.createFormattedTxtAuthor(fio.read("data/test-data/books.csv"),
                new OutputStreamWriter(outputStream1, "UTF8"));
        InputStream inputStream1 = new ByteArrayInputStream(outputStream1.toByteArray());

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        fio.createFormattedTxtBook(fio.read("data/test-data/books.csv"),
                new BufferedReader(new InputStreamReader(inputStream1, "UTF8")),
                new OutputStreamWriter(outputStream2, "UTF8"), new BookIdentifierProviderStub());
        InputStream inputStream2 = new ByteArrayInputStream(outputStream2.toByteArray());

        ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        fio.createFormattedTxtBookCity(fio.read("data/test-data/books.csv"), fio.read("data/test-data/cities.csv"),
                new BufferedReader(new InputStreamReader(inputStream2, "UTF8")),
                new OutputStreamWriter(outputStream3, "UTF8"));
        InputStream inputStream3 = new ByteArrayInputStream(outputStream3.toByteArray());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream3, "UTF8"));
        String line = reader.readLine();
        ;
        while (line != null) {
            System.out.println(line);

            line = reader.readLine();
        }

        // CitiesMigrateNeo cityMigrator = new CitiesMigrateNeo("bolt://localhost:7687",
        //         AuthTokens.basic("neo4j", "class"));

        // // /data/cities.csv
        // cityMigrator.performMigration("/data/test-data/cities.csv");

        // BookMigrateNeo bookMigrator = new BookMigrateNeo("bolt://localhost:7687", AuthTokens.basic("neo4j", "class"),
        //         new IBookIdentifierProvider() {
        //             public UUID getNextIdentifier() {
        //                 return UUID.randomUUID();
        //             }
        //         });

        // // "/data/allformatted.txt"
        // bookMigrator.performMigration("/data/test-data/books.csv");
    }
}
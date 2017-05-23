package Migration;

import java.sql.*;
import java.io.IOException;

import org.junit.Test;

import java.util.UUID;

public class SqlMigratorTest {

    @Test
    public void performMigration_shouldInsertAllData() throws Exception, IOException {
        String[] commands = new String[] { "DELETE FROM book_author WHERE 1 = 1;", "DELETE FROM book_city WHERE 1 = 1;",
                "DELETE FROM books WHERE 1 = 1;", "DELETE FROM authors WHERE 1 = 1;",
                "DELETE FROM cities WHERE 1 = 1;", };

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testprojekt5", "root",
                "123123qwe")) {
            for (String command : commands) {
                try (Statement st = con.createStatement()) {
                    st.execute(command);
                }
            }
        }

        CitiesMigrate cityMigrator = new CitiesMigrate("jdbc:mysql://localhost:3306/testprojekt5", "root", "123123qwe");

        // /data/cities.csv
        cityMigrator.performMigration("/data/test-data/cities.csv");

        BookMigrate bookMigrator = new BookMigrate("jdbc:mysql://localhost:3306/testprojekt5", "root", "123123qwe",
                new IBookIdentifierProvider() {
                    public UUID getNextIdentifier() {
                        return UUID.randomUUID();
                    }
                });

        // "/data/allformatted.txt"
        bookMigrator.performMigration("/data/test-data/books.csv");
    }
}
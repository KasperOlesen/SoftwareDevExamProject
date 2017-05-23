package persistence;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.ws.rs.core.Response;

import softDevExam.controller.GutenbergController;
import softDevExam.persistence.GutenbergMysql;

import org.junit.Test;
import org.junit.Before;

import java.util.*;

import java.sql.*;

import softDevExam.entity.*;

import Migration.BookMigrate;
import Migration.CitiesMigrate;

import Migration.BookIdentifierProviderStub;

public class GutenbergMysqlTest {
    @Test
    public void givenACityNameYourApplicationReturnsAllBookTitlesWithCorrespondingAuthorsThatMentionThisCity()
            throws Exception {
        insertCities(new String[] { "4281730,Wichita,37.69224,-97.33754", "4282757,Ashland,38.47841,-82.63794",
                "4285268,Bowling Green,36.99032,-86.4436", "4286281,Burlington,39.02756,-84.72411",
                "4288809,Covington,39.08367,-84.50855", "4289445,Danville,37.64563,-84.77217",
                "4290988,Elizabethtown,37.69395,-85.85913", "4291255,Erlanger,39.01673,-84.60078",
                "4291620,Fern Creek,38.15979,-85.58774" });

        insertBooks(new String[] { "Ringenes Herre#J Tokien#Wichita,Covington",
                "Harry Potter#Mogens Sørsen#Danville,Erlanger,Gern Creek",
                "En Dag I Solen#Mads Jensen#Danville,Covington", "En Dag I Regnen#Mads Jensen#Erlanger,Ashland",
                "En Dag I Snen#Mads Jensen#Bowling Green", "Sidste Dag I Orkanen#Mads Jensen#Burlington,Erlanger",
                "Glemt Skoleopgave#Minni Musen#Burlington", });

        GutenbergMysql service = createService();

        List<Book> books = service.getBooksByCity("Covington");

        assertThat(books.size(), equalTo(2));
    }

    private static GutenbergMysql createService() {
        return new GutenbergMysql("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/testprojekt5", "root", "123123qwe");
    }

    private static void insertCities(String[] cities) throws Exception {
        CitiesMigrate migrator = new CitiesMigrate("jdbc:mysql://localhost:3306/testprojekt5", "root", "123123qwe");

        InputStream stream = new ByteArrayInputStream(String.join("\n", cities).getBytes());
        migrator.performMigration(stream);
    }

    private static void insertBooks(String[] books) throws Exception {
        BookMigrate migrator = new BookMigrate("jdbc:mysql://localhost:3306/testprojekt5", "root", "123123qwe",
                new BookIdentifierProviderStub());

        InputStream stream = new ByteArrayInputStream(String.join("\n", books).getBytes());
        migrator.performMigration(stream);
    }

    @Before
    public void cleanDatabase() throws Exception {
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
    }
}

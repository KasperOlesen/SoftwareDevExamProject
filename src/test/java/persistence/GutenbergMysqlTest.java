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
        GutenbergMysql service = createService();

        List<Book> books = service.getBooksByCity("Covington");

        assertThat(books.size(), equalTo(2));
    }

    @Test
    public void givenAGeolocationYourApplicationListsAllBooksMentioningACityInVicinityOfTheGivenGeolocation()
            throws Exception {
        GutenbergMysql service = createService();

        List<Book> books = service.getBooksByLocation(-84.77217, 37.64563);

        assertThat(books.size(), equalTo(2));
    }

    private static GutenbergMysql createService() {
        return new GutenbergMysql("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/testprojekt5", "root",
                "123123qwe");
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

        CitiesMigrate cityMigrator = new CitiesMigrate("jdbc:mysql://localhost:3306/testprojekt5", "root", "123123qwe");
        cityMigrator.performMigration("/data/test-data/cities.csv");

        BookMigrate bookMigrator = new BookMigrate("jdbc:mysql://localhost:3306/testprojekt5", "root", "123123qwe",
                new BookIdentifierProviderStub());
        bookMigrator.performMigration("/data/test-data/books.csv");
    }
}

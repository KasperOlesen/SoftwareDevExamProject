package persistence;

import java.io.ByteArrayInputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import softDevExam.persistence.GutenbergNeo4J;

import org.junit.*;

import java.io.*;

import java.util.*;

import softDevExam.entity.*;

import Migration.BookIdentifierProviderStub;

import org.neo4j.driver.v1.*;

import TxtFormat.*;

import Migration.*;

public class GutenbergNeoTest {
        @Test
        @Ignore
        public void givenACityNameYourApplicationReturnsAllBookTitlesWithCorrespondingAuthorsThatMentionThisCity()
                        throws Exception {
                GutenbergNeo4J service = createService();

                List<Book> books = service.getBooksByCity("Covington");

                assertThat(books.size(), equalTo(2));

                // assertThat(books.get(0).getName(), equalTo("Ringenes Herre"));
                // assertThat(books.get(1).getName(), equalTo("En Dag I Solen"));
        }

        @Test
        @Ignore
        public void givenAGeolocationYourApplicationListsAllBooksMentioningACityInVicinityOfTheGivenGeolocation()
                        throws Exception {
                GutenbergNeo4J service = createService();

                List<Book> books = service.getBooksByLocation(-84.77217, 37.64563);

                assertThat(books.size(), equalTo(2));
        }

        private static GutenbergNeo4J createService() {
                return new GutenbergNeo4J("bolt://localhost:7687", AuthTokens.basic("neo4j", "123123qwe"));
        }

        @Before
        public void performMigration_shouldInsertAllData() throws Exception, IOException {
                // DELETE ALL DATA 
                org.neo4j.driver.v1.Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                                AuthTokens.basic("neo4j", "123123qwe"));

                try (Session session = driver.session()) {
                        session.run("MATCH (n) DETACH DELETE n");
                }

                FileIO fio = new FileIO();

                ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
                fio.createFormattedTxtAuthor(fio.read("data/test-data/books.csv"),
                                new OutputStreamWriter(outputStream1, "UTF8"));
                byte[] content1 = outputStream1.toByteArray();

                ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
                fio.createFormattedTxtBook(fio.read("data/test-data/books.csv"),
                                new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content1), "UTF8")),
                                new OutputStreamWriter(outputStream2, "UTF8"), new BookIdentifierProviderStub());
                byte[] content2 = outputStream2.toByteArray();

                ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
                fio.createFormattedTxtBookCity(fio.read("data/test-data/books.csv"),
                                fio.read("data/test-data/cities.csv"),
                                new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content2), "UTF8")),
                                new OutputStreamWriter(outputStream3, "UTF8"));
                byte[] content3 = outputStream3.toByteArray();

                CitiesMigrateNeo cityMigrator = new CitiesMigrateNeo("bolt://localhost:7687",
                                AuthTokens.basic("neo4j", "123123qwe"));

                // /data/cities.csv
                cityMigrator.performMigration("/data/test-data/cities.csv");

                AuthorMigrateNeo amn = new AuthorMigrateNeo("bolt://localhost:7687",
                                AuthTokens.basic("neo4j", "123123qwe"));
                amn.performMigration(new ByteArrayInputStream(content1));

                BooksMigrateNeo bmn = new BooksMigrateNeo("bolt://localhost:7687",
                                AuthTokens.basic("neo4j", "123123qwe"));
                bmn.performMigration(new ByteArrayInputStream(content2));

                CreateRelationshipsNeo crn = new CreateRelationshipsNeo("bolt://localhost:7687",
                                AuthTokens.basic("neo4j", "123123qwe"));
                crn.performMigration(new ByteArrayInputStream(content3));
        }
}

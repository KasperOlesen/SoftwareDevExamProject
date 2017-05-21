package Migration;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class BookMigrateTest {
    @Test
    public void givenAStream_shouldCreateMultipleCommands() throws Exception, IOException {
        String dataset = "Hell#Dante Alighieri#Lombard,Rome\nA Little Pilgrim#Margaret O. (Wilson) Oliphant#Mary,Young";

        BookMigrate migrator = new BookMigrate("Cities", new BookIdentifierProviderStub());

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String[] commands = migrator.createMigration(new InputStreamReader(bais));

        String authorsSql = "INSERT INTO authors (name) VALUES ('Margaret O. (Wilson) Oliphant');\n\nINSERT INTO authors (name) VALUES ('Dante Alighieri');\n\n";

        String book1Sql = "INSERT INTO books (id, name) VALUES ('1cf23a40-4c3c-444c-9014-04eee2211f1a', 'Hell');\n";
        book1Sql += "INSERT INTO book_author (bookId, authorId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', authors.id FROM authors WHERE authors.name = 'Dante Alighieri';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', cities.id FROM cities WHERE cities.name = 'Lombard';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', cities.id FROM cities WHERE cities.name = 'Rome';\n\n";

        String book2Sql = "INSERT INTO books (id, name) VALUES ('792f42e0-7ae4-44dd-a197-eeb4a66f8c42', 'A Little Pilgrim');\n";
        book2Sql += "INSERT INTO book_author (bookId, authorId) SELECT '792f42e0-7ae4-44dd-a197-eeb4a66f8c42', authors.id FROM authors WHERE authors.name = 'Margaret O. (Wilson) Oliphant';\n";
        book2Sql += "INSERT INTO book_city (bookId, cityId) SELECT '792f42e0-7ae4-44dd-a197-eeb4a66f8c42', cities.id FROM cities WHERE cities.name = 'Mary';\n";
        book2Sql += "INSERT INTO book_city (bookId, cityId) SELECT '792f42e0-7ae4-44dd-a197-eeb4a66f8c42', cities.id FROM cities WHERE cities.name = 'Young';\n\n";

        String expected = book1Sql + book2Sql;

        assertThat(commands[0], equalTo(authorsSql));
        assertThat(commands[1], equalTo(expected));
    }

    @Test
    public void shouldBeAbleToCreateCommandsWhereAuthorsIncludeQute() throws Exception, IOException {
        String dataset = "Hell#Dante A'lighieri#Lombard\n";

        BookMigrate migrator = new BookMigrate("Cities", new BookIdentifierProviderStub());

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String[] commands = migrator.createMigration(new InputStreamReader(bais));

        String authorsSql = "INSERT INTO authors (name) VALUES ('Dante A\\'lighieri');\n\n";

        String book1Sql = "INSERT INTO books (id, name) VALUES ('1cf23a40-4c3c-444c-9014-04eee2211f1a', 'Hell');\n";
        book1Sql += "INSERT INTO book_author (bookId, authorId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', authors.id FROM authors WHERE authors.name = 'Dante A\\'lighieri';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', cities.id FROM cities WHERE cities.name = 'Lombard';\n\n";

        assertThat(commands[0], equalTo(authorsSql));
        assertThat(commands[1], equalTo(book1Sql));
    }

    @Test
    public void shouldBeAbleToCreateCommandsWhereBookNameIncludeQute() throws Exception, IOException {
        String dataset = "He'll#Dante Alighieri#Lombard\n";

        BookMigrate migrator = new BookMigrate("Cities", new BookIdentifierProviderStub());

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String[] commands = migrator.createMigration(new InputStreamReader(bais));

        String authorsSql = "INSERT INTO authors (name) VALUES ('Dante Alighieri');\n\n";

        String book1Sql = "INSERT INTO books (id, name) VALUES ('1cf23a40-4c3c-444c-9014-04eee2211f1a', 'He\\'ll');\n";
        book1Sql += "INSERT INTO book_author (bookId, authorId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', authors.id FROM authors WHERE authors.name = 'Dante Alighieri';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', cities.id FROM cities WHERE cities.name = 'Lombard';\n\n";

        assertThat(commands[0], equalTo(authorsSql));
        assertThat(commands[1], equalTo(book1Sql));
    }

    @Test
    public void shouldBeAbleToCreateCommandsWhereCityNameIncludeQute() throws Exception, IOException {
        String dataset = "Hell#Dante Alighieri#Lom'bard\n";

        BookMigrate migrator = new BookMigrate("Cities", new BookIdentifierProviderStub());

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String[] commands = migrator.createMigration(new InputStreamReader(bais));

        String authorsSql = "INSERT INTO authors (name) VALUES ('Dante Alighieri');\n\n";

        String book1Sql = "INSERT INTO books (id, name) VALUES ('1cf23a40-4c3c-444c-9014-04eee2211f1a', 'Hell');\n";
        book1Sql += "INSERT INTO book_author (bookId, authorId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', authors.id FROM authors WHERE authors.name = 'Dante Alighieri';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', cities.id FROM cities WHERE cities.name = 'Lom\\'bard';\n\n";

        assertThat(commands[0], equalTo(authorsSql));
        assertThat(commands[1], equalTo(book1Sql));
    }

    @Test
    public void performMigration_shouldInsertAllData() throws Exception, IOException {
        BookMigrate migrator = new BookMigrate("Cities", new IBookIdentifierProvider() {
            public UUID getNextIdentifier() {
                return UUID.randomUUID();
            }
        });

        migrator.performMigration();
    }

}
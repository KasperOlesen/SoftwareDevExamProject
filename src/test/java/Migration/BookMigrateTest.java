package Migration;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class BookMigrateTest {
    @Test
    public void givenAStream_shouldCreateMultipleCommands() throws IOException {
        String dataset = "Hell#Dante Alighieri#Lombard,Rome\nA Little Pilgrim#Margaret O. (Wilson) Oliphant#Mary,Young";

        BookMigrate migrator = new BookMigrate("Cities", new BookIdentifierProviderStub());

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String commands = migrator.createMigration(new InputStreamReader(bais));

        String authorsSql = "INSERT INTO Author (name) VALUES ('Margaret O. (Wilson) Oliphant');\nINSERT INTO Author (name) VALUES ('Dante Alighieri');\n";

        String book1Sql = "INSERT INTO Books (id, name) VALUES ('1cf23a40-4c3c-444c-9014-04eee2211f1a', 'Hell');\n";
        book1Sql += "INSERT INTO book_author (bookId, authorId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', author.id FROM Authors WHERE Name = 'Dante Alighieri';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', city.id FROM Cities WHERE Name = 'Lombard';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', city.id FROM Cities WHERE Name = 'Rome';\n";

        String book2Sql = "INSERT INTO Books (id, name) VALUES ('792f42e0-7ae4-44dd-a197-eeb4a66f8c42', 'A Little Pilgrim');\n";
        book2Sql += "INSERT INTO book_author (bookId, authorId) SELECT '792f42e0-7ae4-44dd-a197-eeb4a66f8c42', author.id FROM Authors WHERE Name = 'Margaret O. (Wilson) Oliphant';\n";
        book2Sql += "INSERT INTO book_city (bookId, cityId) SELECT '792f42e0-7ae4-44dd-a197-eeb4a66f8c42', city.id FROM Cities WHERE Name = 'Mary';\n";
        book2Sql += "INSERT INTO book_city (bookId, cityId) SELECT '792f42e0-7ae4-44dd-a197-eeb4a66f8c42', city.id FROM Cities WHERE Name = 'Young';\n";

        String expected = authorsSql + book1Sql + book2Sql;

        assertThat(commands, equalTo(expected));
    }  
    
    @Test
    public void shouldBeAbleToCreateCommandsWhereAuthorsIncludeQute() throws IOException {
        String dataset = "Hell#Dante A'lighieri#Lombard\n";

        BookMigrate migrator = new BookMigrate("Cities", new BookIdentifierProviderStub());

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String commands = migrator.createMigration(new InputStreamReader(bais));

        String authorsSql = "INSERT INTO Author (name) VALUES ('Dante A\'lighieri');\n";

        String book1Sql = "INSERT INTO Books (id, name) VALUES ('1cf23a40-4c3c-444c-9014-04eee2211f1a', 'Hell');\n";
        book1Sql += "INSERT INTO book_author (bookId, authorId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', author.id FROM Authors WHERE Name = 'Dante A\'lighieri';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', city.id FROM Cities WHERE Name = 'Lombard';\n";

        String expected = authorsSql + book1Sql;

        assertThat(commands, equalTo(expected));
    }
    
    @Test
    public void shouldBeAbleToCreateCommandsWhereBookNameIncludeQute() throws IOException {
        String dataset = "He'll#Dante Alighieri#Lombard\n";

        BookMigrate migrator = new BookMigrate("Cities", new BookIdentifierProviderStub());

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String commands = migrator.createMigration(new InputStreamReader(bais));

        String authorsSql = "INSERT INTO Author (name) VALUES ('Dante Alighieri');\n";

        String book1Sql = "INSERT INTO Books (id, name) VALUES ('1cf23a40-4c3c-444c-9014-04eee2211f1a', 'He'll');\n";
        book1Sql += "INSERT INTO book_author (bookId, authorId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', author.id FROM Authors WHERE Name = 'Dante Alighieri';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', city.id FROM Cities WHERE Name = 'Lombard';\n";

        String expected = authorsSql + book1Sql;

        assertThat(commands, equalTo(expected));
    }
    
    @Test
    public void shouldBeAbleToCreateCommandsWhereCityNameIncludeQute() throws IOException {
        String dataset = "Hell#Dante Alighieri#Lom'bard\n";

        BookMigrate migrator = new BookMigrate("Cities", new BookIdentifierProviderStub());

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String commands = migrator.createMigration(new InputStreamReader(bais));

        String authorsSql = "INSERT INTO Author (name) VALUES ('Dante Alighieri');\n";

        String book1Sql = "INSERT INTO Books (id, name) VALUES ('1cf23a40-4c3c-444c-9014-04eee2211f1a', 'Hell');\n";
        book1Sql += "INSERT INTO book_author (bookId, authorId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', author.id FROM Authors WHERE Name = 'Dante Alighieri';\n";
        book1Sql += "INSERT INTO book_city (bookId, cityId) SELECT '1cf23a40-4c3c-444c-9014-04eee2211f1a', city.id FROM Cities WHERE Name = 'Lom'bard';\n";

        String expected = authorsSql + book1Sql;

        assertThat(commands, equalTo(expected));
    }
}
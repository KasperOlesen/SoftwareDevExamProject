package Migration;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.junit.Ignore;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class CitiesMigrateTest {
    @Test
    public void shouldCreateExpectSQLCommand() throws IOException {
        CitiesMigrate migrator = new CitiesMigrate("Cities");

        String command = migrator.createSqlString("1", "Test", 1.2, 2.1);

        assertThat(command,
                equalTo("INSERT INTO Cities (id, name, latitude, longitude) VALUES (1, 'Test', 1.2, 2.1);"));
    }

    @Test
    public void shouldHandleQuoutes() throws IOException {
        CitiesMigrate migrator = new CitiesMigrate("Cities");

        String command = migrator.createSqlString("1", "Test2'2123", 1.2, 2.1);

        assertThat(command,
                equalTo("INSERT INTO Cities (id, name, latitude, longitude) VALUES (1, 'Test2\\'2123', 1.2, 2.1);"));
    }

    @Test
    @Ignore
    public void performMigration_shouldInsertAllCities() throws IOException {
        CitiesMigrate migrator = new CitiesMigrate("Cities");

        migrator.performMigration();
    }

    @Test
    public void givenAStream_shouldCreateMultipleCommands() throws IOException {
        String dataset = "1132495,Nahrin,36.0649,69.13343\n1133453,Maymana,35.92139,64.78361";

        CitiesMigrate migrator = new CitiesMigrate("Cities");

        byte[] bytes = dataset.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        String commands = migrator.createMigration(new InputStreamReader(bais));

        assertThat(commands, equalTo(
                "INSERT INTO Cities (id, name, latitude, longitude) VALUES (1132495, 'Nahrin', 36.0649, 69.13343);\nINSERT INTO Cities (id, name, latitude, longitude) VALUES (1133453, 'Maymana', 35.92139, 64.78361);\n"));
    }
}
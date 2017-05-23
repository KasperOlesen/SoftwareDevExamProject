package Migration;

import java.sql.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ CitiesMigrateTests.class, BookMigrateTests.class })
@RunWith(Suite.class)
public class MigrationSuiteTest {
}
package broccoli.common;

import java.sql.Connection;
import java.sql.DriverManager;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * The {@link LiquibaseMigrator} class.
 */
public final class LiquibaseMigrator {

  private LiquibaseMigrator() {
    // Private constructor for utility class
  }


  /**
   * Private constructor for utility class.
   */
  public static void migrate(String filename) {

    final var properties = PropertyLoader.loadProperties("application.properties");
    properties.putAll(PropertyLoader.loadProperties(filename));

    final var databaseUrl = properties.getProperty("datasources.default.url");
    final var username = properties.getProperty("datasources.default.username");
    final var password = properties.getProperty("datasources.default.password");
    final var changelogFile = properties.getProperty("liquibase.datasources.default.change-log")
        .replaceFirst("classpath:", "");

    Connection conn = null;
    try {
      // Open a connection to your database
      conn = DriverManager.getConnection(databaseUrl, username, password);

      // Create a Liquibase Database instance
      DatabaseConnection databaseConnection = new JdbcConnection(conn);
      Database database =
          DatabaseFactory.getInstance().findCorrectDatabaseImplementation(databaseConnection);

      // Initialize Liquibase and run the migrations
      Liquibase liquibase =
          new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);
      // You can specify contexts if you have them
      liquibase.update(new Contexts());

    } catch (Exception e) {
      // Handle exceptions (e.g., log them, rethrow, etc.)
      e.printStackTrace();
    } finally {
      // Ensure the database connection is closed
      if (conn != null) {
        try {
          conn.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}

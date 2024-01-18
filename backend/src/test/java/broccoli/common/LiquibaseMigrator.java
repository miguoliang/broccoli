package broccoli.common;

import liquibase.command.CommandScope;

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

    try {
      // Initialize Liquibase and run the migrations
      // @since 4.21.1
      new CommandScope("update")
          .addArgumentValue("url", databaseUrl)
          .addArgumentValue("username", username)
          .addArgumentValue("password", password)
          .addArgumentValue("changeLogFile", changelogFile)
          .execute();
    } catch (Exception e) {
      // Handle exceptions (e.g., log them, rethrow, etc.)
      e.printStackTrace();
    }
  }
}

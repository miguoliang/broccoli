package broccoli.common;

/**
 * The {@link BaseDatabaseTest} class.
 */
public abstract class BaseDatabaseTest {

  static {
    LiquibaseMigrator.migrate("application-test.properties");
  }
}

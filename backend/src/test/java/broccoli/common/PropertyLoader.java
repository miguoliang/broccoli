package broccoli.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The {@link PropertyLoader} class.
 */
public final class PropertyLoader {

  private PropertyLoader() {
    // Private constructor for utility class
  }

  /**
   * Load properties from a file.
   *
   * @param filename the filename
   * @return the properties
   */
  public static Properties loadProperties(String filename) {
    Properties properties = new Properties();
    try (
        InputStream input = PropertyLoader.class.getClassLoader().getResourceAsStream(filename)) {
      if (input == null) {
        System.out.println("Sorry, unable to find " + filename);
        return properties;
      }

      // load a properties file from class path
      properties.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return properties;
  }
}

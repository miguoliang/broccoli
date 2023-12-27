package broccoli;

import io.micronaut.runtime.Micronaut;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;

/**
 * The {@link Application} class.
 */
public class Application {

  public static void main(String[] args) throws NoSuchAlgorithmException {
    SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
    Micronaut.run(Application.class, args);
  }
}

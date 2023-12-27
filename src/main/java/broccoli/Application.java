package broccoli;

import io.micronaut.runtime.Micronaut;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

public class Application {

  public static void main(String[] args) throws NoSuchAlgorithmException {
    SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
    Micronaut.run(Application.class, args);
  }
}

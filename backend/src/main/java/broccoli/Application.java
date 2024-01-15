package broccoli;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;

/**
 * The {@link Application} class.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Broccoli",
        version = "1.0",
        description = "Ease is a greater threat to progress than hardship.",
        license = @License(name = "Apache 2.0", url = " https://www.apache.org/licenses/LICENSE-2.0"),
        contact = @Contact(url = "https://miguoliang.com", name = "Mi GuoLiang", email = "boymgl@qq.com")
    )
)
public class Application {

  public static void main(String[] args) throws NoSuchAlgorithmException {
    SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
    Micronaut.run(Application.class, args);
  }
}

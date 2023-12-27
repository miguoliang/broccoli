package broccoli.security.keycloak;

import io.micronaut.http.MediaType;
import org.keycloak.adapters.authorization.TokenPrincipal;
import org.keycloak.adapters.authorization.spi.HttpRequest;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

public class SpiHttpRequest implements HttpRequest {

  private final io.micronaut.http.HttpRequest<?> request;

  public SpiHttpRequest(io.micronaut.http.HttpRequest<?> request) {
    this.request = request;
  }

  @Override
  public String getRelativePath() {
    return request.getPath();
  }

  @Override
  public String getMethod() {
    return request.getMethod().toString();
  }

  @Override
  public String getURI() {
    return request.getUri().toString();
  }

  @Override
  public List<String> getHeaders(String name) {
    return request.getHeaders().getAll(name);
  }

  @Override
  public String getFirstParam(String name) {
    return request.getParameters().getFirst(name).orElse(null);
  }

  @Override
  public String getCookieValue(String name) {
    return request.getCookies().getValue(name).getValue();
  }

  @Override
  public String getRemoteAddr() {
    return request.getRemoteAddress().getHostString();
  }

  @Override
  public boolean isSecure() {
    return request.isSecure();
  }

  @Override
  public String getHeader(String name) {
    return request.getHeaders().get(name);
  }

  @Override
  public InputStream getInputStream(boolean buffered) {
    if (request.getBody().isPresent() && request.getContentType().map(MediaType::isTextBased).orElse(false)) {
      // Get the InputStream from the request body
      InputStream inputStream = request.getBody(InputStream.class).orElse(null);
      if (inputStream != null && buffered && (!(inputStream instanceof BufferedInputStream))) {
        return new BufferedInputStream(inputStream);
      }
      return inputStream;
    }
    return null;
  }

  @Override
  public TokenPrincipal getPrincipal() {
    return () -> {
      final var token = request.getHeaders().getAuthorization().orElse("");
      return token.replaceFirst("^[Bb]earer\\s+", "");
    };
  }
}

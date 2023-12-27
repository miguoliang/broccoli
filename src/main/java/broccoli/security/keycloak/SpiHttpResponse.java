package broccoli.security.keycloak;

import org.keycloak.adapters.authorization.spi.HttpResponse;

public class SpiHttpResponse implements HttpResponse {

  @Override
  public void sendError(int statusCode) {
    // unused in reactive implementation
  }

  @Override
  public void sendError(int statusCode, String reason) {
    // unused in reactive implementation
  }

  @Override
  public void setHeader(String name, String value) {
    // unused in reactive implementation
  }
}

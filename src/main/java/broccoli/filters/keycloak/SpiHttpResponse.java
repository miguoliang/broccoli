package broccoli.filters.keycloak;

import io.micronaut.http.MutableHttpResponse;
import org.keycloak.adapters.authorization.spi.HttpResponse;

public class SpiHttpResponse implements HttpResponse {

    private final MutableHttpResponse<?> httpResponse;

    public SpiHttpResponse(MutableHttpResponse<?> httpResponse) {
        this.httpResponse = httpResponse;
    }

    @Override
    public void sendError(int statusCode) {
        httpResponse.status(statusCode);
    }

    @Override
    public void sendError(int statusCode, String reason) {
        httpResponse.status(statusCode, reason);
    }

    @Override
    public void setHeader(String name, String value) {
        httpResponse.header(name, value);
    }
}

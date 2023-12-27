package broccoli.common;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;

/**
 * The {@link HttpStatusExceptions} class.
 */
public class HttpStatusExceptions {

  private HttpStatusExceptions() {
  }

  public static HttpStatusException notFound() {
    return new HttpStatusException(HttpStatus.NOT_FOUND, "Resource not found");
  }

  public static HttpStatusException conflict() {
    return new HttpStatusException(HttpStatus.CONFLICT, "Resource already exists");
  }
}

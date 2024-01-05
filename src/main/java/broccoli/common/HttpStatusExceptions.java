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

  public static HttpStatusException raw(int code, String message) {
    return new HttpStatusException(HttpStatus.valueOf(code), message);
  }

  public static HttpStatusException raw(HttpStatus status, String message) {
    return new HttpStatusException(status, message);
  }
}

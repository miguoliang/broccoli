package broccoli.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

/**
 * The {@link EdgeController} class.
 */
@Controller("/edge")
public class EdgeController {

  @Get(produces = "text/plain")
  public String index() {
    return "Example Response";
  }
}

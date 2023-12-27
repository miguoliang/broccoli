package broccoli.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/edge")
public class EdgeController {

  @Get(produces = "text/plain")
  public String index() {
    return "Example Response";
  }
}

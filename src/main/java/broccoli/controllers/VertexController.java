package broccoli.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/vertex")
public class VertexController {

  @Get(produces = "text/plain")
  public String index() {
    return "Hello, Vertex";
  }

  @Get(value = "/anonymous", produces = "text/plain")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public String anonymous() {
    return "Hello, Anonymous";
  }
}

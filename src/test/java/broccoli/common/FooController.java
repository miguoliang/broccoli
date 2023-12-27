package broccoli.common;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

/**
 * The {@link FooController} class.
 */
@Controller("/foo")
public class FooController {

  @Get(value = "anonymous", produces = "text/plain")
  @Secured(SecurityRule.IS_ANONYMOUS)
  public String anonymous() {
    return "Hello, Anonymous";
  }
}

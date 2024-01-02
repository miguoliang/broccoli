package broccoli.common;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
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

  @Get(value = "protected", produces = "text/plain")
  public String protectedResource() {
    return "Hello, Protected";
  }

  @Get(value = "protected/premium", produces = "text/plain")
  public String protectedPremiumResource() {
    return "Hello, Protected Premium";
  }

  @Post(value = "protected", produces = "text/plain")
  @Status(HttpStatus.CREATED)
  public String protectedResourcePost() {
    return "Hello, Protected (Post)";
  }
}

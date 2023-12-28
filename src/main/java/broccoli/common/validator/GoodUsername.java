package broccoli.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The {@link GoodUsername} annotation.
 */
@Constraint(validatedBy = GoodUsernameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoodUsername {

  /**
   * The message.
   *
   * @return the message.
   */
  String message() default "Invalid username";

  /**
   * The groups.
   *
   * @return the groups.
   */
  Class<?>[] groups() default {};

  /**
   * The payload.
   *
   * @return the payload.
   */
  Class<? extends Payload>[] payload() default {};
}

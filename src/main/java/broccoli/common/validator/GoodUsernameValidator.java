package broccoli.common.validator;


import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;

/**
 * The {@link GoodUsernameValidator} class.
 *
 * <p>Checks if the username is good enough.</p>
 * <p>Good username requirements:</p>
 * <ul>
 * <li>Contains only alphanumeric characters and underscores</li>
 * <li>Is between 5 and 10 characters long</li>
 * </ul>
 */
public class GoodUsernameValidator implements ConstraintValidator<GoodUsername, String> {

  private static final String USERNAME_PATTERN = "^\\w{5,100}$";

  @Override
  public boolean isValid(@Nullable String value,
                         @NonNull AnnotationValue<GoodUsername> annotationMetadata,
                         @NonNull ConstraintValidatorContext context) {
    return value != null && value.matches(USERNAME_PATTERN);
  }
}

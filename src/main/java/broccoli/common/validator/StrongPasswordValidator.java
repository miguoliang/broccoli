package broccoli.common.validator;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;

/**
 * The {@link StrongPasswordValidator} class.
 *
 * <p>Checks if the password is strong enough.</p>
 * <p>Strong password requirements:</p>
 * <ul>
 * <li>At least 8 characters long</li>
 * <li>Contains at least one digit</li>
 * <li>Contains at least one lowercase letter</li>
 * <li>Contains at least one uppercase letter</li>
 * <li>Contains at least one special character from the set [@#$%^&+=!]</li>
 * <li>Does not contain whitespaces</li>
 * </ul>
 */
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

  private static final String PATTERN =
      "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

  @Override
  public boolean isValid(@Nullable String value,
                         @NonNull AnnotationValue<StrongPassword> annotationMetadata,
                         @NonNull ConstraintValidatorContext context) {
    return value != null && value.matches(PATTERN);
  }
}
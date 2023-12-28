package broccoli.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The {@link GoodUsernameValidator} class.
 *
 * <p>Checks if the username is good enough.</p>
 * <p>Good username requirements:</p>
 * <ul>
 * <li>Contains only alphanumeric characters and underscores</li>
 * </ul>
 */
public class GoodUsernameValidator implements ConstraintValidator<GoodUsername, String> {

  private static final String USERNAME_PATTERN = "^\\w+$";

  @Override
  public void initialize(GoodUsername constraintAnnotation) {
    // nothing to do
  }

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    return username != null && username.matches(USERNAME_PATTERN);
  }
}

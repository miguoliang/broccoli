package broccoli.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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
  public void initialize(StrongPassword constraintAnnotation) {
    // nothing to do
  }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    return password != null && password.matches(PATTERN);
  }
}
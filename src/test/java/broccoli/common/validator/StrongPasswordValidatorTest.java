package broccoli.common.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StrongPasswordValidatorTest {

  static Stream<Arguments> passwordProvider() {
    return Stream.of(
        // Valid passwords
        Arguments.of("Abcd@123", true),
        Arguments.of("Secure123#", true),

        // Invalid passwords (missing digit)
        Arguments.of("StrongP@ss", false),
        Arguments.of("NoDigitPassword", false),
        Arguments.of("OnlyLetters@Upper", false),

        // Invalid passwords (missing uppercase letter)
        Arguments.of("nouppercase@123", false),
        Arguments.of("onlylowercaseletters123", false),

        // Invalid passwords (missing lowercase letter)
        Arguments.of("NOLOWERCASE@123", false),
        Arguments.of("ONLYUPPERCASELETTERS123", false),

        // Invalid passwords (missing special character)
        Arguments.of("SpecialCharMissing123", false),
        Arguments.of("LettersAndDigitsOnly", false),

        // Invalid passwords (too short)
        Arguments.of("Sh0r!1", false),

        // Invalid passwords (does not meet any condition)
        Arguments.of("WeakPassword", false),

        // Invalid passwords (null value)
        Arguments.of(null, false)
    );
  }

  private final StrongPasswordValidator validator = new StrongPasswordValidator();

  @ParameterizedTest
  @MethodSource("passwordProvider")
  void testPasswordValidation(String password, boolean valid) {

    assertEquals(valid, validator.isValid(password, null, null));
  }
}

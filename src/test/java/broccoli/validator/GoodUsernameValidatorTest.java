package broccoli.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import broccoli.common.validator.GoodUsernameValidator;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GoodUsernameValidatorTest {

  static Stream<Arguments> usernameProvider() {
    return Stream.of(
        // Valid usernames
        Arguments.of("user1", true),
        Arguments.of("user_2", true),
        Arguments.of("u12345", true),
        Arguments.of("user123", true),
        Arguments.of("user_123", true),

        // Invalid usernames (too short)
        Arguments.of("usr", false),
        Arguments.of("u2", false),

        // Invalid usernames (too long)
        Arguments.of(RandomStringUtils.randomAlphabetic(101), false),
        Arguments.of(RandomStringUtils.randomAlphanumeric(101), false),

        // Invalid usernames (invalid characters)
        Arguments.of("user!", false),
        Arguments.of("name space", false),
        Arguments.of("user-name", false),

        // Edge cases (exactly 5 and 100 characters)
        Arguments.of("user5", true), // 5 characters
        Arguments.of(RandomStringUtils.randomAlphanumeric(100), true), // 100 characters

        // Invalid passwords (null value)
        Arguments.of(null, false)
    );
  }

  private final GoodUsernameValidator validator = new GoodUsernameValidator();

  @ParameterizedTest
  @MethodSource("usernameProvider")
  void testUsernameValidation(String username, boolean valid) {

    assertEquals(valid, validator.isValid(username, null, null));
  }
}
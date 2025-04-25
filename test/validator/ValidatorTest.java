package bg.sofia.uni.fmi.mjt.validator;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidUserAuthenticationException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArrayOfString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateUserCredentials;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    void testCheckArgumentNotNullWithNull() {
        assertThrows(IllegalArgumentException.class,
                () -> checkArgumentNotNull(null, "argName"),
                "IllegalArgumentException is expected to be thrown when null argument is passed!");
    }

    @Test
    void testCheckArgumentNotNullWithValidArgument() {
        assertDoesNotThrow(() -> checkArgumentNotNull("argument", "testArg"),
                "Not expected to throw an exception with valid arguments!");
    }

    @Test
    void testValidateStringWithEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> validateString("", "argName"),
                "IllegalArgumentException is expected to be thrown when empty argument is passed!");
    }

    @Test
    void testValidateStringWithBlankString() {
        assertThrows(IllegalArgumentException.class,
                () -> validateString("   ", "argName"),
                "IllegalArgumentException is expected to be thrown when blank argument is passed!");
    }

    @Test
    void testValidateFilePathWithInvalidPath() {
        Path path = Path.of("invalid,path");
        assertThrows(IllegalArgumentException.class, () -> validateFilePath(path),
                "IllegalArgumentException is expected to be thrown " +
                        "when invalid path is passed as an argument!");
    }

    @Test
    void testValidateIntWithNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> validateInt(-1, "number"),
                "IllegalArgumentException is expected to be thrown " +
                        "when negative number is passed as an argument!");
    }

    @Test
    void testValidateArrayOfStringWithNullElement() {
        String[] arr = new String[] {"one", null};
        assertThrows(IllegalArgumentException.class, () -> validateArrayOfString(arr),
                "IllegalArgumentException is expected to be thrown " +
                        "when a string array with null element is passed as an argument!");
    }

    @Test
    void testValidateArgCountWithWrongCount() {
        String[] args = {"one", "two"};
        assertThrows(InvalidArgumentsCountException.class, () -> validateArgCount(args, 1),
                "InvalidArgumentsCountException is expected to be thrown " +
                        "when wrong argument count is passed as an argument!");
    }

    @Test
    void testValidateUserCredentialsWithInvalidEmail() {
        assertThrows(InvalidUserAuthenticationException.class,
                () -> validateUserCredentials("invalid-email", "password123"),
                "InvalidUserAuthenticationException is expected to be thrown " +
                        "when invalid email is passed as an argument!");
    }

    @Test
    void testValidateUserCredentialsWithShortPassword() {
        assertThrows(InvalidUserAuthenticationException.class,
                () -> validateUserCredentials("user@example.com", "123"),
                "InvalidUserAuthenticationException is expected to be thrown " +
                        "when short password is passed as an argument!");
    }

    @Test
    void testValidateUserCredentialsWithValidUserCredentials() {
        assertDoesNotThrow(() -> validateUserCredentials("user@example.com", "securePass"),
                "No exception is expected to be thrown when valid user credentials are passed!");
    }

}

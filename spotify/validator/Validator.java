package bg.sofia.uni.fmi.mjt.spotify.validator;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidUserAuthenticationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;

import java.nio.file.Path;

public class Validator {
    public static final int MIN_LEN_PASS = 6;
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String FILE_PATH_REGEX = "^[a-zA-Z0-9_/\\\\:.\\-]+$";

    public static <T> void checkArgumentNotNull(T arg, String argument) {
        if (arg == null) {
            throw new IllegalArgumentException("The " + argument + " cannot be null!");
        }
    }

    public static void validateString(String str, String argument) {
        checkArgumentNotNull(str, argument);
        if (str.isBlank()) {
            throw new IllegalArgumentException("The " + argument + " must have at least one character!");
        }
    }

    public static void validateFilePath(Path filePath) {
        String pathString = filePath.toString();
        validateString(pathString, "file path");
        if (!pathString.matches(FILE_PATH_REGEX)) {
            throw new IllegalArgumentException("The file path is invalid!");
        }
    }

    public static void validateInt(int arg, String argument) {
        if (arg < 0) {
            throw new IllegalArgumentException("The " + argument + "cannot be less than 0!");
        }
    }

    public static void validateArrayOfString(String[] arr) {
        checkArgumentNotNull(arr, "array of strings");
        for (String currStr : arr) {
            validateString(currStr, "string in the array");
        }
    }

    public static void validateArgCount(String[] args, int expectedCount) throws InvalidArgumentsCountException {
        validateArrayOfString(args);
        validateInt(expectedCount, "count of the arguments");

        if (args.length != expectedCount) {
            String excMess = "The count of the arguments is expected to be " + expectedCount +
                    " but is " + args.length + " instead!";
            throw new InvalidArgumentsCountException(excMess);
        }
    }

    public static void validateUserCredentials(String email, String pass) throws InvalidUserAuthenticationException {
        checkArgumentNotNull(email, "email");
        checkArgumentNotNull(pass, "password");

        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidUserAuthenticationException("The email is invalid!");
        }

        if (pass.length() < MIN_LEN_PASS) {
            String exceptionMessage = "The password must be at least " + MIN_LEN_PASS + " characters long!";
            throw new InvalidUserAuthenticationException(exceptionMessage);
        }
    }

}

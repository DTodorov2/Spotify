package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class InvalidUserAuthenticationException extends Exception {
    public InvalidUserAuthenticationException(String message) {
        super(message);
    }

    public InvalidUserAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

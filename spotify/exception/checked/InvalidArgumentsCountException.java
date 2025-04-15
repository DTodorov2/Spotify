package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class InvalidArgumentsCountException extends Exception {
    public InvalidArgumentsCountException(String message) {
        super(message);
    }

    public InvalidArgumentsCountException(String message, Throwable cause) {
        super(message, cause);
    }
}

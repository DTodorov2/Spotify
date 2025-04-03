package bg.sofia.uni.fmi.mjt.spotify.exception.unchecked;

public class InvalidArgumentsCountException extends RuntimeException {
    public InvalidArgumentsCountException(String message) {
        super(message);
    }

    public InvalidArgumentsCountException(String message, Throwable cause) {
        super(message, cause);
    }
}

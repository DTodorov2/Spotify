package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class SuchUserAlreadyExistsException extends AddOperationException {

    public SuchUserAlreadyExistsException(String message) {
        super(message);
    }

    public SuchUserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class MarshallingException extends Exception {

    public MarshallingException(String message) {
        super(message);
    }

    public MarshallingException(String message, Throwable cause) {
        super(message, cause);
    }

}

package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class SerializationDataException extends Exception {
    public SerializationDataException(String message) {
        super(message);
    }

    public SerializationDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

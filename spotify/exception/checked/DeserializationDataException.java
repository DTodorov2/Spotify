package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class DeserializationDataException extends MarshallingException {

    public DeserializationDataException(String message) {
        super(message);
    }

    public DeserializationDataException(String message, Throwable cause) {
        super(message, cause);
    }

}

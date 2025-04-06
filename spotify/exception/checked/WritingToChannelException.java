package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class WritingToChannelException extends SerializationException {
    public WritingToChannelException(String message) {
        super(message);
    }

    public WritingToChannelException(String message, Throwable cause) {
        super(message, cause);
    }
}

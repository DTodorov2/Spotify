package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class ReadingFromChannelException extends DeserializationException {
    public ReadingFromChannelException(String message) {
        super(message);
    }

    public ReadingFromChannelException(String message, Throwable cause) {
        super(message, cause);
    }
}

package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class ChannelConnectionException extends Exception {
    public ChannelConnectionException(String message) {
        super(message);
    }

    public ChannelConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}

package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class ClosingClientChannelException extends Exception {

    public ClosingClientChannelException(String message) {
        super(message);
    }

    public ClosingClientChannelException(String message, Throwable cause) {
        super(message, cause);
    }

}

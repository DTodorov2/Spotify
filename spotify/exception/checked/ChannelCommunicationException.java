package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class ChannelCommunicationException extends Exception {

    public ChannelCommunicationException(String message) {
        super(message);
    }

    public ChannelCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

}

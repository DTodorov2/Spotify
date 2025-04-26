package bg.sofia.uni.fmi.mjt.spotify.exception.unchecked;

public class UnsuccessfulChannelClosingException extends RuntimeException {

    public UnsuccessfulChannelClosingException(String message) {
        super(message);
    }

    public UnsuccessfulChannelClosingException(String message, Throwable cause) {
        super(message, cause);
    }

}

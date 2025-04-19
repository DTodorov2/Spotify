package bg.sofia.uni.fmi.mjt.spotify.exception.unchecked;

public class StreamingException extends RuntimeException {

    public StreamingException(String message) {
        super(message);
    }

    public StreamingException(String message, Throwable cause) {
        super(message, cause);
    }

}

package bg.sofia.uni.fmi.mjt.spotify.exception.unchecked;

public class UnsuccessfulHashingException extends RuntimeException {

    public UnsuccessfulHashingException(String message) {
        super(message);
    }

    public UnsuccessfulHashingException(String message, Throwable cause) {
        super(message, cause);
    }

}

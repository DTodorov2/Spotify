package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class UnsuccessfulLogOperationException extends Exception {
    public UnsuccessfulLogOperationException(String message) {
        super(message);
    }

    public UnsuccessfulLogOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

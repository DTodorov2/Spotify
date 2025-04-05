package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class FileCreationException extends Exception {
    public FileCreationException(String message) {
        super(message);
    }

    public FileCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

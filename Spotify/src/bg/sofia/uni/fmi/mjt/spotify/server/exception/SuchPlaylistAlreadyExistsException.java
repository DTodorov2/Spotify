package bg.sofia.uni.fmi.mjt.spotify.server.exception;

public class SuchPlaylistAlreadyExistsException extends AddOperationException {
    public SuchPlaylistAlreadyExistsException(String message) {
        super(message);
    }

    public SuchPlaylistAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

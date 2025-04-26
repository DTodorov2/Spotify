package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class LoadingDataFromFileException extends Exception {

    public LoadingDataFromFileException(String message) {
        super(message);
    }

    public LoadingDataFromFileException(String message, Throwable cause) {
        super(message, cause);
    }

}

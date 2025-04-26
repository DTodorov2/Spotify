package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class SavingDataToFileException extends Exception {

    public SavingDataToFileException(String message) {
        super(message);
    }

    public SavingDataToFileException(String message, Throwable cause) {
        super(message, cause);
    }

}

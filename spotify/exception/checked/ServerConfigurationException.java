package bg.sofia.uni.fmi.mjt.spotify.exception.checked;

public class ServerConfigurationException extends Exception {

    public ServerConfigurationException(String message) {
        super(message);
    }

    public ServerConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}

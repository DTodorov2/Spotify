package bg.sofia.uni.fmi.mjt.spotify.command.commands.messages;

public final class CommandMessages {

    private CommandMessages() { }

    public static final String SUCCESS_ADD_SONG = "The song is added successfully!";
    public static final String SUCCESS_ATTACH = "READY";
    public static final String SUCCESS_PLAYLIST_CREATION = "The playlist is created successfully!";
    public static final String SUCCESS_LOGIN = "The user is successfully logged in!";
    public static final String SUCCESS_LOGOUT = "You logged out successfully!";
    public static final String SUCCESS_REGISTER = "The user is registered successfully!";
    public static final String SUCCESS_PLAYLIST_REMOVAL = "The playlist is removed successfully!";
    public static final String SUCCESS_SONG_REMOVAL = "The song is removed successfully!";
    public static final String SUCCESS_DISCONNECT = "You disconnected successfully!";

    public static final String START_SONG_PLAYING = "The song will start playing shortly!";
    public static final String STOP_SONG_PLAYING = "The song has stopped playing!";

    public static final String NO_SUCH_SONG = "There is no such song in the app!";
    public static final String NO_SUCH_PLAYLIST = "There is no such playlist!";
    public static final String NO_SUCH_COMMAND = "No such command!";
    public static final String NO_STREAMING_SONG = "There is not a streaming song!";

    public static final String MORE_THAN_ZERO_REQUIRED = "The number cannot be less than 1!";
    public static final String LOGIN_REQUIRED = "You must be logged in in order to use this option!";
    public static final String LOGOUT_REQUIRED = "You must logout first in order to login!";
    public static final String MUSIC_STOP_REQUIRED = "You must stop the current music in order to play a new one!";

    public static final String SONG_ALREADY_IN_PLAYLIST = "This song is already in the playlist!";
    public static final String PLAYLIST_ALREADY_EXIST = "There is a playlist with this name in your repository!" +
            System.lineSeparator() + "The new playlist is not created!";
    public static final String ALREADY_LOGGED_IN = "The current user is already logged in!";

    public static final String ERROR_ATTACH = "ERROR";
    public static final String ERROR_USER_CREDENTIALS = "There was an error with the credentials. ";
    public static final String WRONG_COUNT_PARAMETERS = "Wrong number of parameters! ";
    public static final String INCORRECT_PASSWORD = "Incorrect password!";
    public static final String FAIL_REGISTER = "This email is taken! You cannot be registered!";
    public static final String USER_DOES_NOT_EXIST = "User with this email does not exist in the system!";
    public static final String SONG_NOT_IN_PLAYLIST = "The given song is not in the playlist!";

    public static final String SONG_EXISTS = "Exists";
    public static final String EMPTY_MESSAGE = "";
    public static final String EMPTY_PLAYLIST = "No songs in this playlist.";

    public static String getWrongParametersMessage(int expectedCount, int actualCount) {
        return WRONG_COUNT_PARAMETERS + "The count of the arguments is expected to be " +
                (expectedCount - 1) + " but is " + (actualCount - 1) + " instead!";
    }

}

package bg.sofia.uni.fmi.mjt.command;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandExecutorTest {

    private static final String REMOVE_COMMAND_SUCCESS_MESS = "Removed";
    private static final String ADD_COMMAND_FAIL_MESS = "Not added!";
    private static final String ADD_COMMAND_SUCCESS_MESS = "Added";
    private static final String PLAY_COMMAND_MESS = "Playing";
    private static final String EXAMPLE_USER_EMAIL = "user";
    private static final String EXAMPLE_USER_PASS = "pass";
    private static final String REMOVE_COMMAND = "remove-song-from";
    private static final String LOGIN_COMMAND = "login";
    private static final String PLAY_COMMAND = "play";
    private static final String ADD_COMMAND = "add-song-to";
    private static final String EXAMPLE_SONG_TITLE = "OceanEyes";
    private static final String EXAMPLE_SONG_ARTIST = "Chill";
    private static final String[] ARGUMENTS = new String[]{EXAMPLE_SONG_ARTIST, EXAMPLE_SONG_TITLE};

    private ClientCommandInvoker clientInvoker;
    private CommandExecutor commandExecutor;

    @BeforeEach
    void setUp() {
        clientInvoker = mock(ClientCommandInvoker.class);
        ClientResources clientResources = mock(ClientResources.class);

        commandExecutor = new CommandExecutor(clientResources, clientInvoker);
    }

    @Test
    void testExecuteSimpleCommand() {
        try {
            String[] argsArr = new String[]{EXAMPLE_USER_EMAIL, EXAMPLE_USER_PASS};
            when(clientInvoker.executeCommand(LOGIN_COMMAND, argsArr)).thenReturn("Logged in");

            String clientInput = LOGIN_COMMAND + " " + EXAMPLE_USER_EMAIL + " " + EXAMPLE_USER_PASS;
            String result = commandExecutor.execute(clientInput);

            assertEquals("Logged in", result, "Logged in is expected to be returned from the executor");
            verify(clientInvoker).executeCommand(LOGIN_COMMAND, argsArr);
        } catch (ChannelCommunicationException e) {
            fail("The testExecuteSimpleCommand test cannot be executed: " + e.getMessage());
        }
    }

    @Test
    void testExecutePlayCommandWithSongTitleWithMoreThanOneWord() {
        try {
            String[] argsArr = new String[]{EXAMPLE_SONG_TITLE + EXAMPLE_SONG_TITLE};
            when(clientInvoker.executeCommand(PLAY_COMMAND, argsArr)).thenReturn(PLAY_COMMAND_MESS);

            String clientInput = PLAY_COMMAND + " " + EXAMPLE_SONG_TITLE + " " + EXAMPLE_SONG_TITLE;
            String result = commandExecutor.execute(clientInput);

            assertEquals(PLAY_COMMAND_MESS, result, "Playing is expected to be returned from the executor");

            verify(clientInvoker).executeCommand(PLAY_COMMAND, argsArr);
        } catch (ChannelCommunicationException e) {
            fail("The testExecutePlayCommandWithSongTitleWithMoreThanOneWord test cannot be executed: " +
                    e.getMessage());
        }
    }

    @Test
    void testExecuteAddSongToCommand() {
        try {
            when(clientInvoker.executeCommand(ADD_COMMAND, ARGUMENTS)).thenReturn(ADD_COMMAND_SUCCESS_MESS);

            String clientInput = ADD_COMMAND + " " + EXAMPLE_SONG_ARTIST + " " + EXAMPLE_SONG_TITLE;
            String result = commandExecutor.execute(clientInput);

            assertEquals(ADD_COMMAND_SUCCESS_MESS, result, "Added is expected to be returned from the executor");

            verify(clientInvoker).executeCommand(ADD_COMMAND, ARGUMENTS);
        } catch (ChannelCommunicationException e) {
            fail("The testExecuteAddSongToCommand test cannot be executed: " + e.getMessage());
        }
    }

    @Test
    void testExecuteWithAddCommandWithInvalidArgs() {
        try {
            String[] argsArr = new String[]{EXAMPLE_SONG_ARTIST};
            when(clientInvoker.executeCommand(ADD_COMMAND, argsArr)).thenReturn(ADD_COMMAND_FAIL_MESS);

            String result = commandExecutor.execute(ADD_COMMAND + " " + EXAMPLE_SONG_ARTIST);
            assertEquals(ADD_COMMAND_FAIL_MESS, result, "Not added is expected to be returned from the executor");

            verify(clientInvoker).executeCommand(ADD_COMMAND, argsArr);
        } catch (ChannelCommunicationException e) {
            fail("The testExecuteWithCommandWithInvalidArgs test cannot be executed: " + e.getMessage());
        }
    }

    @Test
    void testExecuteWithRemoveSongCommand() {
        try {
            when(clientInvoker.executeCommand(REMOVE_COMMAND, ARGUMENTS)).thenReturn(REMOVE_COMMAND_SUCCESS_MESS);

            String clientInput = REMOVE_COMMAND + " " + EXAMPLE_SONG_ARTIST + " " + EXAMPLE_SONG_TITLE;
            String result = commandExecutor.execute(clientInput);

            assertEquals(REMOVE_COMMAND_SUCCESS_MESS, result, "Removed is expected to be returned from the executor");

            verify(clientInvoker).executeCommand(REMOVE_COMMAND, ARGUMENTS);
        } catch (ChannelCommunicationException e) {
            fail("The testExecuteWithRemoveSongCommand test cannot be executed: " + e.getMessage());
        }
      }

    @Test
    void testExecuteNullInput() {
        try {
            String result = commandExecutor.execute(null);

            String errMess = "You must enter a command message is expected to be returned from the executor";
            assertEquals("You must enter a command!", result, errMess);
        } catch (ChannelCommunicationException e) {
            fail("The testExecuteNullInput test could not be executed: " + e.getMessage());
        }
    }

}

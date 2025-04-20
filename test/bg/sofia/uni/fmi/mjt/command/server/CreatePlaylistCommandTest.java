package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.PLAYLIST_ALREADY_EXIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_PLAYLIST_CREATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreatePlaylistCommandTest {

    private ServerResources mockServerResources;
    private User mockUser;
    private String[] args;
    private CreatePlaylistCommand command;

    @BeforeEach
    public void setUp() {
        mockServerResources = mock(ServerResources.class);
        mockUser = mock(User.class);
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        args = new String[]{"newPlaylist"};
        command = new CreatePlaylistCommand(args, mockServerResources);
    }

    @Test
    public void testExecuteWithInvalidArgsCount() {
        String[] arguments = {};
        command.setArguments(new String[]{});

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    public void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new CreatePlaylistCommand(null, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new CreatePlaylistCommand(arguments, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String[] arguments = {"parameter"};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new CreatePlaylistCommand(arguments, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new CreatePlaylistCommand(arguments, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWhenUserNotLoggedIn()  {
        when(mockServerResources.getLoggedUser()).thenReturn(null);
        String result = executeCommand();

        assertEquals(LOGIN_REQUIRED, result,
                "Should return " + LOGIN_REQUIRED + " if the user is not logged in.");
    }

    @Test
    public void testExecuteWhenPlaylistAlreadyExists() {
        when(mockUser.addPlaylist(anyString())).thenReturn(false);
        String result = executeCommand();

        assertEquals(PLAYLIST_ALREADY_EXIST, result,
                "Should return " + PLAYLIST_ALREADY_EXIST + " if the user is not logged in.");
    }

    @Test
    public void testExecuteWhenPlaylistCreatedSuccessfully() {
        when(mockUser.addPlaylist(anyString())).thenReturn(true);
        String result = executeCommand();

        assertEquals(SUCCESS_PLAYLIST_CREATION, result,
                "Should return " + SUCCESS_PLAYLIST_CREATION + " if the user is not logged in.");
    }

    @Test
    public void testExecuteWithInvalidArguments() {
        args = new String[]{};
        command = new CreatePlaylistCommand(args, mockServerResources);

        assertThrows(InvalidArgumentsCountException.class, command::execute,
                "InvalidArgumentsCountException is expected to be thrown for invalid arguments.");
    }

    private String executeCommand() {
        String result = null;
        try {
            result = command.execute();
            return result;
        } catch (InvalidArgumentsCountException e) {
            fail("The create-playlist command could not execute!");
        }

        return result;
    }

}

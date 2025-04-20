package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.RemovePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_PLAYLIST_REMOVAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RemovePlaylistCommandTest {

    private ServerResources mockResources;
    private User mockUser;
    private RemovePlaylistCommand command;
    private static final String EXAMPLE_PARAMETER = "parameter";

    @BeforeEach
    void setUp() {
        mockResources = mock(ServerResources.class);
        mockUser = mock(User.class);

        String[] args = {"playlist-name"};
        command = new RemovePlaylistCommand(args, mockResources);
    }

    @Test
    public void testExecuteWithInvalidArgsCount() {
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
                () -> new RemovePlaylistCommand(null, mockResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {EXAMPLE_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemovePlaylistCommand(arguments, mockResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemovePlaylistCommand(new String[]{EXAMPLE_PARAMETER}, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {EXAMPLE_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RemovePlaylistCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteReturnsLoginRequiredWhenUserNotLoggedIn() {
        when(mockResources.getLoggedUser()).thenReturn(null);

        String result = executeCommand();

        assertEquals(LOGIN_REQUIRED, result);
    }

    @Test
    void testExecuteReturnsSuccessWhenPlaylistRemoved() {
        when(mockResources.getLoggedUser()).thenReturn(mockUser);
        when(mockUser.removePlaylist("playlist-name")).thenReturn(true);


        String result = executeCommand();

        assertEquals(SUCCESS_PLAYLIST_REMOVAL, result);
    }

    @Test
    void testExecuteReturnsNoSuchPlaylistWhenPlaylistMissing() {
        when(mockResources.getLoggedUser()).thenReturn(mockUser);
        when(mockUser.removePlaylist("playlist-name")).thenReturn(false);

        String result = executeCommand();

        assertEquals(NO_SUCH_PLAYLIST, result);
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

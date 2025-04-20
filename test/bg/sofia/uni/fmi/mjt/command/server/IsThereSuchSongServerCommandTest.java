package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.IsThereSuchSongServerCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_SONG;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SONG_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsThereSuchSongServerCommandTest {

    private ServerResources mockServerResources;
    private SongsRepository mockSongsRepository;
    private User mockUser;
    private IsThereSuchSongServerCommand command;

    String[] arguments = new String[]{"parameter"};

    @BeforeEach
    public void setUp() {
        mockServerResources = mock(ServerResources.class);
        mockSongsRepository = mock(SongsRepository.class);
        mockUser = mock(User.class);

        when(mockServerResources.getSongsRepository()).thenReturn(mockSongsRepository);

        command = new IsThereSuchSongServerCommand(arguments, mockServerResources);
    }

    @Test
    public void testExecuteWithInvalidArgsCount() {

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        command = new IsThereSuchSongServerCommand(new String[]{}, mockServerResources);
        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    public void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new IsThereSuchSongServerCommand(null, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new IsThereSuchSongServerCommand(arguments, mockServerResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new IsThereSuchSongServerCommand(arguments, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new IsThereSuchSongServerCommand(arguments, mockServerResources), errMess);
    }


    @Test
    public void testExecuteUserNotLoggedIn() {
        when(mockServerResources.getLoggedUser()).thenReturn(null);
        String result = executeCommand();

        assertEquals(LOGIN_REQUIRED, result);
    }

    @Test
    public void testExecuteSongNotFound() {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockSongsRepository.getSongByName(anyString())).thenReturn(null);

        String result = executeCommand();

        assertEquals(NO_SUCH_SONG, result);
    }

    @Test
    public void testExecuteSongFound() {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);
        when(mockSongsRepository.getSongByName(anyString())).thenReturn(mock(Song.class));

        String result = executeCommand();

        assertEquals(SONG_EXISTS, result);
    }

    private String executeCommand() {
        String result = null;
        try {
            result = command.execute();
            return result;
        } catch (InvalidArgumentsCountException e) {
            fail("The check-song command could not execute!");
        }

        return result;
    }

}

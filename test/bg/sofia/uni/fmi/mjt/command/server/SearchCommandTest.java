package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.SearchCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchCommandTest {

    private static final String EXAMPLE_PARAMETER = "parameter";

    private ServerResources mockResources;
    private SongsRepository mockSongsRepository;

    private SearchCommand command;
    @BeforeEach
    void setUp() {
        mockResources = mock(ServerResources.class);
        mockSongsRepository = mock(SongsRepository.class);
        when(mockResources.getSongsRepository()).thenReturn(mockSongsRepository);

        command = new SearchCommand(new String[]{EXAMPLE_PARAMETER}, mockResources);
    }

    @Test
    public void testExecuteWithInvalidArgsCount() {
        String[] arguments = {};
        command = new SearchCommand(arguments, mockResources);

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    public void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new SearchCommand(null, mockResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new SearchCommand(arguments, mockResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String[] arguments = {"parameter"};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new SearchCommand(arguments, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new SearchCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteReturnsLoginRequiredWhenUserNotLogged() {
        when(mockResources.isUserLogged()).thenReturn(false);

        String result = executeCommand();

        assertEquals(LOGIN_REQUIRED, result);
    }

    @Test
    void testExecuteReturnsFormattedSongListWhenLoggedIn() {
        when(mockResources.isUserLogged()).thenReturn(true);

        Song mockSong1 = mock(Song.class);
        Song mockSong2 = mock(Song.class);

        when(mockSong1.toString()).thenReturn("Song One - Artist A");
        when(mockSong2.toString()).thenReturn("Song Two - Artist B");

        when(mockSongsRepository.searchByWords(EXAMPLE_PARAMETER))
                .thenReturn(List.of(mockSong1, mockSong2));

        String result = executeCommand();

        String expected = "Songs: " + System.lineSeparator() + "1. Song One - Artist A" +
                System.lineSeparator() + "2. Song Two - Artist B";

        assertEquals(expected, result);
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

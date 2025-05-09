package bg.sofia.uni.fmi.mjt.command.server;

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

    private static final String TEST_PARAMETER = "parameter";

    private ServerResources mockResources;
    private SongsRepository mockSongsRepository;

    private SearchCommand command;

    @BeforeEach
    void setUp() {
        mockResources = mock(ServerResources.class);
        mockSongsRepository = mock(SongsRepository.class);
        when(mockResources.getSongsRepository()).thenReturn(mockSongsRepository);

        command = new SearchCommand(new String[]{TEST_PARAMETER}, mockResources);
    }

    @Test
    void testExecuteWithInvalidArgsCount() {
        command.setArguments(new String[]{});

        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new SearchCommand(null, mockResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {TEST_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new SearchCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArgument() {
        String[] arguments = {TEST_PARAMETER};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new SearchCommand(arguments, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArgument() {
        String[] arguments = {TEST_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new SearchCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteNotLoggedUser() {
        when(mockResources.isUserLogged()).thenReturn(false);

        String result = executeCommand();
        assertEquals(LOGIN_REQUIRED, result, LOGIN_REQUIRED + " message is expected to be shown!");
    }

    @Test
    void testExecuteWithValidArguments() {
        when(mockResources.isUserLogged()).thenReturn(true);

        Song mockSong1 = mock(Song.class);
        Song mockSong2 = mock(Song.class);

        when(mockSong1.toString()).thenReturn("Song One - Artist A");
        when(mockSong2.toString()).thenReturn("Song Two - Artist B");

        when(mockSongsRepository.searchByWords(TEST_PARAMETER))
                .thenReturn(List.of(mockSong1, mockSong2));

        String result = executeCommand();
        String expected = "Songs: " + System.lineSeparator() + "1. Song One - Artist A" +
                System.lineSeparator() + "2. Song Two - Artist B";
        assertEquals(expected, result, "Formatted songs output is expected!");
    }

    private String executeCommand() {
        try {
            return command.execute();
        } catch (InvalidArgumentsCountException e) {
            fail("The search command could not execute! Reason: " + e.getMessage());
        }

        return null;
    }

}

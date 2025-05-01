package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.TopSongsCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.MORE_THAN_ZERO_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NUMBER_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TopsSongsCommandTest {

    private static final String TEST_PARAMETER = "parameter";

    private ServerResources mockResources;
    private TopSongsCommand command;

    @BeforeEach
    void setUp() {
        mockResources = mock(ServerResources.class);
        when(mockResources.isUserLogged()).thenReturn(true);

        command = new TopSongsCommand(new String[]{"0"}, mockResources);
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
                () -> new TopSongsCommand(null, mockResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {TEST_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new TopSongsCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArgument() {
        String[] arguments = {TEST_PARAMETER};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new TopSongsCommand(arguments, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArgument() {
        String[] arguments = {TEST_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new TopSongsCommand(arguments, mockResources), errMess);
    }

    @Test
    void testExecuteWhenUserNotLoggedIn() {
        when(mockResources.isUserLogged()).thenReturn(false);

        String result = executeCommand();
        assertEquals(LOGIN_REQUIRED, result, LOGIN_REQUIRED + " message is expected to be shown!");
    }

    @Test
    void testExecuteWithLessThanOne() {
        String result = executeCommand();

        assertEquals(MORE_THAN_ZERO_REQUIRED, result,
                MORE_THAN_ZERO_REQUIRED + " message is expected to be shown!");
    }

    @Test
    void testExecuteWithSuccessfulArgs() {
        SongsRepository mockSongsRepo = mock(SongsRepository.class);

        Song song1 = new Song("testTitle1", "testArtist1", 1);
        Song song2 = new Song("testTitle2", "testArtist2", 0);
        List<Song> topSongs = List.of(song1, song2);

        when(mockResources.getSongsRepository()).thenReturn(mockSongsRepo);
        when(mockSongsRepo.getTopStatistics(2)).thenReturn(topSongs);

        command.setArguments(new String[]{"2"});
        String result = executeCommand();

        String expected = "Songs: " + System.lineSeparator()
                + "1. " + song1 + System.lineSeparator()
                + "2. " + song2;

        assertEquals(expected, result, "Formatted songs list is expected to be shown!");
    }

    @Test
    void testExecuteWithInvalidArgumentType() {
        command.setArguments(new String[]{TEST_PARAMETER});
        String result = executeCommand();

        assertEquals(NUMBER_REQUIRED, result, NUMBER_REQUIRED + " message is expected to be shown!");
    }

    private String executeCommand() {
        try {
            return command.execute();
        } catch (InvalidArgumentsCountException e) {
            fail("The top command could not execute!");
        }

        return null;
    }

}

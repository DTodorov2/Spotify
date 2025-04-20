package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.AddSongToPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.AttachEmailToKeyCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.ERROR_ATTACH;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_PLAYLIST;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_ATTACH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AttachEmailToKeyCommandTest {

    private ServerResources mockServerResources;
    private SelectionKey mockSelectionKey;
    private AttachEmailToKeyCommand command;
    private final static String[] ARGUMENTS = {"email@example.bg"};

    @BeforeEach
    void setUp() {
        mockServerResources = mock(ServerResources.class);
        mockSelectionKey = mock(SelectionKey.class);

        when(mockServerResources.getSelectionKey()).thenReturn(mockSelectionKey);

        command = new AttachEmailToKeyCommand(ARGUMENTS, mockServerResources);
    }

    @Test
    void testExecuteWithInvalidArgumentsCount() {
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
                () -> new AttachEmailToKeyCommand(null, mockServerResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new AttachEmailToKeyCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArg() {
        String[] arguments = {"parameter"};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new AttachEmailToKeyCommand(arguments, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new AttachEmailToKeyCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testExecuteWhenSelectionKeyIsNull() {
        when(mockServerResources.getSelectionKey()).thenReturn(null);

        String result = executeCommand();
        assertEquals(ERROR_ATTACH, result, "Expected " + ERROR_ATTACH + " message to be shown!");
    }

    @Test
    void testExecuteWithValidArguments() {
        String result = executeCommand();
        assertEquals(SUCCESS_ATTACH, result, "Expected " + SUCCESS_ATTACH + " message to be shown!") ;
    }

    private String executeCommand() {
        String result = null;
        try {
            result = command.execute();
            return result;
        } catch (InvalidArgumentsCountException e) {
            fail("The attach command could not execute!");
        }

        return result;
    }

}

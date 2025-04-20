package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.ServerCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.NO_SUCH_COMMAND;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.WRONG_COUNT_PARAMETERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

public class ServerCommandInvokerTest {

    private ServerCommandInvoker invoker;

    @BeforeEach
    void setUp() {
        ServerResources mockResources = mock(ServerResources.class);
        invoker = new ServerCommandInvoker(mockResources);
    }

    @Test
    void testExecuteValidCommandReturnsExpectedResult() {
        String result = executeCommand("show-all-commands", new String[]{});

        assertTrue(result.contains("Available Commands"));
    }

    @Test
    void testExecuteUnknownCommandReturnsNoSuchCommand() {
        String result = executeCommand("random-cmd", new String[]{});

        assertEquals(NO_SUCH_COMMAND, result);
    }

    @Test
    void testExecuteCommandWithInvalidArgsCountReturnsError() {
        String result = executeCommand("login", new String[]{});

        assertTrue(result.contains(WRONG_COUNT_PARAMETERS));
    }

    @Test
    void testExecuteCommandWithNullCommand() {
        assertThrows(IllegalArgumentException.class,
                () -> executeCommand(null, new String[]{"arg1"}));
    }

    @Test
    void testExecuteCommandWithNullArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> executeCommand("login", null));
    }

    private String executeCommand(String cmd, String[] args) {
        String result = null;
        try {
            result = invoker.executeCommand(cmd, args);
            return result;
        } catch (ChannelCommunicationException e) {
            fail("The create-playlist command could not execute!");
        }

        return result;
    }

}

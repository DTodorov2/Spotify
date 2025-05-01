package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.ServerCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
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
    void testExecuteValidCommand() {
        String result = executeCommand("show-all-commands", new String[]{});

        assertTrue(result != null && result.contains("Available Commands"),
                "All commands are expected to be shown!");
    }

    @Test
    void testExecuteUnknownCommand() {
        String result = executeCommand("random-cmd", new String[]{});

        assertEquals(NO_SUCH_COMMAND, result, NO_SUCH_COMMAND + " message is expected to be shown!");
    }

    @Test
    void testExecuteCommandWithInvalidArgsCount() {
        String result = executeCommand("login", new String[]{});

        assertTrue(result != null && result.contains(WRONG_COUNT_PARAMETERS),
                "Wrong count parameters message is expected!");
    }

    @Test
    void testExecuteCommandWithNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> executeCommand(null, new String[]{"arg1"}),
                "IllegalArgumentException is expected to be thrown when null string as a command is passed!");
    }

    @Test
    void testExecuteCommandWithNullArgs() {
        assertThrows(IllegalArgumentException.class, () -> executeCommand("login", null),
                "IllegalArgumentException is expected to be thrown when null args array is passed!");
    }

    private String executeCommand(String cmd, String[] args) {
        try {
            return invoker.executeCommand(cmd, args);
        } catch (ChannelCommunicationException e) {
            fail("The command could not execute! Reason: " + e.getMessage());
        }

        return null;
    }

}

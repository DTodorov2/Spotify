package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.LogoutCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGIN_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_LOGOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogoutCommandTest {

    private static final String[] ARGUMENTS = new String[]{};
    private static final String TEST_PARAMETER = "parameter";

    private static ServerResources mockServerResources;
    private static User mockUser;
    private static SelectionKey mockSelectionKey;
    private LogoutCommand command;

    @BeforeAll
    static void setUpBeforeAll() {
        mockServerResources = mock(ServerResources.class);
        mockUser = mock(User.class);
        mockSelectionKey = mock(SelectionKey.class);
    }

    @BeforeEach
    void setUpBeforeEach() {
        when(mockServerResources.getSelectionKey()).thenReturn(mockSelectionKey);
        command = new LogoutCommand(ARGUMENTS, mockServerResources);
    }

    @Test
    void testExecuteWithInvalidArgsCount() {
        command.setArguments(new String[]{TEST_PARAMETER});
        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new LogoutCommand(null, mockServerResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {TEST_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new LogoutCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArgument() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new LogoutCommand(ARGUMENTS, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArgument() {
        String[] arguments = {TEST_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new LogoutCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testExecuteWhenNotLoggedInReturnsLoginRequired() {
        when(mockServerResources.getLoggedUser()).thenReturn(null);

        String result = executeCommand();
        assertEquals(LOGIN_REQUIRED, result, LOGIN_REQUIRED + " message is expected to be shown!");
    }

    @Test
    void testExecuteWhenLoggedInLogsOutSuccessfully() {
        when(mockServerResources.getLoggedUser()).thenReturn(mockUser);

        String result = executeCommand();
        assertEquals(SUCCESS_LOGOUT, result, SUCCESS_LOGOUT + " message is expected to be shown!");

        verify(mockUser).setLoggedIn(false);
        verify(mockSelectionKey).attach(null);
    }

    private String executeCommand() {
        try {
            return command.execute();
        } catch (InvalidArgumentsCountException e) {
            fail("The logout command could not execute! Reason: " + e.getMessage());
        }

        return null;
    }

}

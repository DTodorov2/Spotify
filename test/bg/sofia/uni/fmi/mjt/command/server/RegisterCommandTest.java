package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.RegisterCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.ERROR_USER_CREDENTIALS;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.FAIL_REGISTER;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_REGISTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterCommandTest {

    private static final String TEST_PARAMETER = "parameter";
    private static final String[] ARGUMENTS = {"user@email.com", "ValidPassword123"};
    private final ServerResources serverResources = new ServerResources();
    private UsersRepository mockUsersRepo;

    private RegisterCommand command;

    @BeforeEach
    void setUp() {
        ServerResources mockResources = mock(ServerResources.class);
        mockUsersRepo = mock(UsersRepository.class);
        when(mockUsersRepo.add(any())).thenReturn(false);
        when(mockResources.getUsersRepository()).thenReturn(mockUsersRepo);

        command = new RegisterCommand(ARGUMENTS, mockResources);
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
                () -> new RegisterCommand(null, serverResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {TEST_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RegisterCommand(arguments, serverResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArgument() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RegisterCommand(new String[]{TEST_PARAMETER}, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArgument() {
        String[] arguments = {TEST_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RegisterCommand(arguments, serverResources), errMess);
    }

    @Test
    void testExecuteWithInvalidUserCredentials() {
        command.setArguments(new String[]{"invalidEmail", "short"});
        String result = executeCommand();
        assertTrue(result != null && result.contains(ERROR_USER_CREDENTIALS),
                ERROR_USER_CREDENTIALS + " message is expected!");
    }

    @Test
    void testExecuteWithUniqueEmail() {
        when(mockUsersRepo.add(any())).thenReturn(true);

        String result = executeCommand();
        assertEquals(SUCCESS_REGISTER, result, SUCCESS_REGISTER + " message is expected to be shown!");
    }

    @Test
    void testExecuteWithExistingEmail() {
        String result = executeCommand();
        assertEquals(FAIL_REGISTER, result, FAIL_REGISTER + " message is expected to be shown!");
    }

    private String executeCommand() {
        try {
            return command.execute();
        } catch (InvalidArgumentsCountException e) {
            fail("The register command could not execute! Reason: " + e.getMessage());
        }

        return null;
    }

}

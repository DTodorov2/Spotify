package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.PlaySongServerCommand;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.RegisterCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidUserAuthenticationException;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import bg.sofia.uni.fmi.mjt.spotify.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.ERROR_USER_CREDENTIALS;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.FAIL_REGISTER;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_REGISTER;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateUserCredentials;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class RegisterCommandTest {

    private static final String EXAMPLE_PARAMETER = "parameter";
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
    public void testExecuteWithInvalidArgsCount() {
        command.setArguments(new String[]{EXAMPLE_PARAMETER});
        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    public void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";


        assertThrows(IllegalArgumentException.class,
                () -> new RegisterCommand(null, serverResources), errMess);
    }

    @Test
    public void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {EXAMPLE_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RegisterCommand(arguments, serverResources), errMess);
    }

    @Test
    public void testExecuteWithNullServerResourcesArg() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RegisterCommand(new String[]{EXAMPLE_PARAMETER}, null), errMess);
    }

    @Test
    public void testExecuteWithNullStringArg() {
        String[] arguments = {EXAMPLE_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new RegisterCommand(arguments, serverResources), errMess);
    }

    @Test
    void testExecuteThrowsInvalidArgumentsCountExceptionWhenArgumentsAreLess() {
        command.setArguments(new String[]{EXAMPLE_PARAMETER});

        assertThrows(InvalidArgumentsCountException.class, command::execute);
    }

    @Test
    void testExecuteReturnsErrorWhenInvalidUserCredentials() {
        try (MockedStatic<Validator> mockedValidator = mockStatic(bg.sofia.uni.fmi.mjt.spotify.validator.Validator.class)) {
            mockedValidator
                    .when(() -> validateUserCredentials("invalidEmail", "short"))
                    .thenThrow(new InvalidUserAuthenticationException("Test invalid"));

            command.setArguments(new String[]{"invalidEmail", "short"});
            String result = executeCommand();
            assertTrue(result.contains(ERROR_USER_CREDENTIALS));
        }
    }

    @Test
    void testExecuteReturnsSuccessRegisterWhenAddReturnsTrue() {
        when(mockUsersRepo.add(any())).thenReturn(true);
        String result = executeCommand();

        assertEquals(SUCCESS_REGISTER, result);
    }

    @Test
    void testExecuteReturnsFailRegisterWhenAddReturnsFalse() {
        String result = executeCommand();

        assertEquals(FAIL_REGISTER, result);
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

package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.LoginCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import bg.sofia.uni.fmi.mjt.spotify.server.security.SHA256;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.ALREADY_LOGGED_IN;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.INCORRECT_PASSWORD;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.LOGOUT_REQUIRED;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.SUCCESS_LOGIN;
import static bg.sofia.uni.fmi.mjt.spotify.command.commands.messages.CommandMessages.USER_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginCommandTest {

    private static final String EXAMPLE_PASS = "pass";
    private static final String EXAMPLE_EMAIL = "email";
    private static final String EXAMPLE_USER_ID = "user-id";
    private static final String EXAMPLE_PARAMETER = "parameter";
    private static final String[] ARGUMENTS = new String[]{EXAMPLE_EMAIL, EXAMPLE_PASS};

    private static ServerResources mockServerResources;
    private static UsersRepository mockUsersRepository;
    private static SelectionKey mockSelectionKey;
    private static User mockUser;
    private LoginCommand command;

    @BeforeAll
    static void setUpBeforeAll() {
        mockServerResources = mock(ServerResources.class);
        mockUsersRepository = mock(UsersRepository.class);
        mockSelectionKey = mock(SelectionKey.class);
        mockUser = mock(User.class);
    }

    @BeforeEach
    void setUpBeforeEach() {
        when(mockServerResources.getUsersRepository()).thenReturn(mockUsersRepository);
        when(mockServerResources.getSelectionKey()).thenReturn(mockSelectionKey);
        when(mockServerResources.getLoggedUser()).thenReturn(null);

        Map<String, User> users = new HashMap<>();
        when(mockUsersRepository.getMap()).thenReturn(users);

        users.put(EXAMPLE_EMAIL, mockUser);

        String hashedPass = SHA256.hash(EXAMPLE_PASS);
        when(mockUser.getPassword()).thenReturn(hashedPass);

        command = new LoginCommand(ARGUMENTS, mockServerResources);
    }

    @Test
    void testExecuteWithInvalidArgsCount() {
        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        command = new LoginCommand(new String[]{}, mockServerResources);
        assertThrows(InvalidArgumentsCountException.class, () -> command.execute(), errMess);
    }

    @Test
    void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new LoginCommand(null, mockServerResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {EXAMPLE_PARAMETER, "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new LoginCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArg() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new LoginCommand(ARGUMENTS, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArg() {
        String[] arguments = {EXAMPLE_PARAMETER, null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new LoginCommand(arguments, mockServerResources), errMess);
    }

    @Test
    void testAlreadyLoggedIn() {
        when(mockServerResources.getLoggedUser()).thenReturn(mock(User.class));

        String result = executeCommand();
        assertEquals(LOGOUT_REQUIRED, result, LOGOUT_REQUIRED + " message is expected to be shown!");
    }

    @Test
    void testUserDoesNotExist() {
        when(mockServerResources.getLoggedUser()).thenReturn(null);
        when(mockUsersRepository.getMap()).thenReturn(new HashMap<>());

        String result = executeCommand();
        assertEquals(USER_DOES_NOT_EXIST, result, USER_DOES_NOT_EXIST + " message is expected to be shown!");
    }

    @Test
    void testExecuteWithIncorrectPassword() {
        String[] arguments = new String[]{EXAMPLE_EMAIL, "wrongPass"};

        command = new LoginCommand(
                arguments,
                mockServerResources
        );

        String result = executeCommand();
        assertEquals(INCORRECT_PASSWORD, result, INCORRECT_PASSWORD + " message is expected to be shown!");
    }

    @Test
    void testSuccessfulLogin() {
        when(mockUser.isLoggedIn()).thenReturn(false);
        when(mockUser.getId()).thenReturn(EXAMPLE_USER_ID);

        String result = executeCommand();
        assertEquals(SUCCESS_LOGIN, result, SUCCESS_LOGIN + " message is expected to be shown!");
    }

    @Test
    void testUserAlreadyLoggedInReturnsCorrectMessage() {
        when(mockUser.isLoggedIn()).thenReturn(true);

        String result = executeCommand();
        assertEquals(ALREADY_LOGGED_IN, result, ALREADY_LOGGED_IN + " message is expected to be shown!");
    }

    private String executeCommand() {
        String result = null;
        try {
            result = command.execute();
            return result;
        } catch (InvalidArgumentsCountException e) {
            fail("The login command could not execute!");
        }

        return result;
    }

}

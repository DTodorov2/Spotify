package bg.sofia.uni.fmi.mjt.command.server;

import bg.sofia.uni.fmi.mjt.spotify.command.commands.server.ShowAllCommandsCommand;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShowAllCommandsCommandTest {

    private static final String EXAMPLE_PARAMETER = "parameter";
    private final ServerResources serverResources = new ServerResources();
    private final ShowAllCommandsCommand command = new ShowAllCommandsCommand(new String[]{}, serverResources);

    @Test
    void testExecuteWithInvalidArgsCount() {
        command.setArguments(new String[]{EXAMPLE_PARAMETER, EXAMPLE_PARAMETER});
        String errMess = "InvalidArgumentsCountException is expected to be thrown " +
                "when invalid number of arguments is passed!";

        assertThrows(InvalidArgumentsCountException.class, command::execute, errMess);
    }

    @Test
    void testExecuteWithNullArgs() {
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null args array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new ShowAllCommandsCommand(null, serverResources), errMess);
    }

    @Test
    void testExecuteWithBlankStringInTheArray() {
        String[] arguments = {"parameter", "    "};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a blank argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new ShowAllCommandsCommand(arguments, serverResources), errMess);
    }

    @Test
    void testExecuteWithNullServerResourcesArg() {
        String[] arguments = {"parameter"};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null server resources argument is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new ShowAllCommandsCommand(arguments, null), errMess);
    }

    @Test
    void testExecuteWithNullStringArg() {
        String[] arguments = {"parameter", null};
        String errMess = "IllegalArgumentException is expected to be thrown " +
                "when a null string element in the argument array is passed!";

        assertThrows(IllegalArgumentException.class,
                () -> new ShowAllCommandsCommand(arguments, serverResources), errMess);
    }

    @Test
    void testExecuteWithSuccessfulArgs() throws Exception {
        String expected = "Available Commands:" + System.lineSeparator() +
                "register <email> <password> -> register the user into the system" + System.lineSeparator() +
                "login <email> <password> -> login the user" + System.lineSeparator() +
                "disconnect -> disconnects the user from the system" + System.lineSeparator() +
                "search <words> -> returns all songs whose titles or artist names contain the searched word/words" + System.lineSeparator() +
                "top <number> -> returns a list of the number most listened-to songs, sorted in descending order." + System.lineSeparator() +
                "create-playlist <name_of_the_playlist>" + System.lineSeparator() +
                "add-song-to <name_of_the_playlist> <song>" + System.lineSeparator() +
                "show-playlist <name_of_the_playlist>" + System.lineSeparator() +
                "play <song>" + System.lineSeparator() +
                "stop -> stops the streaming song";

        String actual = command.execute();
        assertEquals(expected, actual, "All commands are expected to be shown!");
    }

}

package bg.sofia.uni.fmi.mjt.command.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.LoginClientCommand;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginClientCommandTest {

    private static final String EXAMPLE_USER_EMAIL = "user@example.com";
    private static final String LOGIN_COMMAND = "login ";
    private static final String EXAMPLE_USER_PASS = " pass123";
    private static final String NOT_NULL_RESULT_MESSAGE = "Result should not be null!";

    private ClientResources mockClientResources;
    private LoginClientCommand command;

    private ChannelDataLoader mockLoader;

    @BeforeEach
    public void setUp() {
        mockClientResources = mock(ClientResources.class);
        SocketChannel mockSocket = mock(SocketChannel.class);
        when(mockClientResources.getClientChannel()).thenReturn(mockSocket);

        mockLoader = mock(ChannelDataLoader.class);
        ChannelDataWriter channelDataWriter =  new ChannelDataWriter(mockSocket);
        command = new LoginClientCommand("input", mockClientResources, channelDataWriter, mockLoader);
    }

    @Test
    void testExecuteWithValidArguments() {
        String userInput = LOGIN_COMMAND + EXAMPLE_USER_EMAIL + EXAMPLE_USER_PASS;
        command.setUserInput(userInput);

        String result = commandExecute();

        assertNotNull(result, "Successful command execution is expected!");
        verify(mockClientResources).setLoggedClientEmail(EXAMPLE_USER_EMAIL);
        verify(mockClientResources).setClientLogged(true);
    }

    @Test
    void testExecuteWithInvalidArguments() {
        String userInput = LOGIN_COMMAND + EXAMPLE_USER_EMAIL;
        command.setUserInput(userInput);

        String result = commandExecute();

        assertNotNull(result, "Unsuccessful command execution is expected!");
        verify(mockClientResources, never()).setLoggedClientEmail(anyString());
        verify(mockClientResources, never()).setClientLogged(anyBoolean());
    }

    @Test
    void testSetUserInputWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> command.setUserInput(null),
                "IllegalArgumentException is expected to be thrown when trying to set the user input to null");
    }

    private String commandExecute() {
        String result = null;
        try {
            when(mockLoader.loadData()).thenReturn("text");
            result = command.execute();
        } catch (DeserializationDataException | ChannelCommunicationException e) {
            fail("The login operation cannot be executed!");
        }

        return result;
    }
}

package bg.sofia.uni.fmi.mjt.command;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommandInvoker;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandExecutorTest {

    private ClientCommandInvoker clientInvoker;
    private CommandExecutor commandExecutor;

    @BeforeEach
    void setUp() {
        clientInvoker = mock(ClientCommandInvoker.class);
        ClientResources clientResources = mock(ClientResources.class);

        commandExecutor = new CommandExecutor(clientResources, clientInvoker);
    }

    @Test
    void testExecuteSimpleCommand() throws ChannelCommunicationException {
        when(clientInvoker.executeCommand("login", new String[]{"user", "pass"})).thenReturn("Logged in");

        String result = commandExecutor.execute("login user pass");

        assertEquals("Logged in", result);
        verify(clientInvoker).executeCommand("login", new String[]{"user", "pass"});
    }

    @Test
    void testExecutePlayCommandWithSpacesInSongName() throws ChannelCommunicationException {
        when(clientInvoker.executeCommand("play", new String[]{"ImagineDragons"})).thenReturn("Playing");

        String result = commandExecutor.execute("play Imagine Dragons");

        assertEquals("Playing", result);
        verify(clientInvoker).executeCommand("play", new String[]{"ImagineDragons"});
    }

    @Test
    void testExecuteAddSongToCommand() throws ChannelCommunicationException {
        when(clientInvoker.executeCommand("add-song-to", new String[]{"Chill", "OceanEyes"})).thenReturn("Added");

        String result = commandExecutor.execute("add-song-to Chill Ocean Eyes");

        assertEquals("Added", result);
        verify(clientInvoker).executeCommand("add-song-to", new String[]{"Chill", "OceanEyes"});
    }

    @Test
    void testExecuteAddSongToCommandWithInvalidArgs() throws ChannelCommunicationException {
        when(clientInvoker.executeCommand("add-song-to", new String[]{"Chill"})).thenReturn("Not added!");

        String result = commandExecutor.execute("add-song-to Chill");

        assertEquals("Not added!", result);
        verify(clientInvoker).executeCommand("add-song-to", new String[]{"Chill"});
    }

    @Test
    void testExecuteRemoveSongFromCommand() throws ChannelCommunicationException {
        when(clientInvoker.executeCommand("remove-song-from", new String[]{"Chill", "HeatWaves"})).thenReturn("Removed");

        String result = commandExecutor.execute("remove-song-from Chill Heat Waves");

        assertEquals("Removed", result);
        verify(clientInvoker).executeCommand("remove-song-from", new String[]{"Chill", "HeatWaves"});
    }

    @Test
    void testExecuteNullInputReturnsErrorMessage() throws ChannelCommunicationException {
        String result = commandExecutor.execute(null);

        assertEquals("You must enter a command!", result);
    }

}

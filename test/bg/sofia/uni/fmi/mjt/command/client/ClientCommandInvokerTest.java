//package bg.sofia.uni.fmi.mjt.command.client;
//
//import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
//import bg.sofia.uni.fmi.mjt.spotify.command.commands.client.ClientCommandInvoker;
//import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
//import org.junit.jupiter.api.Test;
//
//import java.nio.channels.SocketChannel;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class ClientCommandInvokerTest {
//
//    @Test
//    void testExecuteValidCommandReturnsExpectedResult() throws ChannelCommunicationException {
//        SocketChannel mockChannel = mock(SocketChannel.class);
//        ClientResources mockResources = mock(ClientResources.class);
//        when(mockResources.getClientChannel()).thenReturn(mockChannel);
//
//        ClientCommandInvoker invoker = new ClientCommandInvoker(mockResources);
//
//        String result = invoker.executeCommand("show-all-commands", new String[]{});
//
//        assertTrue(result.contains("Available Commands"));
//    }
//
//}

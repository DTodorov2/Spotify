package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class SpotifyClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    private final ClientResources clientResources;

    private final CommandExecutor commandExecutor;

    public SpotifyClient() {

        clientResources = new ClientResources();
        commandExecutor = new CommandExecutor(clientResources);
    }

    private void start() {
        try (SocketChannel clientChannel = SocketChannel.open()) {
            clientChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            this.clientResources.setClientChannel(clientChannel);
            runClientLoop();
        } catch (IOException e) {
            System.out.println("Unable to connect to the server!");
        }
    }

    // tuk dobavih break-ove i zamestih isClientRunning s true
    private void runClientLoop() {
        //boolean isClientRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter command: ");
            String input = scanner.nextLine();

            try {
                String response = commandExecutor.execute(input);
                System.out.println(response);

            } catch (ChannelCommunicationException e) {
                System.out.println("Unable to connect with the server! " + e.getMessage() +
                        System.lineSeparator() + " Exiting...");

                if (clientResources.isClientLogged()) {
                    //ako stream-va muzika da spira
                }
                break;
            }
        }
    }
}



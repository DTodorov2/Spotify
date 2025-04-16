package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;
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
    private static final String DISCONNECT = "disconnect";

    private static final String EXIT_MESSAGE = "Exiting...";

    private final ClientResources clientResources;

    private final CommandExecutor commandExecutor;

    public SpotifyClient() {

        clientResources = new ClientResources();
        commandExecutor = new CommandExecutor(clientResources);
    }

    public void start() {
        try (SocketChannel clientChannel = SocketChannel.open()) {
            clientChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server!");
            this.clientResources.setClientChannel(clientChannel);

            runClientLoop();

            clientResources.getExecutor().shutdown();
        } catch (IOException e) {
            System.out.println("Unable to connect to the server!");
        }
    }

    private void runClientLoop() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter command: ");
            String input = scanner.nextLine();
            try {
                String response = commandExecutor.execute(input);
                System.out.println(response);
                if (input.equals(DISCONNECT)) {
                    System.out.println(EXIT_MESSAGE);
                    break;
                }

            } catch (ChannelCommunicationException e) {
                System.out.println("Unable to connect with the server! " + e.getMessage() +
                        System.lineSeparator() + EXIT_MESSAGE);

                if (clientResources.isClientLogged()) {
                    clientResources.setClientLogged(false);
                    AudioClient audioClient = clientResources.getAudioClient();
                    if (audioClient != null) {
                        audioClient.stopStreaming();
                    }
                }
                break;
            }
        }
    }
}



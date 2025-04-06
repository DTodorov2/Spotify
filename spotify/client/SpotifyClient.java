package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

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

    private void runClientLoop() {
        boolean isClientRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isClientRunning) {
            System.out.println("Enter command: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("disconnect")) {
                isClientRunning = false;
                //tuka trqq proverqm, ako puska muzika da q spiram
                clientResources.setClientLogged(false);
                continue;
            }
            try {
                String response = commandExecutor.execute(input);
            } catch (ChannelCommunicationException e) {
                System.out.println("Unable to connect with the server! " + e.getMessage() +
                        System.lineSeparator() + " Exiting...");

                isClientRunning = false;
                if (clientResources.isClientLogged()) {
                    //ako stream-va muziak da spira
                }
            }
        }
    }
}



import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class SpotifyClient {

    private final String serverHost;
    private final int serverPort;

    private final ClientResources clientResources;

    private final CommandExecutor commandExecutor;

    public SpotifyClient(String serverHost, int serverPort) {
        validateString(serverHost, "server host");
        validateInt(serverPort, "server port");

        this.serverHost = serverHost;
        this.serverPort = serverPort;

        clientResources = new ClientResources();
        commandExecutor = new CommandExecutor(clientResources);
    }

    private void start() {
        try (SocketChannel clientChannel = SocketChannel.open()) {
            clientChannel.connect(new InetSocketAddress(serverHost, serverPort));
            this.clientResources.setClientChannel(clientChannel);
            runClientLoop();
        } catch (IOException e) {
            System.out.println("Unable to connect to the server!");
        }
    }

    private void runClientLoop() {
        boolean isClientRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isClientRunning) {
            System.out.println("Enter command: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("disconnect")) {
                isClientRunning = false;
                //tuka trqq proverqm, ako puska muzika da q spiram
                clientResources.setClientLogged(false);
                continue;
            }
            try {
                String response = commandExecutor.execute(input);
            } catch (ChannelCommunicationException e) {
                System.out.println("Unable to connect with the server! " + e.getMessage() +
                        System.lineSeparator() + " Exiting...");

                isClientRunning = false;
                if (clientResources.isClientLogged()) {
                    //ako stream-va muziak da spira
                }
            }
        }
    }
}



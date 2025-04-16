package bg.sofia.uni.fmi.mjt.spotify.launch;

import bg.sofia.uni.fmi.mjt.spotify.server.SpotifyServer;

import java.util.Scanner;

public class StartServer {
    private static final String STOP_COMMAND = "stop";
    public static void main(String[] args) {

        SpotifyServer spotifyServer = new SpotifyServer();
        new Thread(spotifyServer::start).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase(STOP_COMMAND)) {
                spotifyServer.stop();
                System.out.println("The server was shutdown!");
                break;
            }
        }
    }
}

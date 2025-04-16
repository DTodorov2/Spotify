package bg.sofia.uni.fmi.mjt.spotify.launch;

import bg.sofia.uni.fmi.mjt.spotify.server.SpotifyServer;

public class StartServer {
    public static void main(String[] args) {
        new SpotifyServer().start();
    }
}

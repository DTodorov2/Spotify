package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;

import java.nio.ByteBuffer;

public class ClientHandler {
    private static final int BUFFER_SIZE = 1024;
    private final ByteBuffer buffer;
    //private final CommandExecutor commandExecutor;

    public ClientHandler(UsersRepository usersRepository, SongsRepository songsRepository) {
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);

    }
}

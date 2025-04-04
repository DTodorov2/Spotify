package bg.sofia.uni.fmi.mjt.spotify.server.resources;

import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class ServerResources {

    private final UsersRepository usersRepository;
    private final SongsRepository songsRepository;
    private SelectionKey key;

    public ServerResources() {
        usersRepository = new UsersRepository();
        songsRepository = new SongsRepository();
        key = null;
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public SongsRepository getSongsRepository() {
        return songsRepository;
    }

    public SelectionKey getSelectionKey() {
        return key;
    }

    public void setSelectionKey(SelectionKey key) {
        checkArgumentNotNull(key, "new selection key");
        this.key = key;
    }

}

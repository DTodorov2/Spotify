package bg.sofia.uni.fmi.mjt.spotify.server.resources;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.song.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.user.UsersRepository;

import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class ServerResources {

    private final UsersRepository usersRepository;
    private final SongsRepository songsRepository;
    private SelectionKey key;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

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

    public boolean isUserLogged() {
        if (key == null) {
            return false;
        }

        String userId = (String) key.attachment();
        return userId != null;
    }

    public User getLoggedUser() {
        if (!isUserLogged()) {
            return null;
        }

        String userId = (String) key.attachment();
        return usersRepository.getById(userId);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

}

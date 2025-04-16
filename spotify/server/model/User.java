package bg.sofia.uni.fmi.mjt.spotify.server.model;

import bg.sofia.uni.fmi.mjt.spotify.server.audio.AudioServer;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist.PlaylistsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.security.SHA256;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class User extends IdentifiableModel {

    private final String email;
    private final String password;
    private transient boolean loggedIn;
    private final PlaylistsRepository playlistsRepository;
    private transient AudioServer audioServer;

    public User(String email, String password, PlaylistsRepository playlistsRepository) {
        validateString(email, "email");
        validateString(password, "password");
        checkArgumentNotNull(playlistsRepository, "playlist repository");

        this.email = email;
        this.password = SHA256.hash(password);
        this.loggedIn = false;
        this.playlistsRepository = playlistsRepository;
        audioServer = null;
    }

    public void setAudioServer(AudioServer audioServer) {
        this.audioServer = audioServer;
    }

    public AudioServer getAudioServer() {
        return audioServer;
    }

    public boolean addPlaylist(String playlistName) {
        validateString(playlistName, "name of the new playlist");
        return playlistsRepository.add(new Playlist(playlistName, new HashSet<>()));
    }

    public boolean removePlaylist(String playlistName) {
        validateString(playlistName, "name of the playlist to remove");
        return playlistsRepository.remove(playlistName);
    }

    public PlaylistsRepository getPlaylistsRepository() {
        return playlistsRepository;
    }

    @Override
    public String getId() {
        return email;
    }

    @Override
    public boolean isDuplicate(Map<String, ? extends IdentifiableModel> users) {
        return users.containsKey(email);
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

}

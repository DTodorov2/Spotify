package bg.sofia.uni.fmi.mjt.spotify.server.model;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.AddOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SuchPlaylistAlreadyExistsException;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class Playlist extends BaseModel<String> implements Identifiable {

    private final String name;
    private final Set<Song> songList;

    public Playlist(String name, Set<Song> songList) {
        validateString(name, "playlist name");
        checkArgumentNotNull(songList, "list of songs");

        this.name = name;
        this.songList = songList;
    }

    public void addSong(Song song) {
        checkArgumentNotNull(song, "song");
        songList.add(song);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(name, playlist.name) && Objects.equals(songList, playlist.songList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, songList);
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void checkForDuplicate(Map<String, ? extends Identifiable> playlists) throws AddOperationException {
        if (playlists.containsKey(name)) {
            throw new SuchPlaylistAlreadyExistsException("A playlist with name: " + name + " already exists!");
        }
    }
}

package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class Playlist extends IdentifiableModel {

    private final String name;
    private final Set<Song> songList;

    public Playlist(String name, Set<Song> songList) {
        validateString(name, "playlist name");
        checkArgumentNotNull(songList, "list of songs");

        this.name = name;
        this.songList = songList;
    }

    public boolean addSong(Song song) {
        checkArgumentNotNull(song, "new song");
        return songList.add(song);
    }

    public boolean removeSong(Song song) {
        checkArgumentNotNull(song, "song to remove");
        return songList.remove(song);
    }

    public String display() {
        StringBuilder sb = new StringBuilder();
        sb.append("Playlist: ").append(name).append(System.lineSeparator());

        if (songList.isEmpty()) {
            sb.append("  No songs in this playlist.");
        } else {
            int index = 1;
            for (Song song : songList) {
                sb.append(index++)
                        .append(". ")
                        .append(song.toString());
            }
        }

        return sb.toString();
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
    public boolean isDuplicate(Map<String, ? extends IdentifiableModel> playlists) {
        return playlists.containsKey(name);
    }
}

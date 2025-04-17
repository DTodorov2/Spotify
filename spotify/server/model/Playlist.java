package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class Playlist extends IdentifiableModel {

    private final String name;
    private final Set<Song> songSet;

    public Playlist(String name, Set<Song> songList) {
        validateString(name, "playlist name");
        checkArgumentNotNull(songList, "list of songs");

        this.name = name;
        this.songSet = songList;
    }

    public boolean addSong(Song song) {
        checkArgumentNotNull(song, "new song");
        return songSet.add(song);
    }

    public boolean removeSong(Song song) {
        checkArgumentNotNull(song, "song to remove");
        return songSet.remove(song);
    }

    public Set<Song> getAllSongs() {
        return songSet;
    }

    public static String displayListOfSongs(List<Song> songList) {
        checkArgumentNotNull(songList, "list of songs");

        StringBuilder sb = new StringBuilder();
        sb.append("Songs: ")
                .append(System.lineSeparator());
        int index = 1;
        for (Song song : songList) {
            sb.append(index)
                    .append(". ")
                    .append(song.toString());
            if (index != songList.size()) {
                sb.append(System.lineSeparator());
            }
            index++;
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(name, playlist.name) && Objects.equals(songSet, playlist.songSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, songSet);
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

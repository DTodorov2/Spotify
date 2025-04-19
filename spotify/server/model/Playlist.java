package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class Playlist extends IdentifiableModel {

    private final String name;
    private final Map<String, Song> songs;

    public Playlist(String name, Map<String, Song> songs) {
        validateString(name, "playlist name");
        checkArgumentNotNull(songs, "list of songs");

        this.name = name;
        this.songs = songs;
    }

    public boolean addSong(Song song) {
        checkArgumentNotNull(song, "new song");
        return songs.put(song.getTitle(), song) == null;
    }

    public boolean removeSong(Song song) {
        checkArgumentNotNull(song, "song to remove");
        return songs.remove(song.getTitle()) != null;
    }

    public Map<String, Song> getAllSongs() {
        return songs;
    }

    public boolean contains(String songName) {
        validateString(songName, "title of the song");

        return songs.containsKey(songName);
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
        return Objects.equals(name, playlist.name) && Objects.equals(songs, playlist.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, songs);
    }

    public void incrementPlayingsCountOf(String songName) {
        validateString(songName, "song name");

        songs.get(songName).incrementPlayCount();
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

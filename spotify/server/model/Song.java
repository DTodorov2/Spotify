package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.util.Map;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class Song extends IdentifiableModel {

    private final String title;
    private final String artist;
    private int playCount;

    public Song(String title, String artist, int playCount) {
        validateString(title, "title");
        validateString(artist, "artist");
        validateInt(playCount, "play count");

        this.title = title;
        this.artist = artist;
        this.playCount = playCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(title, song.title) && Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }

    public int getPlayCount() {
        return playCount;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Singer: " + artist;
    }

    public void incrementPlayCount() {
        playCount++;
    }

    @Override
    public String getId() {
        return title;
    }

    @Override
    public boolean isDuplicate(Map<String, ? extends IdentifiableModel> songs) {
        return songs.containsKey(title);
    }
}

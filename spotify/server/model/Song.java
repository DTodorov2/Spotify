package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.nio.file.Path;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateInt;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class Song {

    private final String title;
    private final String artist;
    private int playCount;

    public Song(int id, String title, String artist, Path filePath, int playCount) {
        validateInt(id, "id");
        validateString(title, "title");
        validateString(artist, "artist");
        validateFilePath(filePath);
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

}

package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.spotify.server.model.ModelValidator.validateFilePath;
import static bg.sofia.uni.fmi.mjt.spotify.server.model.ModelValidator.validateInt;
import static bg.sofia.uni.fmi.mjt.spotify.server.model.ModelValidator.validateString;

public class Song extends BaseModel<Integer> {

    private final int id;
    private final String title;
    private final String artist;
    private final String filePath;
    private final int playCount;

    public Song(int id, String title, String artist, String filePath, int playCount) {
        validateInt(id, "id");
        validateString(title, "title");
        validateString(artist, "artist");
        validateFilePath(filePath);
        validateInt(playCount, "play count");

        this.id = id;
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.playCount = playCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id == song.id && Objects.equals(title, song.title) && Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artist);
    }

    @Override
    public Integer getId() {
        return id;
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
}

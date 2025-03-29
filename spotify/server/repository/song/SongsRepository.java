package bg.sofia.uni.fmi.mjt.spotify.server.repository.song;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.comparator.SongPlayCountComparator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SongsRepository implements ReadOnlyRepository {

    List<Song> songs;

    public SongsRepository(Path filePath) {
        //trqq go napravq da chete ot faila
        songs = new ArrayList<>();
    }

    @Override
    public List<Song> searchByWords(String... words) {
        return songs.stream()
                .filter(song -> containsAnyWord(song, words))
                .collect(Collectors.toList());
    }

    private boolean containsAnyWord(Song song, String... words) {
        return Arrays.stream(words)
                .anyMatch(word ->
                        song.getTitle().toLowerCase().contains(word.toLowerCase()) ||
                                song.getArtist().toLowerCase().contains(word.toLowerCase())
                );
    }

    @Override
    public List<Song> getTopStatistics(int num) {
        return songs.stream()
                .sorted(new SongPlayCountComparator())
                .limit(num)
                .collect(Collectors.toList());
    }

    @Override
    public void loadDataFrom(Path filePath) {

    }

    @Override
    public void saveDataTo(Path filePath) {

    }
}

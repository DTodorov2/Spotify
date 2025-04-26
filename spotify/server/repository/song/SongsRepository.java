package bg.sofia.uni.fmi.mjt.spotify.server.repository.song;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SavingDataToFileException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;

import bg.sofia.uni.fmi.mjt.spotify.server.repository.StringKeyBaseRepositoryImpl;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.comparator.SongPlayCountComparator;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class SongsRepository extends StringKeyBaseRepositoryImpl<Song> {

    public SongsRepository() {
        super();
    }

    public List<Song> searchByWords(String... words) {
        return entities.values().stream()
                .filter(song -> containsAllWords(song, words))
                .collect(Collectors.toList());
    }

    private boolean containsAllWords(Song song, String... words) {
        return Arrays.stream(words)
                .allMatch(word ->
                        song.getTitle().contains(word) ||
                                song.getArtist().contains(word)
                );
    }

    public List<Song> getTopStatistics(int num) {
        return entities.values().stream()
                .sorted(new SongPlayCountComparator())
                .limit(num)
                .collect(Collectors.toList());
    }

    public Song getSongByName(String songName) {
        validateString(songName, "song name");
        return entities.values().stream()
                .filter(song -> song.getTitle().replace(" ", "").equals(songName))
                .findFirst()
                .orElse(null);
    }

    public void saveData(Path filePath) throws SavingDataToFileException {
        validateFilePath(filePath);
        super.saveData(filePath, entities);
    }

    @Override
    public void loadData(Path filePath) throws LoadingDataFromFileException {
        validateFilePath(filePath);
        setMap(loadDataWithCurrentValueClass(filePath, Song.class));
    }

}

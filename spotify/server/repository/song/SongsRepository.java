package bg.sofia.uni.fmi.mjt.spotify.server.repository.song;

import bg.sofia.uni.fmi.mjt.spotify.data.DataDeserializer;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.TextFormatter;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.json.JsonFormatter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.comparator.SongPlayCountComparator;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable.JsonSerializableRepository;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;

public class SongsRepository extends JsonSerializableRepository<List<Song>> implements ReadOnlyRepository {

    List<Song> songs;

    public SongsRepository() {
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
    public void loadDataFrom(Path filePath) throws LoadingDataFromFileException {
        validateFilePath(filePath);

        TextFormatter jsonFormatter = new JsonFormatter();
        DataDeserializer dataDeserializer = new FileDataLoader(filePath);

        try {
            String listInJsonFormat = dataDeserializer.loadData();
            songs = jsonFormatter.loadDataFromFormat(listInJsonFormat, Song.class);
        } catch (DeserializationException | FileNotFoundException e) {
            throw new LoadingDataFromFileException("The data from file: " + filePath + "cannot be loaded!", e);
        }
    }
}

package bg.sofia.uni.fmi.mjt.spotify.server.repository.song;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable.SerializableRepository;

import java.util.List;

public interface ReadOnlyRepository extends SerializableRepository<List<Song>> {

    List<Song> searchByWords(String... words);

    List<Song> getTopStatistics(int num);
}

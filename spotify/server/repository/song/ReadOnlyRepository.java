package bg.sofia.uni.fmi.mjt.spotify.server.repository.song;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import java.util.List;

public interface ReadOnlyRepository {

    List<Song> searchByWords(String... words);

    List<Song> getTopStatistics(int num);
}

package bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable;

import java.nio.file.Path;

public interface SerializableRepository<T> {
    void loadDataFrom(Path filePath);

    void saveDataTo(Path filePath);
}

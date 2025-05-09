package bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SavingDataToFileException;

import java.nio.file.Path;

public interface SerializableRepository<T> {

    void loadData(Path filePath) throws LoadingDataFromFileException;

    void saveData(Path filePath, T collection) throws SavingDataToFileException;

}

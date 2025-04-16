package bg.sofia.uni.fmi.mjt.spotify.server.repository.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SavingDataToFileException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.StringKeyBaseRepositoryImpl;
import java.nio.file.Path;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;

public class PlaylistsRepository extends StringKeyBaseRepositoryImpl<Playlist> {

    public PlaylistsRepository() {
        super();
    }

    @Override
    public void loadData(Path filePath) throws LoadingDataFromFileException {
        validateFilePath(filePath);
        loadDataWithCurrentValueClass(filePath, Playlist.class);
    }

    public void saveData(Path filePath) throws SavingDataToFileException {
        validateFilePath(filePath);
        super.saveData(filePath, entities);
    }

}

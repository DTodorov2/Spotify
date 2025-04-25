package bg.sofia.uni.fmi.mjt.spotify.server.repository.user;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SavingDataToFileException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.StringKeyBaseRepositoryImpl;
import java.nio.file.Path;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;

public class UsersRepository extends StringKeyBaseRepositoryImpl<User> {

    public UsersRepository() {
        super();
    }

    @Override
    public void loadData(Path filePath) throws LoadingDataFromFileException {
        validateFilePath(filePath);
        setMap(loadDataWithCurrentValueClass(filePath, User.class));
    }

    public void saveData(Path filePath) throws SavingDataToFileException {
        validateFilePath(filePath);
        super.saveData(filePath, entities);
    }

}

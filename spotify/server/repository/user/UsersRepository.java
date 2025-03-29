package bg.sofia.uni.fmi.mjt.spotify.server.repository.user;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.BaseRepositoryImpl;
import java.nio.file.Path;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;

public class UsersRepository extends BaseRepositoryImpl<User> {

    public UsersRepository() {
        super();
    }

    @Override
    public void loadDataFrom(Path filePath) throws LoadingDataFromFileException {
        validateFilePath(filePath);
        setMap(loadDataWithCurrentClass(filePath, User.class));
    }
}

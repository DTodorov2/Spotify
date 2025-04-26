package bg.sofia.uni.fmi.mjt.server.repository.stub;

import bg.sofia.uni.fmi.mjt.spotify.server.model.IdentifiableModel;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.StringKeyBaseRepositoryImpl;

import java.nio.file.Path;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;

public class StringKeyBaseRepositoryImplStub extends StringKeyBaseRepositoryImpl<IdentifiableModel> {

    @Override
    public void loadData(Path filePath) {
        validateFilePath(filePath);
    }

}

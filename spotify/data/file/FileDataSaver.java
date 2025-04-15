package bg.sofia.uni.fmi.mjt.spotify.data.file;

import bg.sofia.uni.fmi.mjt.spotify.data.DataSerializer;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.FileCreationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

import static bg.sofia.uni.fmi.mjt.spotify.data.file.FileManager.createFileIfItDoesNotExist;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileDataSaver implements DataSerializer {

    private final Path pathFile;

    public FileDataSaver(Path pathFile) {
        validateFilePath(pathFile);

        this.pathFile = pathFile;
    }

    @Override
    public void saveData(String data, boolean toAppend) throws SerializationDataException {
        validateString(data, "data to save to a file");
        String pathFileString = pathFile.toString();
        try {
            createFileIfItDoesNotExist(pathFile);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathFileString, toAppend))) {
                writer.write(data);
            }
        } catch (IOException | FileCreationException e) {
            String exceptionMessage = "The given data could not be serialized in file with path: " + pathFileString;
            throw new SerializationDataException(exceptionMessage, e);
        }
    }
}

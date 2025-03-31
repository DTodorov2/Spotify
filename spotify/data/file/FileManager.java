package bg.sofia.uni.fmi.mjt.spotify.data.file;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.FileCreationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;

public class FileManager {

    public static void createFileIfItDoesNotExist(Path pathFile) throws FileCreationException {
        validateFilePath(pathFile);
        try {
            Files.createDirectories(pathFile.getParent());
            if (Files.notExists(pathFile)) {
                Files.createFile(pathFile);
            }
        } catch (IOException e) {
            String fileName = pathFile.getFileName().toString();
            throw new FileCreationException("The file with path: " + fileName + " cannot be created", e);
        }
    }

    public static void checkFileExists(Path filePath) throws FileNotFoundException {
        if (Files.notExists(filePath)) {
            String fileName = filePath.getFileName().toString();
            throw new FileNotFoundException("A file with this file path: " + fileName + " is not found!");
        }
    }

}

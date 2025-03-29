package bg.sofia.uni.fmi.mjt.spotify.data.file;

import bg.sofia.uni.fmi.mjt.spotify.data.DataDeserializer;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.spotify.data.file.FileManager.checkFileExists;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;

public class FileDataLoader implements DataDeserializer {

    private final Path filePath;

    public FileDataLoader(Path filePath) {
        validateFilePath(filePath);

        this.filePath = filePath;
    }

    @Override
    public String loadData() throws FileNotFoundException, DeserializationException {
        checkFileExists(filePath);

        try (Stream<String> lines = Files.lines(filePath)) {
            return lines.collect(
                    Collectors.joining(System.lineSeparator())
            );
        } catch (IOException e) {
            String fileName = filePath.getFileName().toString();
            throw new DeserializationException("The data from file " + fileName + " cannot be deserialized!");
        }
    }

}

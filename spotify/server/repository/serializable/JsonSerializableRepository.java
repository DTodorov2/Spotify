package bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable;

import bg.sofia.uni.fmi.mjt.spotify.data.DataSerializer;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataSaver;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.TextFormatter;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.json.JsonFormatter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SavingDataToFileException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

import java.nio.file.Path;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;

public abstract class JsonSerializableRepository<T> implements SerializableRepository<T> {

    public void saveData(Path filePath, T collection) throws SavingDataToFileException {
        validateFilePath(filePath);

        TextFormatter jsonFormatter = new JsonFormatter();
        DataSerializer dataSerializer = new FileDataSaver(filePath);

        try {
            String dataInJsonFormat = jsonFormatter.convert(collection);
            dataSerializer.saveData(dataInJsonFormat, false);
        } catch (SerializationDataException e) {
            throw new SavingDataToFileException("The provided data cannot be saved in file: " + filePath + "!", e);
        }
    }

}

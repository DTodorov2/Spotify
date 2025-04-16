package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.data.DataDeserializer;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.TextFormatter;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.json.JsonFormatter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.IdentifiableModel;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable.JsonSerializableRepository;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public abstract class StringKeyBaseRepositoryImpl<T extends IdentifiableModel>
                                        extends JsonSerializableRepository<Map<String, T>>
                                        implements BaseRepositoryAPI<T> {

    protected Map<String, T> entities;

    public StringKeyBaseRepositoryImpl() {
        entities = new HashMap<>();
    }

    @Override
    public boolean add(T entity) {
        checkArgumentNotNull(entity, "entity to add");
        String entityId = entity.getId();
        if (entity.isDuplicate(entities)) {
            return false;
        }
        entities.put(entityId, entity);
        return true;
    }

    @Override
    public boolean remove(String id) {
        validateString(id, "id of the entity to remove");

        return entities.remove(id) != null;
    }

    @Override
    public T getById(String id) {
        validateString(id, "id of the entity");

        return entities.get(id);
    }

    @Override
    public Map<String, T> getMap() {
        return entities;
    }

    protected Map<String, T> loadDataWithCurrentValueClass(Path filePath, Class<T> valueClass)
            throws LoadingDataFromFileException {
        validateFilePath(filePath);

        TextFormatter jsonFormatter = new JsonFormatter();
        DataDeserializer dataDeserializer = new FileDataLoader(filePath);

        try {
            String mapInJsonFormat = dataDeserializer.loadData();
            return jsonFormatter.loadDataFromFormat(mapInJsonFormat, String.class, valueClass);
        } catch (DeserializationDataException | FileNotFoundException e) {
            throw new LoadingDataFromFileException("The data from file: " + filePath + "cannot be loaded!", e);
        }
    }

    protected void setMap(Map<String, T> newEntities) {
        checkArgumentNotNull(newEntities, "new map of entities");

        this.entities = newEntities;
    }
}

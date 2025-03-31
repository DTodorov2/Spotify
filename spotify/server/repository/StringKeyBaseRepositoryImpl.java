package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.data.DataDeserializer;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.TextFormatter;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.json.JsonFormatter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.AddOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ElementNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.BaseModel;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Identifiable;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable.JsonSerializableRepository;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public abstract class StringKeyBaseRepositoryImpl<T extends BaseModel<String> & Identifiable>
                                        extends JsonSerializableRepository<Map<String, T>>
                                        implements BaseRepository<T> {

    private Map<String, T> entities;

    public StringKeyBaseRepositoryImpl() {
        entities = new HashMap<>();
    }

    @Override
    public void add(T entity) throws AddOperationException {
        checkArgumentNotNull(entity, "entity");
        String entityId = entity.getId();
        entity.checkForDuplicate(entities);
        entities.put(entityId, entity);
    }

    @Override
    public T getById(String id) throws ElementNotFoundException {
        validateString(id, "id of the entity");
        if (!entities.containsKey(id)) {
            throw new ElementNotFoundException("Such entity does not exist!");
        }

        return entities.get(id);
    }

    @Override
    public List<T> getAll() {
        return List.copyOf(entities.values());
    }

    protected Map<String, T> loadDataWithCurrentValueClass(Path filePath, Class<T> valueClass)
            throws LoadingDataFromFileException {
        validateFilePath(filePath);

        TextFormatter jsonFormatter = new JsonFormatter();
        DataDeserializer dataDeserializer = new FileDataLoader(filePath);

        try {
            String mapInJsonFormat = dataDeserializer.loadData();
            return jsonFormatter.loadDataFromFormat(mapInJsonFormat, String.class, valueClass);
        } catch (DeserializationException | FileNotFoundException e) {
            throw new LoadingDataFromFileException("The data from file: " + filePath + "cannot be loaded!", e);
        }
    }

    protected void setMap(Map<String, T> newEntities) {
        checkArgumentNotNull(newEntities, "new map of entities");

        this.entities = newEntities;
    }

}

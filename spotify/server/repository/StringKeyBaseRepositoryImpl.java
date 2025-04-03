package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.data.DataDeserializer;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.TextFormatter;
import bg.sofia.uni.fmi.mjt.spotify.data.serializer.json.JsonFormatter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.IdentifiableModel;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable.JsonSerializableRepository;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateFilePath;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public abstract class StringKeyBaseRepositoryImpl<T extends IdentifiableModel<String>>
                                        extends JsonSerializableRepository<Map<String, T>>
                                        implements BaseRepository<T> {

    private Map<String, T> entities;

    public StringKeyBaseRepositoryImpl() {
        entities = new HashMap<>();
    }

    @Override
    public boolean add(T entity) {
        checkArgumentNotNull(entity, "entity");
        String entityId = entity.getId();
        if (entity.isDuplicate(entities)) {
            return false;
        }
        entities.put(entityId, entity);
        return true;
    }

    @Override
    public T getById(String id) {
        validateString(id, "id of the entity");
        // ne sum siguren, che trqq da q imam taq proverka tuka
//        if (!entities.containsKey(id)) {
//            throw new ElementNotFoundException("Such entity does not exist!");
//        }

        return entities.get(id);
    }

    @Override
    public List<T> getAll() {
        return List.copyOf(entities.values());
    }

    @Override
    public Map<String, T> getMap() {
        return entities;
    }

//    @Override
//    public boolean contains(T entity) {
//        checkArgumentNotNull(entity, "entity to check");
//        String entityId = entity.getId();
//        if (entities.containsKey(entityId)) {
//            return entities.get(entityId).equals(entity);
//        }
//        return false;
//    }

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

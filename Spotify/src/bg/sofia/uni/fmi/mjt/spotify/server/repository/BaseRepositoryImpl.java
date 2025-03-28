package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.AddOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ElementNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.BaseModel;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Identifiable;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.serializable.SerializableRepository;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRepositoryImpl<T extends BaseModel<String> & Identifiable>
                                        implements StringKeyBaseRepository<T>, SerializableRepository<Map<String, T>> {

    public Map<String, T> entities;

    public BaseRepositoryImpl() {
        entities = new HashMap<>();
    }

    @Override
    public void add(T entity) throws AddOperationException {
        String entityId = entity.getId();
        entity.checkForDuplicate(entities);
        entities.put(entityId, entity);
    }

    @Override
    public T getById(String id) throws ElementNotFoundException {
        if (!entities.containsKey(id)) {
            throw new ElementNotFoundException("Such entity does not exist!");
        }

        return entities.get(id);
    }

    @Override
    public List<T> getAll() {
        return List.copyOf(entities.values());
    }

    @Override
    public void loadDataFrom(Path filePath) {

    }

    @Override
    public void saveDataTo(Path filePath) {

    }

}

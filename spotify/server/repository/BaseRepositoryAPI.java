package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ElementNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.IdentifiableModel;

import java.util.List;
import java.util.Map;

public interface BaseRepositoryAPI<T extends IdentifiableModel> {

    boolean add(T entity);

    T getById(String id) throws ElementNotFoundException;

    List<T> getAll();

    Map<String, T> getMap();

//    boolean contains(T entity);
}

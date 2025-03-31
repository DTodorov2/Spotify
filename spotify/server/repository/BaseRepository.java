package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.AddOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ElementNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.BaseModel;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Identifiable;

import java.util.List;

public interface BaseRepository<T extends BaseModel<String> & Identifiable> {

    void add(T entity) throws AddOperationException;

    T getById(String id) throws ElementNotFoundException;

    List<T> getAll();
}

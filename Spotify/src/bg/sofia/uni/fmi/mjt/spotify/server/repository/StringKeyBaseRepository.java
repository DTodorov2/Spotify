package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.exception.AddOperationException;
import bg.sofia.uni.fmi.mjt.spotify.server.exception.ElementNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.BaseModel;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Identifiable;

import java.util.List;

public interface StringKeyBaseRepository<T extends BaseModel<String> & Identifiable> {

    void add(T entity) throws AddOperationException;

    T getById(String id) throws ElementNotFoundException;

    List<T> getAll();
}

package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.IdentifiableModel;

import java.util.Map;

public interface BaseRepositoryAPI<T extends IdentifiableModel> {

    boolean add(T entity);

    boolean remove(String entity);

    T getById(String id);

    Map<String, T> getMap();

}

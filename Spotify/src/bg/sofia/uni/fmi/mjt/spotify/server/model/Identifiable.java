package bg.sofia.uni.fmi.mjt.spotify.server.model;

import bg.sofia.uni.fmi.mjt.spotify.server.exception.AddOperationException;

import java.util.Map;

public interface Identifiable {
    public abstract void checkForDuplicate(Map<String, ? extends Identifiable> entities) throws AddOperationException;
}

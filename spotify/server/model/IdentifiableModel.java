package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.util.Map;

public abstract class IdentifiableModel<T> {
    public abstract T getId();

    public abstract boolean isDuplicate(Map<String, ? extends IdentifiableModel<T>> entities);
}

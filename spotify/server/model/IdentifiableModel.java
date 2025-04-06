package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.util.Map;

public abstract class IdentifiableModel {
    public abstract String getId();

    public abstract boolean isDuplicate(Map<String, ? extends IdentifiableModel> entities);
}

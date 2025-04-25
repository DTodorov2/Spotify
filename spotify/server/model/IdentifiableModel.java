package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.util.Map;

public interface IdentifiableModel {

    String getId();

    boolean isDuplicate(Map<String, ? extends IdentifiableModel> entities);

}

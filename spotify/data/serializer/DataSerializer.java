package bg.sofia.uni.fmi.mjt.spotify.data.serializer;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationException;

public interface DataSerializer {
    void saveData(String data, boolean toAppend) throws SerializationException;
}

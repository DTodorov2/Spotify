package bg.sofia.uni.fmi.mjt.spotify.data;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

public interface DataSerializer {

    void saveData(String data, boolean toAppend) throws SerializationDataException;

}

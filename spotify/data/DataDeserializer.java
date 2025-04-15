package bg.sofia.uni.fmi.mjt.spotify.data;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;

import java.io.FileNotFoundException;

public interface DataDeserializer {

    String loadData() throws FileNotFoundException, DeserializationDataException;

}

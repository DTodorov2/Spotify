package bg.sofia.uni.fmi.mjt.spotify.data.serializer;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationException;

import java.io.FileNotFoundException;

public interface DataDeserializer {

    String loadData(String data) throws FileNotFoundException, DeserializationException;

}

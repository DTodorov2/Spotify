package bg.sofia.uni.fmi.mjt.spotify.data.serializer;

import java.util.Map;

public interface TextFormatter {

    <K, V> Map<K, V> loadDataFromFormat(String formatString, Class<K> keyClass, Class<V> valueClass);

    <T> String convert(T collection);

}

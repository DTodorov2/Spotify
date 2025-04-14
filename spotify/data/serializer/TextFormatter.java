package bg.sofia.uni.fmi.mjt.spotify.data.serializer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TextFormatter {
    <K, V> Map<K, V> loadDataFromFormat(String formatString, Class<K> keyClass, Class<V> valueClass);

    <T> Set<T> loadDataFromFormat(String formatString, Class<T> entityClass);

    <T> String convert(T collection);
}

package bg.sofia.uni.fmi.mjt.spotify.data.serializer;

import java.util.List;
import java.util.Map;

public interface TextFormatter {

    //<K, V> String convert(Map<K, V> mapToConvert);

    //<T> String convert(List<T> listToConvert);

    <K, V> Map<K, V> loadDataFromFormat(String formatString, Class<K> keyClass, Class<V> valueClass);

    <T> List<T> loadDataFromFormat(String formatString, Class<T> entityClass);

    <T> String convert(T collection);
}

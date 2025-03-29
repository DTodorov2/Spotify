package bg.sofia.uni.fmi.mjt.spotify.data.serializer.layout;

import java.util.List;
import java.util.Map;

public interface TextFormatter {

    <K, V> String covertToFormat(Map<K, V> mapToConvert);

    <T> String convertToFormat(List<T> listToConvert);

    <K, V> Map<K, V> loadDataFromFormat(String formatString, Class<K> keyClass, Class<V> valueClass);

    <T> List<T> loadDataFromFormat(String formatString, Class<T> entityClass);

}

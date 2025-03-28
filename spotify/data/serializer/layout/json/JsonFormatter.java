package bg.sofia.uni.fmi.mjt.spotify.data.serializer.layout.json;

import bg.sofia.uni.fmi.mjt.spotify.data.serializer.layout.TextFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class JsonFormatter implements TextFormatter {

    private final Gson gson;
    private static final String EMPTY_JSON = "{}";

    public JsonFormatter(Gson gson) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public <K, V> String covertToFormat(Map<K, V> mapToConvert) {
        checkArgumentNotNull(mapToConvert, "map to convert to json format");

        return gson.toJson(mapToConvert);
    }

    @Override
    public <T> String convertToFormat(List<T> listToConvert) {
        checkArgumentNotNull(listToConvert, "list to convert to json format");

        return gson.toJson(listToConvert);
    }

    @Override
    public <K, V> Map<K, V> loadDataFromFormat(String formatString, Class<K> keyClass, Class<V> valueClass) {
        checkArgumentNotNull(formatString, "string to be formatted");
        checkArgumentNotNull(keyClass, "class of the key");
        checkArgumentNotNull(valueClass, "class of the value");

        Type mapType = TypeToken.getParameterized(Map.class, keyClass, valueClass).getType();

        String stringToBeFormatted = formatString.isBlank() ? EMPTY_JSON : formatString;
        return gson.fromJson(stringToBeFormatted, mapType);
    }

    @Override
    public <T> List<T> loadDataFromFormat(String formatString, Class<T> entityClass) {
        checkArgumentNotNull(formatString, "string to be formatted");
        checkArgumentNotNull(entityClass, "class of the entity in the list");

        Type listType = TypeToken.getParameterized(List.class, entityClass).getType();

        String stringToBeFormatted = formatString.isBlank() ? EMPTY_JSON : formatString;
        return gson.fromJson(stringToBeFormatted, listType);
    }
}

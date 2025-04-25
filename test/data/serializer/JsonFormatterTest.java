package bg.sofia.uni.fmi.mjt.data.serializer;

import bg.sofia.uni.fmi.mjt.spotify.data.serializer.json.JsonFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonFormatterTest {

    private JsonFormatter jsonFormatter;

    @BeforeEach
    void setUp() {
        jsonFormatter = new JsonFormatter();
    }

    @Test
    void testLoadDataFromFormatWithValidJson() {
        String json = "{\"name\":\"John\",\"age\":30}";

        Map<String, Object> result = jsonFormatter.loadDataFromFormat(json, String.class, Object.class);

        assertEquals(2, result.size());
        assertEquals("John", result.get("name"));
        assertEquals(30.0, result.get("age"));
    }

    @Test
    void testLoadDataFromFormatWithEmptyJson() {
        String json = "{}";

        Map<String, Object> result = jsonFormatter.loadDataFromFormat(json, String.class, Object.class);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLoadDataFromFormatWithBlankJson() {
        String json = "  ";

        Map<String, Object> result = jsonFormatter.loadDataFromFormat(json, String.class, Object.class);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLoadDataFromFormatWithNullJson() {
        assertThrows(IllegalArgumentException.class,
                () -> jsonFormatter.loadDataFromFormat(null, String.class, Object.class),
                "The string to be formatted cannot be null!");
    }

    @Test
    void testConvertWithValidCollection() {
        Map<String, Object> testMap = Map.of("name", "John");

        String jsonResult = jsonFormatter.convert(testMap);
        String formattedString = jsonResult.replace("\n", System.lineSeparator());
        String expected = "{" + System.lineSeparator() +
                "  \"name\": \"John\"" + System.lineSeparator() + "}";

        assertEquals(formattedString, expected, "The two strings are expected to be the same!");
    }

    @Test
    void testConvertWithNullCollection() {
        assertThrows(IllegalArgumentException.class, () -> jsonFormatter.convert(null),
                "The string to be converted cannot be null!");
    }

    @Test
    void testConvertWithEmptyCollection() {
        Map<String, Object> emptyMap = Map.of();

        String jsonResult = jsonFormatter.convert(emptyMap);
        assertEquals("{}", jsonResult, "The empty collection should be converted as {}");
    }


}

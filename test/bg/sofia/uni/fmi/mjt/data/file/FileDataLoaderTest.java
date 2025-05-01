package bg.sofia.uni.fmi.mjt.data.file;

import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;

public class FileDataLoaderTest {

    private Path tempFilePath;
    private FileDataLoader fileDataLoader;

    @BeforeEach
    void setUp() {
        try {
            tempFilePath = Files.createTempFile("testFile", ".txt");
        } catch (IOException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }

        fileDataLoader = new FileDataLoader(tempFilePath);
    }

    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(tempFilePath);
        } catch (IOException e) {
            System.err.println("Error deleting temp file: " + e.getMessage());
        }
    }

    @Test
    void testLoadDataSuccessfully() {
        String testData = "testData";
        try {
            Files.write(tempFilePath, testData.getBytes());

            String result = fileDataLoader.loadData();
            assertEquals(testData, result, "The two strings are expected to be the same but are not!");
        } catch (DeserializationDataException | IOException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }
    }

    @Test
    void testLoadDataWithNonExistentFile() {
        try {
            Files.delete(tempFilePath);
        } catch (IOException e) {
            fail("The test cannot be executed! Reason: " + e.getMessage());
        }

        assertThrows(FileNotFoundException.class, () -> fileDataLoader.loadData(),
                 "FileNotFoundException is expected when non-existent file is passed as an argument!");
    }

    @Test
    void testLoadDataThrowsDeserializationDataException() {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("test", ".txt");
            Files.writeString(tempFile, "some content");
        } catch (IOException e) {
            fail("The test could not be executed!" + e.getMessage());
        }

        FileDataLoader loader = new FileDataLoader(tempFile);

        try (MockedStatic<Files> fs = mockStatic(Files.class)) {
            Path finalTempFile = tempFile;
            fs.when(() -> Files.lines(finalTempFile)).thenThrow(new IOException("IOException error"));

            assertThrows(DeserializationDataException.class, loader::loadData,
                    "DeserializationDataException is expected to be thrown when IO Exception is thrown!");
        }
    }

}

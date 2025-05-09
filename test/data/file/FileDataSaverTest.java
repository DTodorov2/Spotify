package bg.sofia.uni.fmi.mjt.data.file;

import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataSaver;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FileDataSaverTest {

    private static final String TEST_DATA = "testData";

    private Path tempFilePath;
    private FileDataSaver fileDataSaver;

    @BeforeEach
    void setUp() {
        try {
            tempFilePath = Files.createTempFile("testFile", ".txt");

            fileDataSaver = new FileDataSaver(tempFilePath);
            fileDataSaver.saveData(TEST_DATA, false);
        } catch (IOException | SerializationDataException e) {
            fail("The test could not be executed!" + e.getMessage());
        }
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
    void testSaveDataSuccessfully() {
        try {
            String fileContent = Files.readString(tempFilePath);
            assertEquals(TEST_DATA, fileContent, "The two strings are expected to be equal!");
        } catch (IOException e) {
            fail("The test could not be executed!" + e.getMessage());
        }
    }

    @Test
    void testSaveDataWithAppendingData() {
        try {
            String appendedData = " second data";
            fileDataSaver.saveData(appendedData, true);

            String fileContent = Files.readString(tempFilePath);

            String errMess = "The two strings are expected to be equal!";
            assertEquals(TEST_DATA + appendedData, fileContent, errMess);
        } catch (SerializationDataException | IOException e) {
            fail("The test could not be executed!" + e.getMessage());
        }
    }

    @Test
    void testSaveDataWithNonExistentFile() {
        Path nonExistentFilePath = Path.of("non-existent/path/to/file.txt");
        FileDataSaver fileDataSaver = new FileDataSaver(nonExistentFilePath);
        try {
            fileDataSaver.saveData(TEST_DATA, false);
            String fileContent = Files.readString(nonExistentFilePath);

            String errMess = "The file is expected to be created and the data to be saved!";
            assertEquals(TEST_DATA, fileContent, errMess);
        } catch (IOException | SerializationDataException e) {
            fail("The test could not be executed! " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(nonExistentFilePath);
                Files.deleteIfExists(nonExistentFilePath.getParent());
                Files.deleteIfExists(nonExistentFilePath.getParent().getParent());
                Files.deleteIfExists(nonExistentFilePath.getParent().getParent().getParent());
            } catch (IOException e) {
                System.err.println("Could not clean up test files: " + e.getMessage());
            }
        }
    }

}

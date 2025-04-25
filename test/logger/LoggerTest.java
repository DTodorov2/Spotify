package bg.sofia.uni.fmi.mjt.logger;

import bg.sofia.uni.fmi.mjt.spotify.data.DataSerializer;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.UnsuccessfulLogOperationException;
import bg.sofia.uni.fmi.mjt.spotify.logger.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class LoggerTest {

    private DataSerializer mockSerializer;
    private Logger logger;
    private final String testMessage = "Error occurred";

    @BeforeEach
    void setUp() {
        mockSerializer = Mockito.mock(DataSerializer.class);
        logger = new Logger(mockSerializer);
    }

    @Test
    public void testLogSuccessfullyCallsSaveData() {
        try {
            logger.log(testMessage, new IllegalArgumentException("Test Exception"));
            verify(mockSerializer).saveData(contains(testMessage), eq(true));
        } catch (UnsuccessfulLogOperationException | SerializationDataException e) {
            fail("The test cannot be executed. Reason: " + e.getMessage());
        }
    }

    @Test
    public void testLogThrowsUnsuccessfulLogOperation() {
        try {
            doThrow(new SerializationDataException("Failed to save"))
                    .when(mockSerializer)
                    .saveData(anyString(), eq(true));

            String mess = "UnsuccessfulLogOperationException is expected to be thrown, when the message cannot be logged!";
            assertThrows(UnsuccessfulLogOperationException.class,
                    () -> logger.log(testMessage, new IllegalArgumentException(testMessage)),
                    mess);
        } catch (SerializationDataException e) {
            fail("The test cannot be executed. Reason: " + e.getMessage());
        }

    }

    @Test
    public void testLogWithNullMessage() {
        assertThrows(IllegalArgumentException.class,
                () -> logger.log(null, new IllegalArgumentException("message")),
                "IllegalArgumentException is expected to be thrown when null as a message is passed!");
    }

    @Test
    public void testLogWithNullException() {
        assertThrows(IllegalArgumentException.class,
                () -> logger.log("message", null),
                "IllegalArgumentException is expected to be thrown when null as an exception is passed!");
    }

}

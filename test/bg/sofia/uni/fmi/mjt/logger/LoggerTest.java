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

    private static final String MESSAGE = "Error occurred";

    private DataSerializer mockSerializer;
    private Logger logger;

    @BeforeEach
    void setUp() {
        mockSerializer = Mockito.mock(DataSerializer.class);
        logger = new Logger(mockSerializer);
    }

    @Test
    void testLogSuccessfullyCallsSaveData() {
        try {
            logger.log(MESSAGE, new IllegalArgumentException());

            verify(mockSerializer).saveData(contains(MESSAGE), eq(true));
        } catch (UnsuccessfulLogOperationException | SerializationDataException e) {
            fail("The test cannot be executed. Reason: " + e.getMessage());
        }
    }

    @Test
    void testLogThrowsUnsuccessfulLogOperation() {
        try {
            doThrow(new SerializationDataException(MESSAGE))
                    .when(mockSerializer)
                    .saveData(anyString(), eq(true));

            String mess = "UnsuccessfulLogOperationException is expected to be thrown, when the message cannot be logged!";
            assertThrows(UnsuccessfulLogOperationException.class,
                    () -> logger.log(MESSAGE, new IllegalArgumentException(MESSAGE)),
                    mess);
        } catch (SerializationDataException e) {
            fail("The test cannot be executed. Reason: " + e.getMessage());
        }
    }

    @Test
    void testLogWithNullMessage() {
        assertThrows(IllegalArgumentException.class,
                () -> logger.log(null, new IllegalArgumentException(MESSAGE)),
                "IllegalArgumentException is expected to be thrown when null as a message is passed!");
    }

    @Test
    void testLogWithNullException() {
        assertThrows(IllegalArgumentException.class,
                () -> logger.log(MESSAGE, null),
                "IllegalArgumentException is expected to be thrown when null as an exception is passed!");
    }

}

package bg.sofia.uni.fmi.mjt.spotify.logger;

import bg.sofia.uni.fmi.mjt.spotify.data.DataSerializer;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.UnsuccessfulLogOperationException;

import java.time.LocalDateTime;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class Logger {

    private final DataSerializer dataSerializer;

    public Logger(DataSerializer dataSerializer) {
        checkArgumentNotNull(dataSerializer, "data serializer");
        this.dataSerializer = dataSerializer;
    }

    public void log(String message, Exception e) throws UnsuccessfulLogOperationException {
        validateString(message, "data to be logged");

        String logMessage = buildLogMessage(message, e);

        try {
            dataSerializer.saveData(logMessage, true);
        } catch (SerializationDataException e1) {
            throw new UnsuccessfulLogOperationException("The message could not be logged!", e);
        }
    }

    private String buildLogMessage(String message, Exception e) {
        validateString(message, "message for the exception");
        checkArgumentNotNull(e, "exception object");

        StringBuilder sb = new StringBuilder();
        sb.append("[")
                .append(LocalDateTime.now())
                .append("] ")
                .append(System.lineSeparator())
                .append(e.getClass().getName())
                .append(": ")
                .append(message)
                .append(System.lineSeparator());
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("\t")
                    .append("at ")
                    .append(element)
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }

}

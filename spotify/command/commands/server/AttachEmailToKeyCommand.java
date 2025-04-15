package bg.sofia.uni.fmi.mjt.spotify.command.commands.server;

import bg.sofia.uni.fmi.mjt.spotify.exception.checked.InvalidArgumentsCountException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.nio.channels.SelectionKey;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateArgCount;

public class AttachEmailToKeyCommand extends ServerCommand {
    private static final int EXPECTED_INPUT_LENGTH = 1;
    private static final String SUCCESS_MESS = "READY";
    private static final String ERROR_MESS = "ERROR";

    public AttachEmailToKeyCommand(String[] arguments, ServerResources serverResources) {
        super(arguments, EXPECTED_INPUT_LENGTH, serverResources);
    }

    @Override
    public String execute() throws InvalidArgumentsCountException {
        validateArgCount(arguments, argsCount);
        String emailToAttach = arguments[0];
        SelectionKey key = serverResources.getSelectionKey();
        if (key != null) {
            key.attach(emailToAttach);
            return SUCCESS_MESS;
        }
        return ERROR_MESS;
    }
}

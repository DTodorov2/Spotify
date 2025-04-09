package bg.sofia.uni.fmi.mjt.spotify.server;

import bg.sofia.uni.fmi.mjt.spotify.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataSaver;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelCommunicationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ClosingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.HandlingSelectionKeyException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.LoadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SavingDataToFileException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ServerConfigurationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.UnsuccessfulLogOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulChannelClosingException;
import bg.sofia.uni.fmi.mjt.spotify.logger.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class SpotifyServer {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private static final Path LOGS_FILEPATH = Paths.get("server_logs", "serverLogs.txt");
    private static final Path SONGS_FILEPATH = Paths.get("assets", "songs", "songs.txt");
    private static final Path USERS_FILEPATH = Paths.get("assets", "users", "users.txt");

    private static final int BUFFER_SIZE = 1024;
    private ByteBuffer buffer;

    private boolean isServerWorking;

    private final ServerResources serverResources;
    private final Logger logger;

    private Selector selector;
    private final CommandExecutor commandExecutor;

    public SpotifyServer() {
        serverResources = new ServerResources();
        logger = new Logger(new FileDataSaver(LOGS_FILEPATH));
        commandExecutor = new CommandExecutor(serverResources);
        isServerWorking = false;
    }

    public void start() {
        boolean isDataLoaded = loadAssets();

        if (!isDataLoaded) {
            return;
        }

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            startServer(serverSocketChannel);
        } catch (IOException | ServerConfigurationException e) {
            String message = "Unable to start the server!" + e.getMessage();
            displayMessage(message);
            handleLogs(e);
        } finally {
            stop();
        }
    }

    private boolean loadAssets() {
        try {
            serverResources.getSongsRepository().loadData(SONGS_FILEPATH);
            serverResources.getUsersRepository().loadData(USERS_FILEPATH);

            return true;
        } catch (LoadingDataFromFileException e) {
            displayMessage(e.getMessage());
            handleLogs(e);
        }

        return false;

    }

    private void startServer(ServerSocketChannel serverSocketChannel) throws IOException, ServerConfigurationException {
        checkArgumentNotNull(serverSocketChannel, "server socket channel");

        Selector selector = Selector.open();
        this.selector = selector;
        configureServerSocketChannel(serverSocketChannel, selector);
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        isServerWorking = true;
        runServerLoop();
    }

    private void configureServerSocketChannel(ServerSocketChannel serverChannel, Selector selector)
            throws ServerConfigurationException {
        checkArgumentNotNull(serverChannel, "server channel");
        checkArgumentNotNull(selector, "selector");

        try {
            serverChannel.bind(new InetSocketAddress(HOST, PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new ServerConfigurationException("Unable to configure the server!", e);
        }
    }

    private void runServerLoop() {
        while (isServerWorking) {
            try {
                if (selector.select() == 0) {
                    continue;
                }
                processReadyKeys();
            } catch (IOException e) {
                displayMessage("Error occurred while processing client request: " + e.getMessage());
                handleLogs(e);
            }
        }
    }

    private void processReadyKeys() {
        try {
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                serverResources.setSelectionKey(key);
                if (key.isValid()) {
                    handleKey(key);
                } else {
                    key.cancel();
                }
                keyIterator.remove();
            }
        } catch (HandlingSelectionKeyException e) {
            handleLogs(e);
        }
    }

    private void handleKey(SelectionKey key) throws HandlingSelectionKeyException {
        checkArgumentNotNull(key, "selection key");

        try {
            if (key.isReadable()) {
                handleReadableKey(key);
            } else if (key.isAcceptable()) {
                handleAcceptableKey(selector, key);
            }
        } catch (IOException | ChannelCommunicationException e) {
            throw new HandlingSelectionKeyException("Unable to process the selection key", e);
        }
    }

    private void handleAcceptableKey(Selector selector, SelectionKey key) throws IOException {
        checkArgumentNotNull(selector, "selector");
        checkArgumentNotNull(key, "selection key");

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();

        if (socketChannel != null) {
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }
    }

    private void handleReadableKey(SelectionKey key) throws IOException, ChannelCommunicationException {
        checkArgumentNotNull(key, "selection key");

        SocketChannel clientChannel = (SocketChannel) key.channel();
        String clientInput = getClientInput(clientChannel);
//        if (clientInput == null) {
//            //toi tuka zatvarq client connection-a, ama shto
//            return;
//        }

        //trqq napravq komand z disconnect
//        if (clientInput.equalsIgnoreCase("disconnect")) {
//            displayMessage("Client with email: " + key.attachment() + " is disconnecting...");
//            closeClientChannel(clientChannel, key);
//            return;
//        }

        //System.out.println(clientInput);
        String output = commandExecutor.execute(clientInput);
        writeClientOutput(clientChannel, output);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        checkArgumentNotNull(clientChannel, "client channel");

        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        validateString(output, "output string");
        checkArgumentNotNull(clientChannel, "client channel");

        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    public void stop() {
        this.isServerWorking = false;
        serverResources.getExecutor().shutdown();

        if (selector != null && selector.isOpen()) {
            selector.wakeup();
            closeAllChannels();
        }

        saveAssets();
    }

    private void closeAllChannels() {
        for (SelectionKey key : selector.keys()) {
            try {
                if (key.isValid()) {
                    key.channel().close();
                }
            } catch (IOException e) {
                handleLogs(new ClosingClientChannelException("Unable to close all clients' channels!", e));
            }
        }
    }

    private void saveAssets() {
        try {
            serverResources.getSongsRepository().saveData(SONGS_FILEPATH);
            serverResources.getUsersRepository().saveData(USERS_FILEPATH);
        } catch (SavingDataToFileException e) {
            displayMessage(e.getMessage());
            handleLogs(e);
        }
    }

    private void displayMessage(String message) {
        checkArgumentNotNull(message, "message");
        System.out.println(message);
    }

    private void handleLogs(Exception e) {
        try {
            logger.log(e.getMessage(), e);
        } catch (UnsuccessfulLogOperationException ex) {
            displayMessage(ex.getMessage());
        }
    }
}

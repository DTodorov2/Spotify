package bg.sofia.uni.fmi.mjt.spotify.server.audio;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.MarshallingException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.UnsuccessfulLogOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.StreamingException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulChannelClosingException;
import bg.sofia.uni.fmi.mjt.spotify.logger.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataSaver;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.model.SerializableAudioFormat;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class AudioServer implements Runnable {

    private static final String SONGS_FILEPATH = "assets\\songs\\";
    private static final Path LOGS_FILEPATH = Paths.get("server_logs", "serverLogs.txt");
    private static final int BUFFER_SIZE = 4096 ;
    private static final String STOP_COMMAND = "stop";
    private static final String SEND_COMMAND = "send";
    private static final String EXTENSION = ".wav";

    private final ByteBuffer buffer;
    private final Logger logger;
    private final String pathToSong;

    private final SocketChannel channel;
    private final ChannelDataLoader channelDataLoader;
    private final ChannelDataWriter channelDataWriter;
    private AudioInputStream audioInputStream;
    private volatile boolean isStreaming;
    private final User streamingUser;

    public AudioServer(String songName, SocketChannel channel, User streamingUser) {
        checkArgumentNotNull(songName, "music title");
        checkArgumentNotNull(channel, "socket channel");
        checkArgumentNotNull(streamingUser, "streaming user");

        pathToSong = SONGS_FILEPATH + songName + EXTENSION;
        logger = new Logger(new FileDataSaver(LOGS_FILEPATH));

        this.channel = channel;
        channelDataLoader = new ChannelDataLoader(channel);
        channelDataWriter = new ChannelDataWriter(channel);
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        isStreaming = false;
        this.streamingUser = streamingUser;
    }

    @Override
    public void run() {
        try {
            File songFile = new File(pathToSong);
            audioInputStream = AudioSystem.getAudioInputStream(songFile);
            streamingUser.setAudioServer(this);

            sendAudioFormat();
            isStreaming = true;
            start();
        } catch (IOException | MarshallingException | UnsupportedAudioFileException e) {
            handleLogs(new StreamingException("Unable to play the song with path: " + pathToSong + " .", e));
        } finally {
            goToDefaultState();
        }
    }

    private String getCommand() throws DeserializationDataException {
        byte[] clientInputBytes = channelDataLoader.loadBytes();

        return clientInputBytes != null ? new String(clientInputBytes, StandardCharsets.UTF_8) : null;
    }

    private void start() throws IOException, MarshallingException {
        while (isStreaming) {
            String command = getCommand();
            if (command != null && command.equals(SEND_COMMAND)) {
                byte[] byteStreamArray = new byte[BUFFER_SIZE];
                if (audioInputStream.read(byteStreamArray, 0, byteStreamArray.length) == -1) {
                    isStreaming = false;
                    return;
                }
                channelDataWriter.saveData(byteStreamArray);
            } else if (command != null && command.equals(STOP_COMMAND)) {
                isStreaming = false;
            }
        }
    }

    public void setIsStreaming(boolean isStreaming) {
        if (!channel.isOpen()) {
            return;
        }
        this.isStreaming = isStreaming;
    }

    private void sendAudioFormat() throws IOException {
        SerializableAudioFormat serializableFormat = SerializableAudioFormat.of(audioInputStream.getFormat());

        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteStream)) {

            objectOut.writeObject(serializableFormat);
            objectOut.flush();

            byte[] serializedFormatBytes = byteStream.toByteArray();

            buffer.clear();
            buffer.putInt(serializedFormatBytes.length);
            buffer.put(serializedFormatBytes);
            buffer.flip();

            channel.write(buffer);
        }
    }

    private void goToDefaultState() {
        try {
            streamingUser.setAudioServer(null);
            audioInputStream.close();
            if (channel.isOpen()) {
                channel.close();
            }

            isStreaming = false;
        } catch (IOException e) {
            throw new UnsuccessfulChannelClosingException("Could not close streaming channel!", e);
        }
    }

    private void handleLogs(Exception e) {
        try {
            logger.log(e.getMessage(), e);
        } catch (UnsuccessfulLogOperationException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

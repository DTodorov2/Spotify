package bg.sofia.uni.fmi.mjt.spotify.server.audio;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataSaver;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.UnsuccessfulLogOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.StreamingException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulChannelClosingException;
import bg.sofia.uni.fmi.mjt.spotify.logger.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.audio.model.SerializableAudioFormat;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
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
import java.util.Arrays;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;

public class AudioServer implements Runnable {

    private static final String SONGS_FILEPATH = "assets\\songs";
    private static final Path LOGS_FILEPATH = Paths.get("server_logs", "serverLogs.txt");
    private static final int BUFFER_SIZE = 4096;

    private final String pathToSong;
    private final ByteBuffer buffer;
    private final Logger logger;

    private final SocketChannel socketChannel;
    private AudioInputStream audioInputStream;

    private final ChannelDataLoader channelDataLoader;
    private final ChannelDataWriter channelDataWriter;

    boolean isStreaming;

    public AudioServer(String songName, SocketChannel socketChannel) {
        validateString(songName, "song name");
        checkArgumentNotNull(socketChannel, "server resources");

        //this.serverResources = serverResources;
        pathToSong = SONGS_FILEPATH + songName + ".wav";
        this.socketChannel = socketChannel;
        //socketChannel = (SocketChannel) serverResources.getSelectionKey().channel();
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        logger = new Logger(new FileDataSaver(LOGS_FILEPATH));
        //channelDataLoader = new ChannelDataLoader(buffer, socketChannel);
        channelDataWriter = new ChannelDataWriter(socketChannel);
        channelDataLoader = new ChannelDataLoader(socketChannel);
        isStreaming = false;
    }

    @Override
    public void run() {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(pathToSong));
            sendAudioFormat();

            isStreaming = true;
            startStreaming();

        } catch (SerializationDataException | UnsupportedAudioFileException | IOException e) {
            handleLogs(new StreamingException("Unable to play the song with path: " + pathToSong + " .", e));
        } finally {
            goToDefaultState();
        }

    }

    private void sendAudioFormat() throws IOException {
        SerializableAudioFormat serializableAudioFormat = SerializableAudioFormat.of(audioInputStream.getFormat());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(serializableAudioFormat);
        objectOutputStream.flush();

        byte[] audioFormatBytes = byteArrayOutputStream.toByteArray();
        buffer.clear();
        buffer.put(audioFormatBytes);
        buffer.flip();

        socketChannel.write(buffer);
    }

    private void startStreaming() throws IOException, SerializationDataException, DeserializationDataException {
        while (isStreaming) {
            String clientInput = channelDataLoader.loadData();
            if (clientInput.equals("stop")) {
                isStreaming = false;
                break;
            }

            byte[] byteStreamArray = new byte[BUFFER_SIZE];

            if (audioInputStream.read(byteStreamArray, 0, byteStreamArray.length) == -1) {
                isStreaming = false;
                break;
            }
            channelDataWriter.saveData(Arrays.toString(byteStreamArray));
        }
    }

    private void handleLogs(Exception e) {
        try {
            logger.log(e.getMessage(), e);
        } catch (UnsuccessfulLogOperationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void stopStreaming() {
        if (!socketChannel.isOpen()) {
            return;
        }
        try {
            isStreaming = false;
            //trqq li da go zatvrqm vuobshte
            socketChannel.close();
            audioInputStream.close();
        } catch (IOException e) {
            handleLogs(new StreamingException("The streaming server cannot be closed!", e));
        }
//        tova dolu nqmam ideq shto go pravi
//        String stopCommand = "stop";
//
//        try {
//            streamingChannel.write(ByteBuffer.wrap(stopCommand.getBytes(StandardCharsets.UTF_8)));
//        } catch (IOException e) {
//            serverState.getExceptionHandler().handle(
//                    new InvalidStreamingClosingOperationException("Could not stop streaming!", e), false);
//        }
    }

    private void goToDefaultState() {
        try {
            isStreaming = false;
            if (socketChannel.isOpen()) {
                socketChannel.close();
            }
            audioInputStream.close();
        } catch (IOException e) {
            throw new UnsuccessfulChannelClosingException("Could not close streaming channel!", e);
        }
    }
}

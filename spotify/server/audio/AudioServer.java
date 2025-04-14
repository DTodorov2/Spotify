package bg.sofia.uni.fmi.mjt.spotify.server.audio;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.UnsuccessfulLogOperationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.StreamingException;
import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulChannelClosingException;
import bg.sofia.uni.fmi.mjt.spotify.logger.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataSaver;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class AudioServer implements Runnable {

    private static final String SONGS_FILEPATH = "assets\\songs\\";
    private static final Path LOGS_FILEPATH = Paths.get("server_logs", "serverLogs.txt");
    private static final int BUFFER_SIZE = 4096 ;

    private final ByteBuffer buffer;
    private final Logger logger;
    private final String pathToSong;

    private final SocketChannel channel;
    private final ChannelDataLoader channelDataLoader;
    private final ChannelDataWriter channelDataWriter;
    private AudioInputStream audioInputStream;
    private volatile boolean isStreaming;

    public AudioServer(String songName, SocketChannel channel) {
        checkArgumentNotNull(songName, "music title");
        checkArgumentNotNull(channel, "socket channel");

        pathToSong = SONGS_FILEPATH + songName + ".wav";
        logger = new Logger(new FileDataSaver(LOGS_FILEPATH));

        this.channel = channel;
        channelDataLoader = new ChannelDataLoader(channel);
        channelDataWriter = new ChannelDataWriter(channel);
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        isStreaming = false;
    }

    @Override
    public void run() {
        try {
            File songFile = new File(pathToSong);
            audioInputStream = AudioSystem.getAudioInputStream(songFile);

            sendAudioFormat();
            isStreaming = true;
            //while (isStreaming) {
            //stream();
            //}
            start();
        } catch (IOException | SerializationDataException | DeserializationDataException |
                 UnsupportedAudioFileException e) {
            handleLogs(new StreamingException("Unable to play the song with path: " + pathToSong + " .", e));
        } finally {
            goToDefaultState();
        }
    }

//    private void stream() throws IOException, SerializationDataException, DeserializationDataException {
//        while (isStreaming) {
//            String clientInputArgs = getClientInputArgs(channel);
//
//            if (clientInputArgs.equals("send")) {
//                byte[] byteStreamArray = new byte[BUFFER_SIZE];
//
//                if (audioInputStream.read(byteStreamArray, 0, byteStreamArray.length) == -1) {
//                    isStreaming = false;
//                    return;
//                }
//                channelDataWriter.saveData(byteStreamArray);
//            } else if (clientInputArgs.equals("stop")){
//                isStreaming = false;
//            }
////            switch (clientInputArgs) {
////                case "send" -> {
////                    byte[] byteStreamArray = new byte[BUFFER_SIZE];
////
////                    if (audioInputStream.read(byteStreamArray, 0, byteStreamArray.length) == -1) {
////                        isStreaming = false;
////                        return;
////                    }
////                    channelDataWriter.saveData(byteStreamArray);
////                    //channelWriter.writeTo(streamingChannel, byteStreamArray);
////                }
////                case "stop" -> isStreaming = false;
////            }
//        }

    private String getCommand() throws DeserializationDataException {
        byte[] clientInputBytes = channelDataLoader.loadBytes();

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void start() throws IOException, SerializationDataException, DeserializationDataException {
        while (isStreaming) {
            String command = getCommand();
            if (command.equals("send")) {
                byte[] byteStreamArray = new byte[BUFFER_SIZE];
                if (audioInputStream.read(byteStreamArray, 0, byteStreamArray.length) == -1) {
                    isStreaming = false;
                    return;
                }
                channelDataWriter.saveData(byteStreamArray);
            } else if (command.equals("stop")) {
                isStreaming = false;
            }
        }
    }

    private void sendAudioFormat()
            throws IOException {

        AudioFormat format = audioInputStream.getFormat();

        AudioFormat.Encoding encoding = format.getEncoding();
        float sampleRate = format.getSampleRate();
        int sampleSizeInBits = format.getSampleSizeInBits();
        int channels = format.getChannels();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        boolean bigEndian = format.isBigEndian();

        byte[] encodingBytes = encoding.toString().getBytes(StandardCharsets.UTF_8);
        int encodingLength = encodingBytes.length;

        buffer.clear();
        buffer.putFloat(sampleRate);
        buffer.putInt(sampleSizeInBits);
        buffer.putInt(channels);
        buffer.putInt(frameSize);
        buffer.putFloat(frameRate);
        buffer.put((byte) (bigEndian ? 1 : 0));
        buffer.putInt(encodingLength);
        buffer.put(encodingBytes);

        buffer.flip();
        channel.write(buffer);
    }

    private void goToDefaultState() {
        try {
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

package bg.sofia.uni.fmi.mjt.spotify.server.audio;

import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.server.resources.ServerResources;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class AudioServer implements Runnable {

    private static final String INPUT_SPLIT_PATTERN = " ";
    private static final String SONGS_FILEPATH = "assets\\songs\\%s.wav";
    private static final int BUFFER_SIZE = 4096 ;
    private final ByteBuffer formatBuffer;

    private final String songKey;
    //private final ServerResources serverResources;

    private final SocketChannel channel;
    private final ChannelDataLoader channelReader;
    private final ChannelDataWriter channelWriter;
    //private final User userStreamingClient;
    private AudioInputStream audioStream;
    private volatile boolean isStreaming;

    public AudioServer(String songKey, SocketChannel channel) {
        checkArgumentNotNull(songKey, "music title");
        //checkArgumentNotNull(serverResources, "server state");

        //this.serverResources = serverResources;
        this.songKey = songKey;

        this.channel = channel;
        this.channelReader = new ChannelDataLoader(channel);
        this.channelWriter = new ChannelDataWriter(channel);
        //this.userStreamingClient = getUserStreamingClient();
        this.formatBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.isStreaming = false;
    }

    @Override
    public void run() {
        try {
            audioStream = createAudioStream();
            //channelWriter.saveData("sent");
            sendFormat(formatBuffer, audioStream.getFormat(), channel);
            isStreaming = true;
            while (isStreaming) {
                stream();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //serverState.getExceptionHandler().handle(e, false);
        } catch (Exception e) {
            String message = "Unexpected error occurred!";
            System.out.println(message + e.getMessage());
            //serverState.getExceptionHandler().handle(new UnexpectedException(message, e), false);
        } finally {
            setStopState();
        }
    }

    public void stop() {
        if (!channel.isOpen()) {
            return;
        }

        isStreaming = false;
        String stopCommand = "stop";

        try {
            channel.write(ByteBuffer.wrap(stopCommand.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            serverState.getExceptionHandler().handle(
//                    new InvalidStreamingClosingOperationException("Could not stop streaming!", e), false);
        }
    }

//    private User getUserStreamingClient() {
//        String idToken = (String) serverState.getKey().attachment();
//        User userToBeStreaming = serverState.getUserRepository().getUser(idToken);
//
//        if (userToBeStreaming == null) {
//            throw new UserNotExistingException("The user that to be streaming cannot be found in the system!");
//        }
//
//        return userToBeStreaming;
//    }

    private AudioInputStream createAudioStream() throws IOException {
        File pathToMusicFile = new File(String.format(SONGS_FILEPATH, songKey));

        AudioInputStream audioStream = null;
        try {
            audioStream = AudioSystem.getAudioInputStream(pathToMusicFile);
        } catch (UnsupportedAudioFileException e) {
            System.out.println(e.getMessage());
            //throw new AudioStreamingException("The given audio file is not supported by the system!", e);
        }

        //userStreamingClient.setAudioStreamSender(this);

        return audioStream;
    }

    private void stream() throws IOException, SerializationDataException, DeserializationDataException {
        String[] clientInputArgs = getClientInputArgs(channel);

        switch (clientInputArgs[0]) {
            case "stream" -> {
                byte[] byteStreamArray = new byte[BUFFER_SIZE];

                if (audioStream.read(byteStreamArray, 0, byteStreamArray.length) == -1) {
                    isStreaming = false;
                    return;
                }
                channelWriter.saveData(byteStreamArray);
                //channelWriter.writeTo(streamingChannel, byteStreamArray);
            }
            case "stop" -> isStreaming = false;
        }
    }

    private String[] getClientInputArgs(SocketChannel clientChannel) throws DeserializationDataException {
        byte[] clientInputBytes = channelReader.loadBytes();
        //byte[] clientInputBytes = channelReader.readBytesFrom(clientChannel);

        String clientInput = new String(clientInputBytes, StandardCharsets.UTF_8);

        return clientInput.split(INPUT_SPLIT_PATTERN);
    }

    private void sendFormat(ByteBuffer buffer, AudioFormat format, SocketChannel clientSocketChannel)
            throws IOException {
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

        System.out.println("Buferyt izprashta tezi danni: " + buffer);
        buffer.flip();
        clientSocketChannel.write(buffer);
    }

    private void setStopState() {
        closeAudioStream();
        closeStreamingChannel();

        //userStreamingClient.setAudioStreamSender(null);
        isStreaming = false;
    }

    private void closeAudioStream() {
        try {
            audioStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeStreamingChannel() {
        try {
            if (channel.isOpen()) {
                channel.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //throw new InvalidChannelClosingOperationException("Could not close streaming channel!", e);
        }
    }

}

//package bg.sofia.uni.fmi.mjt.spotify.server.audio;
//
//import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
//import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
//import bg.sofia.uni.fmi.mjt.spotify.data.file.FileDataSaver;
//import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
//import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
//import bg.sofia.uni.fmi.mjt.spotify.exception.checked.UnsuccessfulLogOperationException;
//import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.StreamingException;
//import bg.sofia.uni.fmi.mjt.spotify.exception.unchecked.UnsuccessfulChannelClosingException;
//import bg.sofia.uni.fmi.mjt.spotify.logger.Logger;
//import bg.sofia.uni.fmi.mjt.spotify.server.audio.model.SerializableAudioFormat;
//
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.UnsupportedAudioFileException;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
//import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;
//
//public class AudioServer implements Runnable {
//
//    private static final String SONGS_FILEPATH = "assets\\songs\\";
//    private static final Path LOGS_FILEPATH = Paths.get("server_logs", "serverLogs.txt");
//    private static final int BUFFER_SIZE = 4096;
//
//    private final String pathToSong;
//    private final ByteBuffer buffer;
//    private final Logger logger;
//
//    private final SocketChannel socketChannel;
//    private AudioInputStream audioInputStream;
//
//    private final ChannelDataLoader channelDataLoader;
//    private final ChannelDataWriter channelDataWriter;
//
//    boolean isStreaming;
//
//    public AudioServer(String songName, SocketChannel socketChannel) {
//        validateString(songName, "song name");
//        checkArgumentNotNull(socketChannel, "server resources");
//
//        //this.serverResources = serverResources;
//        pathToSong = SONGS_FILEPATH + songName + ".wav";
//        this.socketChannel = socketChannel;
//        //socketChannel = (SocketChannel) serverResources.getSelectionKey().channel();
//        buffer = ByteBuffer.allocate(BUFFER_SIZE);
//        logger = new Logger(new FileDataSaver(LOGS_FILEPATH));
//        //channelDataLoader = new ChannelDataLoader(buffer, socketChannel);
//        channelDataWriter = new ChannelDataWriter(socketChannel);
//        channelDataLoader = new ChannelDataLoader(socketChannel);
//        isStreaming = false;
//    }
//
//    @Override
//    public void run() {
//        try {
//            File songFile = new File(pathToSong);
//            System.out.println("File exists: " + songFile.exists() + ", path: " + songFile.getAbsolutePath());
//            audioInputStream = AudioSystem.getAudioInputStream(songFile);
//            sendAudioFormat();
//
//            isStreaming = true;
//            //while (isStreaming) {
//            stream();
//            //}
//            //startStreaming();
//
//        } catch (DeserializationDataException |
//                 SerializationDataException |
//                 UnsupportedAudioFileException |
//                 IOException e) {
//            handleLogs(new StreamingException("Unable to play the song with path: " + pathToSong + " .", e));
//        } finally {
//            goToDefaultState();
//        }
//
//    }
//
//    private void sendAudioFormat()
//            throws IOException {
//        AudioFormat format = audioInputStream.getFormat();
//
//        AudioFormat.Encoding encoding = format.getEncoding();
//        float sampleRate = format.getSampleRate();
//        int sampleSizeInBits = format.getSampleSizeInBits();
//        int channels = format.getChannels();
//        int frameSize = format.getFrameSize();
//        float frameRate = format.getFrameRate();
//        boolean bigEndian = format.isBigEndian();
//
//        byte[] encodingBytes = encoding.toString().getBytes(StandardCharsets.UTF_8);
//
//        buffer.clear();
//        buffer.putFloat(sampleRate);
//        buffer.putInt(sampleSizeInBits);
//        buffer.putInt(channels);
//        buffer.putInt(frameSize);
//        buffer.putFloat(frameRate);
//        buffer.put((byte) (bigEndian ? 1 : 0));
//        buffer.putInt(encodingBytes.length);
//        buffer.put(encodingBytes);
//
//        buffer.flip();
//        socketChannel.write(buffer);
//    }
//
//    private void stream() throws IOException, DeserializationDataException, SerializationDataException {
//        String[] clientInputArgs = getClientInputArgs();
//        System.out.println(clientInputArgs[0]);
//        switch (clientInputArgs[0]) {
//            case "stream" -> {
//                byte[] byteStreamArray = new byte[BUFFER_SIZE];
//
//                if (audioInputStream.read(byteStreamArray, 0, byteStreamArray.length) == -1) {
//                    isStreaming = false;
//                    return;
//                }
//                channelDataWriter.saveData(byteStreamArray);
//                //channelWriter.writeTo(streamingChannel, byteStreamArray);
//            }
//            case "stop" -> isStreaming = false;
//        }
//    }
//
//    private String[] getClientInputArgs() throws DeserializationDataException {
//        byte[] clientInputBytes  = channelDataLoader.loadBytes();
//
//        String clientInput = new String(clientInputBytes, StandardCharsets.UTF_8);
//
//        return clientInput.split(" ");
//    }
//
////    private void startStreaming() throws IOException, SerializationDataException, DeserializationDataException {
////        while (isStreaming) {
////            String clientInput = channelDataLoader.loadData();
////            if (clientInput.equals("stop")) {
////                isStreaming = false;
////                break;
////            }
////
////            byte[] byteStreamArray = new byte[BUFFER_SIZE];
////
////            System.out.println("cheta data v " + LocalDateTime.now());
////            if (audioInputStream.read(byteStreamArray, 0, byteStreamArray.length) == -1) {
////                isStreaming = false;
////                break;
////            }
////            System.out.println("zapazvam data: " + Arrays.toString(byteStreamArray));
////            channelDataWriter.saveData(byteStreamArray);
////            break;
////        }
////    }
//
//    private void handleLogs(Exception e) {
//        try {
//            logger.log(e.getMessage(), e);
//        } catch (UnsuccessfulLogOperationException ex) {
//            System.out.println(ex.getMessage());
//        }
//    }
//
//    public void stopStreaming() {
//        if (!socketChannel.isOpen()) {
//            return;
//        }
//        try {
//            isStreaming = false;
//            //trqq li da go zatvrqm vuobshte
//            socketChannel.close();
//            audioInputStream.close();
//        } catch (IOException e) {
//            handleLogs(new StreamingException("The streaming server cannot be closed!", e));
//        }
////        tova dolu nqmam ideq shto go pravi
////        String stopCommand = "stop";
////
////        try {
////            streamingChannel.write(ByteBuffer.wrap(stopCommand.getBytes(StandardCharsets.UTF_8)));
////        } catch (IOException e) {
////            serverState.getExceptionHandler().handle(
////                    new InvalidStreamingClosingOperationException("Could not stop streaming!", e), false);
////        }
//    }
//
//    private void goToDefaultState() {
//        try {
//            isStreaming = false;
//            if (socketChannel.isOpen()) {
//                socketChannel.close();
//            }
//            audioInputStream.close();
//        } catch (IOException e) {
//            throw new UnsuccessfulChannelClosingException("Could not close streaming channel!", e);
//        }
//    }
//}

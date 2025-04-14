package bg.sofia.uni.fmi.mjt.spotify.client.audio;

import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelConnectionException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;

public class AudioClient implements Runnable {

    private static final int SERVER_PORT = 8080;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_CAPACITY = 4096 ;

    private volatile boolean isRunning;
    private final ByteBuffer formatBuffer;
    private final SocketChannel socketChannel;
    private final ChannelDataLoader channelReader;
    private final ChannelDataWriter channelWriter;

    private final String songKey;
    private final ClientResources clientResources;

    public AudioClient(String songKey, ClientResources clientResources) throws ChannelConnectionException {
        checkArgumentNotNull(songKey, "song key");
        checkArgumentNotNull(songKey, "client state");

        this.songKey = songKey;
        this.clientResources = clientResources;

        this.isRunning = true;
        this.formatBuffer = ByteBuffer.allocate(BUFFER_CAPACITY);
        this.socketChannel = initializeSocketChannel();
        this.channelReader = new ChannelDataLoader(socketChannel);
        this.channelWriter = new ChannelDataWriter(socketChannel);
    }

    @Override
    public void run() {
        clientResources.setStreaming(true);

        try {
            String response = channelReader.loadData();
            channelWriter.saveData("play " + songKey);
            startStreamingLoop(getSourceDataLine());
            endStream();
        } catch (LineUnavailableException | IOException | SerializationDataException | DeserializationDataException e) {
            System.out.println(e.getMessage());
            //handleStreamingError(e);
        } finally {
            stop();
        }
    }

    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    public void stop() {
        isRunning = false;
        clientResources.setStreaming(false);
        clientResources.setAudioClient(null);
        //clientResources.stopStreaming();
    }

    //todo: use udp for better performance
    private SocketChannel initializeSocketChannel() throws ChannelConnectionException {
        try {
            SocketChannel socket = SocketChannel.open();
            socket.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            return socket;
        } catch (IOException e) {
            throw new ChannelConnectionException("Could not connect to server in order to stream music!", e);
        }
    }

    public SocketChannel getChannel() {
        return socketChannel;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    private void startStreamingLoop(SourceDataLine sourceLine) throws IOException, SerializationDataException, DeserializationDataException {
        while (isRunning) {
            channelWriter.saveData("stream");
            //channelWriter.writeTo(socketChannel, "stream");

            byte[] audioData = channelReader.loadBytes(); //channelReader.readBytesFrom(socketChannel);

            if (audioData.length > 0) {
                sourceLine.write(audioData, 0, audioData.length);
            } else {
                isRunning = false;
            }
        }

        closeSourceLine(sourceLine);
    }

    private SourceDataLine getSourceDataLine() throws IOException, LineUnavailableException {
        AudioFormat format = readFormat(formatBuffer, socketChannel);

        try {
            SourceDataLine sourceLine = AudioSystem.getSourceDataLine(format);
            sourceLine.open(format);
            sourceLine.start();

            return sourceLine;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported audio format!", e);
        }
    }

    private AudioFormat readFormat(ByteBuffer buffer, SocketChannel socket) throws IOException {
        buffer.clear();
        socket.read(buffer);

        buffer.flip();
        float sampleSize = buffer.getFloat();
        int sampleSizeInBits = buffer.getInt();
        int channels = buffer.getInt();
        int frameSize = buffer.getInt();
        float frameRate = buffer.getFloat();
        boolean bigEndian = buffer.get() == 1;
        int encodingLength = buffer.getInt();
        byte[] encodingBytes = new byte[encodingLength];
        buffer.get(encodingBytes);
        String encodingStr = new String(encodingBytes, StandardCharsets.UTF_8);

        AudioFormat.Encoding encoding = new AudioFormat.Encoding(encodingStr);
//        String encodingStr = StandardCharsets.UTF_8.decode(buffer).toString();
//        AudioFormat.Encoding encoding = new AudioFormat.Encoding(encodingStr);

        return new AudioFormat(encoding, sampleSize, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
    }

    private void endStream() throws IOException, SerializationDataException {
        channelWriter.saveData("stop");
        //channelWriter.writeTo(socketChannel, "stop");

        if (socketChannel.isOpen()) {
            socketChannel.close();
        }
    }

    private void closeSourceLine(SourceDataLine sourceDataLine) {
        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();
    }

}

//package bg.sofia.uni.fmi.mjt.spotify.client.audio;
//
//import bg.sofia.uni.fmi.mjt.spotify.client.resources.ClientResources;
//import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataLoader;
//import bg.sofia.uni.fmi.mjt.spotify.data.channel.ChannelDataWriter;
//import bg.sofia.uni.fmi.mjt.spotify.exception.checked.ChannelConnectionException;
//import bg.sofia.uni.fmi.mjt.spotify.exception.checked.DeserializationDataException;
//import bg.sofia.uni.fmi.mjt.spotify.exception.checked.SerializationDataException;
//import bg.sofia.uni.fmi.mjt.spotify.server.audio.model.SerializableAudioFormat;
//
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.DataLine;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.SourceDataLine;
//import java.io.ByteArrayInputStream;
//import java.io.EOFException;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.checkArgumentNotNull;
//import static bg.sofia.uni.fmi.mjt.spotify.validator.Validator.validateString;
//
//public class AudioClient implements Runnable {
//
//    private static final int PORT = 8080;
//    private static final String HOST = "localhost";
//    private static final int BUFFER_CAPACITY = 4096;
//
//    private SocketChannel socketChannel;
//    private final ByteBuffer buffer;
//
//    private final ChannelDataWriter channelDataWriter;
//    private final ChannelDataLoader channelDataLoader;
//
//    private final ClientResources clientResources;
//    private volatile boolean isRunning;
//
//    private final String songName;
//
//    public AudioClient(String songName, ClientResources clientResources) throws ChannelConnectionException {
//        checkArgumentNotNull(clientResources, "client resources");
//        validateString(songName, "song name");
//
//        prepareSocketChannel();
//        buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
//        channelDataLoader = new ChannelDataLoader(socketChannel);
//        channelDataWriter = new ChannelDataWriter(socketChannel);
//        isRunning = true;
//        this.clientResources = clientResources;
//        this.songName = songName;
//    }
//
//    public void setIsRunning(boolean isRunning) {
//        this.isRunning = isRunning;
//    }
//
//    public SocketChannel getChannel() {
//        return socketChannel;
//    }
//
//    private void prepareSocketChannel() throws ChannelConnectionException {
//        try {
//            socketChannel = SocketChannel.open();
//            socketChannel.connect(new InetSocketAddress(HOST, PORT));
//        } catch (IOException e) {
//            throw new ChannelConnectionException("Unable to connect the streaming channel to the server!", e);
//        }
//
//    }
//
//    @Override
//    public void run() {
//        try {
//            channelDataWriter.saveData("play " + songName);
//            clientResources.setStreaming(true);
//            startStreamingLoop(getSourceDataLine());
//            channelDataWriter.saveData("stop");
//            if (socketChannel.isOpen()) {
//                socketChannel.close();
//            }
//        } catch (IOException e)  {
//            System.out.println("Cannot close the streaming channel!" + e.getMessage());
//        } catch (SerializationDataException | DeserializationDataException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            isRunning = false;
//            clientResources.setStreaming(false);
//            clientResources.setAudioClient(null);
//        }
//    }
//
//    private SourceDataLine getSourceDataLine() throws DeserializationDataException {
//        try {
//            System.out.println("влизам в сорс дате лаин");
//            AudioFormat format = readFormat();
//            System.out.println(format);
//
//            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
//
//            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
//            sourceDataLine.open();
//            sourceDataLine.start();
//
//            System.out.println("връщам сорс дате лаин" + sourceDataLine);
//            return sourceDataLine;
//        } catch (LineUnavailableException e) {
//            throw new DeserializationDataException("Unable to open an audio line!", e);
//        }
//    }
//
//    private AudioFormat readFormat() throws DeserializationDataException {
//        try {
//            buffer.clear();
//            socketChannel.read(buffer);
//
//            buffer.flip();
//            float sampleSize = buffer.getFloat();
//            int sampleSizeInBits = buffer.getInt();
//            int channels = buffer.getInt();
//            int frameSize = buffer.getInt();
//            float frameRate = buffer.getFloat();
//            boolean bigEndian = buffer.get() == 1;
//
//            int encodingLength = buffer.getInt();
//            byte[] encodingBytes = new byte[encodingLength];
//            buffer.get(encodingBytes);
//            String encodingStr = new String(encodingBytes, StandardCharsets.UTF_8).trim();
//
//            System.out.println("Encoding string after trim: '" + encodingStr + "'");
//
//            AudioFormat.Encoding encoding = new AudioFormat.Encoding(encodingStr);
//            //String encodingStr = StandardCharsets.UTF_8.decode(buffer).toString();
//            //AudioFormat.Encoding encoding = new AudioFormat.Encoding(encodingStr);
//
//            return new AudioFormat(encoding, sampleSize, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
//        } catch (IOException e) {
//            throw new DeserializationDataException("Unable to get the format of the song!", e);
//        }
//    }
//
//    private void startStreamingLoop(SourceDataLine sourceDataLine) throws DeserializationDataException, SerializationDataException {
//        checkArgumentNotNull(sourceDataLine, "source data line");
//        System.out.println("predi while sum");
//        while (isRunning) {
//            System.out.println("vutre v while sum");
//            channelDataWriter.saveData("stream");
//            byte[] data = channelDataLoader.loadBytes();
//            System.out.println("data: " + Arrays.toString(data));
//            if (data.length > 0) {
//                System.out.println("пращам дата v " + LocalDateTime.now());
//                sourceDataLine.write(data, 0, data.length);
//            } else {
//                isRunning = false;
//            }
//            break;
//        }
//        sourceDataLine.drain();
//        sourceDataLine.stop();
//        sourceDataLine.close();
//    }
//}

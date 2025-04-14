package bg.sofia.uni.fmi.mjt.spotify.client.resources;

import bg.sofia.uni.fmi.mjt.spotify.client.audio.AudioClient;

import javax.sound.sampled.AudioInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientResources {

    private boolean isClientLogged = false;
    private String loggedClientEmail = null;
    private SocketChannel clientChannel = null;
    private static final int BUFFER_SIZE = 1024;
    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    private boolean isStreaming = false;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private AudioClient audioClient = null;

    public void setLoggedClientEmail(String loggedClientUsername) {
        this.loggedClientEmail = loggedClientUsername;
    }

    public String getLoggedClientEmail() {
        return loggedClientEmail;
    }

    public boolean isClientLogged() {
        return isClientLogged;
    }

    public void setClientLogged(boolean clientLogged) {
        isClientLogged = clientLogged;
    }

    public SocketChannel getClientChannel() {
        return clientChannel;
    }

    public void setClientChannel(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public boolean isStreaming() {
        return isStreaming;
    }

    public void setStreaming(boolean streaming) {
        isStreaming = streaming;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public AudioClient getAudioClient() {
        return audioClient;
    }

    public void setAudioClient(AudioClient audioClient) {
        this.audioClient = audioClient;
    }
}

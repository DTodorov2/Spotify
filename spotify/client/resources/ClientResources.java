package bg.sofia.uni.fmi.mjt.spotify.client.resources;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientResources {

    private boolean isClientLogged = false;
    private SocketChannel clientChannel = null;
    private static final int BUFFER_SIZE = 1024;
    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

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
}

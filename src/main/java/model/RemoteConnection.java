package model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class defines connection behavior
 * when we connect to remote host.
 */
public final class RemoteConnection extends Connection {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(RemoteConnection.class.getName());

    @NotNull private final InetAddress host;
    private int port;

    public RemoteConnection(@NotNull InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createSocket(int timeout) {
        try {
            socket = new Socket();
            LOGGER.log(Level.INFO, String.format("trying to connect to %s on port %d", host, port));
            socket.connect(new InetSocketAddress(host, port), timeout);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "could not connect", e);
            close();
            return false;
        }
        LOGGER.log(Level.INFO, "connected to remote host");

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "could not close socket", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return host.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return port;
    }

}

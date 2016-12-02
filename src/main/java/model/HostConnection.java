package model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class defines connection behavior
 * when we act as a host. In general, this
 * class differs from RemoteConnection on
 * in {@link HostConnection#startServer()} method which should be
 * called here before {@link HostConnection#createSocket(int)}
 */
public final class HostConnection extends Connection {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(HostConnection.class.getName());

    private ServerSocket serverSocket;

    public HostConnection() {
    }

    /**
     * Create server socket which will listen to some port
     * Port number can be obtained through {@link HostConnection#getPort()}
     * method
     *
     * @throws IOException from ServerSocket constructor
     */
    public void startServer() throws IOException {
        serverSocket = new ServerSocket(0);
    }

    /**
     * see {@link Connection#createSocket(int)}
     *
     * @param timeout ignored (as we act like host and just wait for connection)
     */
    @Override
    public boolean createSocket(int timeout) {
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "could not accept connection", e);
            return false;
        }
        LOGGER.log(Level.INFO, "accepted connection from remote host");

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        closeSocket();
        closeServerSocket();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return serverSocket.getInetAddress().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return serverSocket.getLocalPort();
    }

    private void closeSocket() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error while closing socket", e);
        }
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error while closing server socket", e);
        }
    }

}

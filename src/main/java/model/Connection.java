package model;

import common.Proto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Connection {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());

    protected Socket socket;

    /**
     * create socket for communication
     *
     * @param timeout timeout for socket creation
     * @return true if socket was created
     */
    public abstract boolean createSocket(int timeout);

    /**
     * get host of current connection
     * @return host of this connection
     */
    public abstract String getHost();

    /**
     * get port of current connection
     * @return port of this connection
     */
    public abstract int getPort();

    /**
     * close connection
     */
    public abstract void close();

    /**
     * send message to remote client
     *
     * @param message message to send
     * @return true if message was sent
     */
    public boolean send(@NotNull Proto.Message message) {
        LOGGER.log(Level.INFO, String.format("trying to send message `%s`", message.getContent()));

        if (socket == null) {
            LOGGER.log(Level.WARNING, "trying to write to null socket");
            return false;
        }

        try {
            message.writeDelimitedTo(socket.getOutputStream());
            socket.getOutputStream().flush();
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error while sending message", e);
            return false;
        }
    }

    /**
     * receive one message from remote clients
     *
     * @return message received
     */
    @Nullable
    public Proto.Message receive() {
        if (socket == null) {
            LOGGER.log(Level.WARNING, "trying to receive from null socket");
            return null;
        }

        try {
            return Proto.Message.parseDelimitedFrom(socket.getInputStream());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error while receiving message", e);
            return null;
        }
    }

    /**
     * check if input stream is not empty
     *
     * @return true if socket's input stream is not empty
     */
    public boolean hasPendingMessages() {
        try {
            return socket.getInputStream().available() > 0;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "could not check input stream", e);
            return false;
        }
    }

}

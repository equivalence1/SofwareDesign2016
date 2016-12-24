package ru.mit.spbau.model;

import ru.mit.spbau.common.Proto;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

public abstract class Connection {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());

    protected Socket socket;
    @NotNull protected final Queue<Proto.Message> messages;

    public Connection() {
        messages = new LinkedBlockingDeque<>();
    }

    /**
     * wait connection
     *
     * @param timeout timeout for connection creation
     * @return true if connection was established
     */
    public abstract boolean waitConnection(int timeout);

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
    public abstract boolean send(@NotNull Proto.Message message);

    /**
     * receive one message from remote clients
     *
     * @return message received
     */
    @NotNull
    public Proto.Message receive() {
        return messages.poll();
    }

    /**
     * check if input stream is not empty
     *
     * @return true if socket's input stream is not empty
     */
    public boolean hasPendingMessages() {
        return messages.size() > 0;
    }

}

package ru.mit.spbau.model;

import ru.mit.spbau.common.MessengerGrpc;
import ru.mit.spbau.common.Proto;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CompletableFuture;

public final class HostConnection extends Connection {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(HostConnection.class.getName());

    @NotNull private final CompletableFuture<StreamObserver<Proto.Message>> responseObserverGetter;
    private StreamObserver<Proto.Message> responseObserver;
    private Server server;

    public HostConnection() throws IOException {
        super();
        responseObserverGetter = new CompletableFuture<>();
        server = ServerBuilder.forPort(0).addService(new ConnectionService()).build();
        start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean waitConnection(int timeout) {
        try {
            responseObserver = responseObserverGetter.get();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not accept connection", e);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return "localhost";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return server.getPort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean send(@NotNull Proto.Message message) {
        if (responseObserver == null) {
            return false;
        }
        responseObserver.onNext(message);
        return true;
    }

    private void start() throws IOException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                HostConnection.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private class ConnectionService extends MessengerGrpc.MessengerImplBase {
        @Override
        public StreamObserver<Proto.Message> sendMessage(StreamObserver<Proto.Message> responseObserver) {
            LOGGER.log(Level.INFO, "gRPC connection established");
            responseObserverGetter.complete(responseObserver);
            return new StreamObserver<Proto.Message>() {
                @Override
                public void onNext(Proto.Message msg) {
                    LOGGER.log(Level.INFO, "got a message.");
                    messages.add(msg);
                }

                @Override
                public void onError(Throwable t) {
                    LOGGER.log(Level.WARNING, "error occurred", t);
                }

                @Override
                public void onCompleted() {
                    LOGGER.log(Level.INFO, "finished server");
                    responseObserver.onCompleted();
                }
            };
        }
    }


}

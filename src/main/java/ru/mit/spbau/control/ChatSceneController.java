package ru.mit.spbau.control;

import ru.mit.spbau.common.Proto;
import ru.mit.spbau.model.ChatTask;
import ru.mit.spbau.model.Connection;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.ChatSceneView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for {@link ChatSceneView}.
 * Also glues chat task and chat screen
 */
public final class ChatSceneController extends SceneController {

    @NotNull private static final Logger LOGGER = Logger.getLogger(ChatSceneController.class.getName());

    @NotNull private final Connection connection;

    private ChatSceneView view;
    private ChatTask task;

    public ChatSceneController(@NotNull GeneralController controller, @NotNull Connection connection) {
        super(controller);
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startControl() {
        task = new ChatTask(controller.getUserInfo().getUserName(), connection);
        task.registerLocalDisplayer(this::displayLocalMessage);
        task.registerRemoteDisplayer(this::displayRemoteMessage);
        view = new ChatSceneView(this);
        controller.getSceneManager().showNextScene(view);
        task.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopControl() {
        task.stop();
    }

    /**
     * display message which our user sent to remote host
     * @param message which was send
     */
    public void displayLocalMessage(Proto.Message message) {
        runOnPlatform(() -> view.displayLocalMessage(message));
    }

    /**
     * display message which we received from remote user
     * @param message which we received
     */
    public void displayRemoteMessage(Proto.Message message) {
        runOnPlatform(() -> view.displayRemoteMessage(message));
    }

    /**
     * send message to remote user
     * @param content content of message
     */
    public void sendMessage(String content) {
        LOGGER.log(Level.INFO, String.format("adding message `%s` to send queue", content));
        task.addToSend(content);
    }

}

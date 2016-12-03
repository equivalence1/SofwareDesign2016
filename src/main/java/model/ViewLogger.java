package model;

/**
 * Interface for view loggers
 * Used primarily in WaitTask
 */
@FunctionalInterface
public interface ViewLogger {

    /**
     * log message to user on screen
     *
     * @param msg message content
     */
    void log(String msg);

}

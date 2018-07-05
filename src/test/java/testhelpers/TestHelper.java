package testhelpers;

import java.util.concurrent.TimeUnit;

public class TestHelper {

    /**
     * Causes the thread to sleep for N milliseconds
     *
     * @param timeout Time to sleep in milliseconds
     */
    public static void sleep(long timeout) {
        ignoreExceptions(() -> {
            TimeUnit.SECONDS.sleep(timeout);
        });
    }

    /**
     * Ignores ALL exceptions that may be thrown by the Runnable
     *
     * @param runnable Runnable lambda
     */
    public static void ignoreExceptions(RunnableIgnoreExceptions runnable) {
        try {
            runnable.run();
        } catch (Exception exception) {
            // Do nothing
        }
    }

    @FunctionalInterface
    public interface RunnableIgnoreExceptions {
        void run() throws Exception;
    }
}

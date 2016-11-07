package org.firstinspires.ftc.team9853;

/**
 * thrown when the stopp button is called and the robot needs to be interupted
 */
public class StoppedException extends Exception {

    /**
     * Constructs a new {@code StoppedException} that includes the current
     * stack trace.
     */
    public StoppedException() {
    }

    /**
     * Constructs a new {@code StoppedException} with the current stack
     * trace and the specified detail message.
     *
     * @param detailMessage
     *            the detail message for this exception.
     */
    public StoppedException(String detailMessage) {
        super(detailMessage);
    }
}

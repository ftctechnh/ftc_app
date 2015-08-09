package org.swerverobotics.library.exceptions;

/**
 * SwerveRuntimeException is a base for all runtime exceptions defined in the Swerve Robotics library
 */
public class SwerveRuntimeException extends RuntimeException
    {
    /**
     * wrap() takes an exception, of any flavor, and wraps it (only if necessary) to yield
     * a RuntimeException. The method is idempotent.
     *
     * The following exception mappings take place:
     *      RuntimeException     => itself
     *      InterruptedException => RuntimeInterruptedException
     *      other                => RuntimeException
     */
    public static RuntimeException wrap(Exception e)
        {
        if (e instanceof RuntimeException)
            return (RuntimeException)e;

        if (e instanceof InterruptedException)
            return new RuntimeInterruptedException((InterruptedException)e);

        return new RuntimeException(e);
        }
    }

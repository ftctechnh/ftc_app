package org.swerverobotics.library.exceptions;

/**
 * SwerveRuntimeException is a base for all runtime exceptions defined in the Swerve Robotics library
 */
public abstract class SwerveRuntimeException extends RuntimeException
    {
    //----------------------------------------------------------------------------------------------
    // Wrapping
    //----------------------------------------------------------------------------------------------

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

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /**
     * Constructs a new {@code SwerveRuntimeException} that includes the current stack trace.
     */
    protected SwerveRuntimeException()
        {
        }

    /**
     * Constructs a new {@code SwerveRuntimeException} with the current stack trace
     * and the specified detail message.
     *
     * @param detailMessage     the detail message for this exception.
     */
    protected SwerveRuntimeException(String detailMessage)
        {
        super(detailMessage);
        }

   /**
     * Constructs a new {@code SwerveRuntimeException} with the current stack trace,
     * the specified detail message and the specified cause.
     *
     * @param detailMessage     the detail message for this exception.
     * @param throwable         the cause of this exception.
     */
    protected SwerveRuntimeException(String detailMessage, Throwable throwable)
        {
        super(detailMessage, throwable);
        }

    /**
     * Constructs a new {@code RuntimeException} with the current stack trace
     * and the specified cause.
     *
    * @param throwable          the cause of this exception.
     */
    protected SwerveRuntimeException(Throwable throwable)
        {
        super(throwable);
        }

    }

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
     * Takes an exception, of any flavor, and wraps it (only if necessary) to yield
     * a RuntimeException. The method is idempotent. The following exception mappings are made:
     * <table summary="Exception Mappings">
     *     <tr><td>RuntimeException    </td><td>=&gt;</td><td>itself</td></tr>
     *     <tr><td>InterruptedException</td><td>=&gt;</td><td>RuntimeInterruptedException</td></tr>
     *     <tr><td>other               </td><td>=&gt;</td><td>RuntimeException</td></tr>
     * </table>
     *
     * @param e the exception which is to be wrapped
     * @return the (possibly new) wrapper for the to-be-wrapped exception
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
     * Constructs a new {@link SwerveRuntimeException} that includes the current stack trace.
     */
    protected SwerveRuntimeException()
        {
        }

    /**
     * Constructs a new {@link SwerveRuntimeException} with the current stack trace
     * and the specified detail message.
     *
     * @param detailMessage     the detail message for this exception.
     */
    protected SwerveRuntimeException(String detailMessage)
        {
        super(detailMessage);
        }

   /**
     * Constructs a new {@link SwerveRuntimeException} with the current stack trace,
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
     * Constructs a new {@link SwerveRuntimeException} with the current stack trace
     * and the specified cause.
     *
    * @param throwable          the cause of this exception.
     */
    protected SwerveRuntimeException(Throwable throwable)
        {
        super(throwable);
        }

    }

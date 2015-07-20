package org.swerverobotics.library;

/**
 * SwerveRuntimeException is a base for all runtime exceptions defined in the Swerve Robotics library
 */
public class SwerveRuntimeException extends RuntimeException
    {
    public static RuntimeException Wrap(Exception e)
        {
        if (e instanceof RuntimeException)
            return (RuntimeException)e;

        if (e instanceof InterruptedException)
            return new RuntimeInterruptedException((InterruptedException)e);

        return new RuntimeException(e);
        }
    }

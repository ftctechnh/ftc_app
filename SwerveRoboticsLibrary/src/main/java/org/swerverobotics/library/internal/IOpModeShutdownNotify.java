package org.swerverobotics.library.internal;

/**
 *
 */
public interface IOpModeShutdownNotify
    {
    boolean onUserOpModeStop();

    boolean onRobotShutdown();
    }

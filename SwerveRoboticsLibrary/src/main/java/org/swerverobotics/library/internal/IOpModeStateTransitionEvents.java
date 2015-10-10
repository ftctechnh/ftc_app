package org.swerverobotics.library.internal;

/**
 * An interface used to receive notifications about various OpMode state transitions
 */
public interface IOpModeStateTransitionEvents
    {
    /**
     * A user OpMode has just stopped. Robot motion should cease.
     * @return if true, the receiver will not receive *any* more opmode state transitions
     */
    boolean onUserOpModeStop();

    /**
     * The robot is in the process of shutting down. All hardware devices, for example,
     * should close().
     * @return if true, the receiver will not receive *any* more opmode state transitions
     * @see com.qualcomm.robotcore.hardware.HardwareDevice#close()
     */
    boolean onRobotShutdown();
    }

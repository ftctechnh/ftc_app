package org.swerverobotics.library.interfaces;

import java.lang.annotation.*;

/**
 * OnRobotRunning annotations can be placed on public static methods which are to be
 * invoked when the robot object in the robot controller application reaches the running
 * state after an initial boot or a 'robot restart'.
 * @see OnRobotStartupFailure
 * @see OpModeRegistrar
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnRobotRunning
    {
    }

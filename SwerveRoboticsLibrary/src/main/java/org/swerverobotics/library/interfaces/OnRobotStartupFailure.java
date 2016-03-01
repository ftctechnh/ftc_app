package org.swerverobotics.library.interfaces;

import java.lang.annotation.*;

/**
 * OnRobotStartupFailure annotations can be placed on public static methods which are to be
 * invoked when the robot object in the robot controller application fails as it is starting up.
 * @see OnRobotRunning
 * @see OpModeRegistrar
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnRobotStartupFailure
    {
    }
